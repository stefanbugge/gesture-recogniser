package dk.loeschcke.matrix;

import dk.loeschcke.matrix.controller.DefaultController;
import dk.loeschcke.matrix.gui.MatrixFrame;
import dk.loeschcke.matrix.hardware.actuator.Actuator;
import dk.loeschcke.matrix.hardware.actuator.ArduinoActuator;
import dk.loeschcke.matrix.hardware.sensor.ArduinoSensor;
import dk.loeschcke.matrix.hardware.sensor.MockSensor;
import dk.loeschcke.matrix.hardware.sensor.Sensor;
import dk.loeschcke.matrix.image.CustomScaleStrategy;
import dk.loeschcke.matrix.model.FrameModel;
import dk.loeschcke.matrix.view.frame.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 18/08/13
 * Time: 16.54
 * To change this template use File | Settings | File Templates.
 */
public class Matrix {

    private static final Logger log = LoggerFactory.getLogger(Matrix.class);

    public static void main(String... args) throws IOException {
        log.info("Starting up...");

        final ExecutorService threadPool = Executors.newCachedThreadPool();

        final FrameModel model = new FrameModel();

        final DefaultController controller = new DefaultController();

        log.info("loading sensors...");
        final Sensor arduinoSensor = new ArduinoSensor("/dev/tty.usbserial-A600aid6");
        final Sensor mockSensor = new MockSensor();
        final Actuator actuator = new ArduinoActuator("/dev/tty.usbserial-A600ai0P");

        final Processor2 processor = new Processor2(arduinoSensor, actuator, controller);
        threadPool.execute(processor);
        threadPool.execute((ArduinoActuator) actuator);

        SwingUtilities.invokeLater(new Runnable() {

            final int PANEL_SIZE = 400;

            @Override
            public void run() {

                FramePanel rawPanel = new RawPanel(PANEL_SIZE, PANEL_SIZE);
                FramePanel smoothedPanel = new SmoothedPanel(PANEL_SIZE, PANEL_SIZE, new CustomScaleStrategy());
                FramePanel postRawPanel = new PostRawPanel(PANEL_SIZE, PANEL_SIZE);
                FramePanel pointCloudPanel = new PointCloudPanel(PANEL_SIZE, PANEL_SIZE);

                controller.addModel(model);

                controller.addView(rawPanel);
                controller.addView(smoothedPanel);
                controller.addView(postRawPanel);
                controller.addView(pointCloudPanel);

                MatrixFrame matrix = new MatrixFrame("Matrix");

                matrix.addView(rawPanel);
                matrix.addView(smoothedPanel);
                matrix.addView(postRawPanel);
                matrix.addView(pointCloudPanel);

                JButton arduinoSensorBtn = new JButton();
                arduinoSensorBtn.setText("Arduino Sensor");
                arduinoSensorBtn.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        processor.setSensor(arduinoSensor);
                    }

                });
                matrix.addButton(arduinoSensorBtn);

                JButton mockSensorBtn = new JButton();
                mockSensorBtn.setText("Mock Sensor");
                mockSensorBtn.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        processor.setSensor(mockSensor);
                    }

                });
                matrix.addButton(mockSensorBtn);

                final Sensor sensor = processor.getSensor();
                if (sensor instanceof MockSensor) {
                    JButton circleBtn = new JButton();
                    circleBtn.setText("circle");
                    circleBtn.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            ((MockSensor) sensor).update("circle");
                        }

                    });
                    matrix.addButton(circleBtn);
                }
                processor.start();
            }
        });

        // Wait for newline, then interrupt threads and exit
        log.info("Press enter to exit program.");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.readLine();

        log.debug("shutdown: disconnect sensors");
        processor.disconnectSensors();
        log.debug("shutdown: sensors disconnected");

        threadPool.shutdownNow();

        // Wait for everything to clean up before exiting
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error("Interrupted.", e);
        }

        System.exit(0);

    }
}
