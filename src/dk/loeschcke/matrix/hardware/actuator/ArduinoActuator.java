package dk.loeschcke.matrix.hardware.actuator;

import dk.loeschcke.matrix.hardware.Arduino;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ArduinoActuator extends Arduino implements Actuator, Runnable {

	private static final Logger log = LoggerFactory.getLogger(ArduinoActuator.class);

	private BlockingQueue<int[]> queue = new ArrayBlockingQueue<int[]>(5);

	public ArduinoActuator(String portname) {
		super(portname);
	}

    public void connect() {
        super.connect();
    }

    public void disconnect() {
        super.disconnect();
    }

	@Override
	public void run() {
		connect();

		try {
			while (!Thread.interrupted()) {

				int[] fb = queue.take();

				String sendValue = String.format("%03d%03d%04d", fb[0], fb[1], fb[2]);
                //log.debug("sending: " + sendValue);
				writer.write(sendValue);
				writer.flush();

				String line = reader.readLine();
				//log.debug("do fb: confirm reception: " + fb[0] + ":" + fb[1] + ":" + fb[2] + " == " + line);
				Thread.sleep(100);

			}
		} catch (IOException e) {
			log.error("Lost connection", e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.debug("Interrupted, exiting.");
		disconnect();
	}

    public static void main(String[] args) throws InterruptedException {
        ArduinoActuator arduinoActuator = new ArduinoActuator("/dev/tty.usbserial-A600ai0P");
        new Thread(arduinoActuator).start();

        Thread.sleep(1000);
        arduinoActuator.sendFeedback(FeedbackType.VIBRATE, 1, 9000);
        Thread.sleep(7000);
//        arduinoActuator.sendFeedback(FeedbackType.LED_ON, 1, 0);
//        Thread.sleep(2000);
//        arduinoActuator.sendFeedback(FeedbackType.LED_OFF, 2, 0);
//        Thread.sleep(2000);
//        arduinoActuator.sendFeedback(FeedbackType.LED_BLINK, 4, 200);
//        Thread.sleep(2000);
//        arduinoActuator.sendFeedback(FeedbackType.VIBRATE, 4, 400);
//        Thread.sleep(2000);
//        arduinoActuator.sendFeedback(FeedbackType.VIBRATE, 0, 200);
//        Thread.sleep(2000);
        System.exit(0);
    }

    @Override
	public void sendFeedback(int type, int repeats, int value) {
        int[] fb = new int[] {type, repeats, value};
		if (!queue.offer(fb)) {
			log.debug("Could not add vibration to queue.");
		}
	}

    public class FeedbackType {
        public static final int LED_ON      = 0;
        public static final int LED_OFF     = 1;
        public static final int LED_BLINK   = 2;
        public static final int VIBRATE     = 3;
        public static final int DATA        = 4;
        public static final int VIBRATE_ON  = 5;
        public static final int VIBRATE_OFF = 6;
    }
}
