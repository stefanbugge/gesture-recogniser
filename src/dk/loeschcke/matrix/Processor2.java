package dk.loeschcke.matrix;

import dk.loeschcke.matrix.controller.DefaultController;
import dk.loeschcke.matrix.hardware.actuator.Actuator;
import dk.loeschcke.matrix.hardware.actuator.ArduinoActuator;
import dk.loeschcke.matrix.helper.Action;
import dk.loeschcke.matrix.helper.HistoryHelper;
import dk.loeschcke.matrix.helper.PointV;
import dk.loeschcke.matrix.model.PixelFrame2;
import dollarrecognizer.P.Point;
import dk.loeschcke.matrix.filter.AveragingDataFilter;
import dk.loeschcke.matrix.filter.DataFilter;
import dk.loeschcke.matrix.hardware.sensor.Sensor;
import dk.loeschcke.matrix.util.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Processor2 implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Processor2.class);

    private final DefaultController controller;

    private Sensor sensor;
    private Actuator actuator;

    private PixelFrame2 pixelFrame = null;

    private boolean started = false;

    private static List<DataFilter> dataFilters = new ArrayList<DataFilter>();
    static {
        dataFilters.add(new AveragingDataFilter());
    }

    private Initiator initiator;
    private static final int inputCount = 3;

    public Processor2(Sensor sensor, Actuator actuator, DefaultController controller) {
        this.actuator = actuator;
        this.controller = controller;
        this.initiator = new Initiator();
        setSensor(sensor);
    }

    public void setSensor(Sensor sensor) {
        if (this.sensor != sensor) {
            if (this.sensor != null) {
                this.sensor.disconnect();
            }
            this.sensor = sensor;
            sensor.connect();
        }
    }

    public void disconnectSensors() {
        if (sensor != null) {
            sensor.disconnect();
        }
        if (actuator != null) {
            actuator.disconnect();
        }
    }

    public Sensor getSensor() {
        return sensor;
    }

    @Override
    public void run() {

        while (!started) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }

        // last 20 points
        HistoryHelper<PointV> pointHistory = new HistoryHelper<PointV>(12);
        // last 20 frames
        //HistoryHelper<PixelFrame> frameHistory = new HistoryHelper<PixelFrame>(20);

        // 1 stroke
        ArrayList<Point> pointCloud = new ArrayList<Point>();

        PointV pMaxPrev = new PointV(-1,-1, 0);
        PointV pMax = null;

        long startTime = System.currentTimeMillis();
        final long LIMIT = 1200;

        long timeout = 800;
        long prevTimestamp = System.currentTimeMillis();
        int strokeID = -1;

        long time_tab_active = 0;
        long tab_timeout_before_gesture = 3000;
        long gesture_timeout = 5000;

        Action currentAction = Action.GESTURE;

        while (!Thread.interrupted()) {

            int[] data = sensor.getData();
            for (DataFilter filter : dataFilters) {
                data = filter.filter(data);
            }
            pixelFrame = new PixelFrame2(data, inputCount, Library.FRAME_WIDTH, Library.FRAME_HEIGHT);

            if (pixelFrame != null) {
                pMax = pixelFrame.getMax();
                //log.debug("number of max max points: " + pixelFrame.getMaxPoints().size());
                //frameHistory.append(pixelFrame);
                switch (currentAction) {
                    case NONE:
                        if (pMax != null) {
                            pointHistory.append(pMax);
                            if (pointHistory.isFull()) {
                                if (isTap(pointHistory)) {
                                    time_tab_active = System.currentTimeMillis();
                                    log.info("TAPPED");
                                    pointHistory.reset();
                                    log.info("size: " + pointHistory.size());
                                    currentAction = Action.GESTURE;
                                }
                            }
                        } else {

                        }
                        break;
                    case GESTURE:
//                        if (System.currentTimeMillis() - time_tab_active > gesture_timeout) {
//                            log.info("gesture timed out");
//                            startTime = System.currentTimeMillis();
//                            pointCloud = new ArrayList<Point>();
//                            strokeID = -1;
//                            currentAction = Action.NONE;
//                        }
                        if (pointHasChanged(pMax, pMaxPrev)) {
                            if (pMax != null) {
                                actuator.sendFeedback(ArduinoActuator.FeedbackType.DATA, 0, 0);
                                //actuator.sendFeedback(ArduinoActuator.FeedbackType.VIBRATE_ON, 0, 0);
                                long timestamp = System.currentTimeMillis();
                                if (timestamp - prevTimestamp > timeout) {
                                    strokeID++;
                                    prevTimestamp = timestamp;
                                    log.debug("new stroke: " + strokeID);
                                }
                                Point newPoint = new PointV(pMax.X, pMax.Y, pMax.V, strokeID);
                                pointCloud.add(newPoint);
                                startTime = System.currentTimeMillis();
                                //if (pointCloud.size() > 1) {
                                    //double distance = SpaceHelper.distance(newPoint, pointCloud.get(pointCloud.size()-2));
                                    //System.out.println("distance: " + distance);
                                //}
                                //log.debug(pointCloud.get(pointCloud.size()-1).toString());
                            }
                        } else {
                            if (!pointCloud.isEmpty()) {
                                //if (!points.isEmpty()) {
                                long time = System.currentTimeMillis();
                                if (time - startTime > LIMIT) {
                                    //actuator.sendFeedback(ArduinoActuator.FeedbackType.VIBRATE_OFF, 0, 0);
                                    log.debug("point cloud size: " + pointCloud.size());
                                    if (pointCloud.size() >= 6) {

                                        log.debug("do recognition test");
                                        actuator.sendFeedback(ArduinoActuator.FeedbackType.LED_BLINK, 3, 200);
                                        initiator.takeAction(pointCloud);
                                        actuator.sendFeedback(ArduinoActuator.FeedbackType.VIBRATE, 0, 2000);
                                        currentAction = Action.GESTURE;
                                        log.info("... listening for tap");
                                    } else {
                                        log.debug("... gesture reset");
                                        startTime = System.currentTimeMillis();
                                    }
                                    pointCloud = new ArrayList<Point>();
                                    strokeID = -1;
                                    time_tab_active = System.currentTimeMillis();

                                    controller.resetFrame();
                                }
                            }

                        }
                        break;
                }

                pMaxPrev = pMax;

                controller.updateFrame(pixelFrame);
            }
        }
        log.debug("trying to disconnect");
        disconnectSensors();
        log.debug("disconnected");
    }

    private boolean isTap(HistoryHelper<PointV> pointHistory) {
        final double THRESHOLD = 90;
        double sum = 0.0;
        for (PointV p : pointHistory.get()) {
            sum += p.V;
        }
        double avg = sum / pointHistory.size();
        return avg >= THRESHOLD;  //To change body of created methods use File | Settings | File Templates.
    }


    /*
     * Has the point changed within 1 decimal place.
     */
    private boolean pointHasChanged(double now, double before) {
        int a = (int) (now * 10.0 + 0.5);
        int b = (int) (before * 10.0 + 0.5);
        return a != b;
    }

    private boolean pointHasChanged(PointV now, PointV before) {
        if (now == null || before == null) {
            if (now == null && before == null) return false;
            return true;
        }
        return pointHasChanged(now.X, before.X) || pointHasChanged(now.Y, before.Y);
    }

    public void start() {
        started = true;
    }

}
