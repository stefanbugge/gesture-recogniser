package dk.loeschcke.matrix;

import dk.loeschcke.av.Command;
import dk.loeschcke.matrix.helper.PointV;
import dollarrecognizer.P.DirectionBasedRecogniser;
import dollarrecognizer.P.PDollarRecognizer;
import dollarrecognizer.P.Point;
import dollarrecognizer.P.RecognizerResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 21/08/13
 * Time: 09.24
 * To change this template use File | Settings | File Templates.
 */
public class Initiator {

    private static final Logger log = LoggerFactory.getLogger(Initiator.class);

    static PDollarRecognizer _Prec = new DirectionBasedRecogniser();
    //static PDollarRecognizer _Prec = new PDollarRecognizer();
    private final InitiatorSocket initiatorSocket;

    //private Robot robot;

    public Initiator() {
//        try {
//            robot = new Robot();
//        } catch (AWTException e) {
//            log.error("error initialising a robot", e);
//        }
        this.initiatorSocket = new InitiatorSocket();
        new Thread(initiatorSocket).start();
    }

    public void takeAction(ArrayList<Point> pointCloud) {
        try {

            ArrayList<PointV> pointvs = (ArrayList<PointV>) pointCloud.clone();
            int sum = 0;
            for (int i = 0; i < pointvs.size(); i++) {
                sum += pointvs.get(i).V;
            }
            double avg = sum * 1.0 / pointvs.size();

            long start = System.currentTimeMillis();
            RecognizerResults recognize = _Prec.Recognize((ArrayList<Point>) pointCloud.clone());
            long end = System.currentTimeMillis();
            log.debug("time: " + (end - start));
            log.info(recognize.mName + ": " + recognize.mScore + " (other info: " + recognize.mOtherInfo + ")");

            String gesture = recognize.mName;
            if (gesture.equals("horizontal line")) {
                initiatorSocket.send(Command.PLAY, avg);
            } else if (gesture.equals("vertical line")) {
                initiatorSocket.send(Command.PAUSE, avg);
            } else if (gesture.equals("cross line up")) {
                initiatorSocket.send(Command.VOLUME_UP, avg);
            } else if (gesture.equals("cross line down")) {
                initiatorSocket.send(Command.SKIP_FORWARD, avg);
            } else if (gesture.equals("arrowhead")) {
                initiatorSocket.send(Command.SET_AUDIO, avg);
            } else if (gesture.equals("X")) {
                initiatorSocket.send(Command.SET_VIDEO, avg);
            }


            log.info("avg: " + avg);

//            ArrayList<PointV> pointvs = (ArrayList<PointV>) pointCloud.clone();
//            Collections.sort(pointvs, new Comparator<PointV>() {
//
//                @Override
//                public int compare(PointV o1, PointV o2) {
//                    return (int) (o1.V - o2.V);
//                }
//
//            });
//
//            SimpleRegression regression = new SimpleRegression();
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < pointvs.size(); i++) {
//                PointV pv = (PointV) pointvs.get(i);
//                regression.addData(i, pv.V);
//            }
//            System.out.println(regression.getIntercept());
//// displays intercept of regression line, since we have constrained the constant, 0.0 is returned
//
//            System.out.println(regression.getSlope());
//// displays slope of regression line
//
//            System.out.println(regression.getSlopeStdErr());
//// displays slope standard error
//
//            System.out.println(regression.getInterceptStdErr() );
//// will return Double.NaN, since we constrained the parameter to zero
        } catch (Exception e) {
            log.debug("Error in recogniser", e);
        }
    }

   public class InitiatorSocket implements Runnable {


        private boolean interrupted = false;
        private Command command = Command.NONE;
       private double expressiveness;

       @Override
        public void run() {

            Socket avSocket = null;
            PrintWriter out = null;

            try {
                avSocket = new Socket("localhost", 4444);
                out = new PrintWriter(avSocket.getOutputStream(), true);
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host: localhost.");
                //System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to: localhost.");
                //System.exit(1);
            }
            System.out.println("Connected to localhost on port 4444");
            try {
                while (!interrupted) {

                    synchronized (this) {
                        this.wait();
                    }

                    if (command != null && command != Command.NONE) {
                        out.println(command + "," + expressiveness);
                        command = Command.NONE;
                    }

                }

                out.close();
                avSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
       }

        public void send(Command command, double expressiveness) {
            this.command = command;
            this.expressiveness = expressiveness;
            synchronized (this) {
                this.notify();
            }
        }

       public void interrupt() {
           interrupted = true;
       }
    }

}
