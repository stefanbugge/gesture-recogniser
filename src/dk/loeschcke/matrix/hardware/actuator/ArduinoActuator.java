package dk.loeschcke.matrix.hardware.actuator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.loeschcke.matrix.hardware.Arduino;

public class ArduinoActuator extends Arduino implements Vibrator, Runnable {

	private static final Logger log = LoggerFactory.getLogger(ArduinoActuator.class);
	
	private BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);
	
	public ArduinoActuator(String portname) {
		super(portname);
	}

	@Override
	public void run() {
		connect();

		try {
			while (!Thread.interrupted()) {

				int type = queue.take();

				//%03d%d
				String sendValue = String.format("%d", type);
				
				writer.write(sendValue);
				writer.flush();
				
				String line = reader.readLine();
				log.debug("do vibration: confirm reception: " + type + " == " + line);
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

	@Override
	public void vibrate(int type) {
		if (!queue.offer(type)) {
			log.debug("Could not add vibration to queue.");
		}
	}
}
