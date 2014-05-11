package dk.loeschcke.matrix.hardware.sensor;

import dk.loeschcke.matrix.hardware.Arduino;
import dk.loeschcke.matrix.util.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * SensorArduino reads distances from the Arduino with the Ping sensor, and puts
 * these values in a datatype.
 * 
 * @author markus
 * 
 */
public class ArduinoSensor extends Arduino implements Sensor {

	private static final Logger log = LoggerFactory.getLogger(ArduinoSensor.class);

	public ArduinoSensor(String portname) {
		super(portname);
	}
	
	@Override
	public void connect() {
		super.connect();
		log.debug("sensor connected.");
	}
	
	@Override
	public void disconnect() {
		super.disconnect();
		log.debug("sensor disconnected.");
	}

	public int[] getData() {
		int[] data = new int[Library.FRAME_WIDTH*Library.FRAME_HEIGHT];
		if (reader != null) {
			try {
				String line = reader.readLine(); // 1-dim
				String[] split = line.split(",");
				for (int i = 0; i < data.length; i++) { // split.length may be too large
					try {
						data[i] = Integer.parseInt(split[i]);
					} catch (NumberFormatException e) {
						data[i] = 0;
					} catch (ArrayIndexOutOfBoundsException e) {
                        data[i] = 0;
                    }
				}
			} catch (IOException e) {
				// With high BAUD rates a zero-byte error may occur.
			}
		}
		return data;
	}

	
}
