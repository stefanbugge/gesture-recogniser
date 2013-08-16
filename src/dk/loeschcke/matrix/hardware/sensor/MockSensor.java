package dk.loeschcke.matrix.hardware.sensor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.loeschcke.matrix.image.PixelFrame;
import dk.loeschcke.matrix.util.Library;
import dk.loeschcke.matrix.util.MockData;

public class MockSensor implements Sensor {
	
	private static final Logger log = LoggerFactory.getLogger(MockSensor.class);
	
	private BufferedReader reader;
	
	boolean isReading = false;
	
	private final String[] NOTHING = MockData.NOTHING.split(",");

	public MockSensor() { }
	
	private void loadGesture(String gesture) {
		try {
			if (reader != null) {
				reader.close();
			}
			FileReader fileReader = new FileReader(new File("mocks" + File.separator + gesture + ".gesture"));
			reader = new BufferedReader(fileReader);
			isReading = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int[] getSensorData() {
		String[] stringData = null;
		try {
			if (isReading) {
				String line = reader.readLine();
				if (line == null) {
					reader.close();
					isReading = false;
					log.debug("gesture stream done.");
					stringData = NOTHING;
				} else {
					isReading = true;
					stringData = line.split(",");
				}
			} else {
				stringData = NOTHING;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int[] data = new int[Library.FRAME_WIDTH * Library.FRAME_HEIGHT];
		for (int i = 0; i < stringData.length; i++) {
			try {
				data[i] = Integer.parseInt(stringData[i]);
			} catch (NumberFormatException e) {
				data[i] = 0;
			}
		}
		return data;
	}

//	@Override
//	public PixelFrame getFrame() {
//		String[] data = getData();
//		return new PixelFrame(data, Library.FRAME_WIDTH, Library.FRAME_WIDTH);
//	}
	
	@Override
	public int[] getData() {
		return getSensorData();
	}
	

	public void update(String message) {
		String gesture = message;
		log.debug("load gesture: " + gesture);
		loadGesture(gesture);
	}

	@Override
	public void connect() {
		log.debug("sensor connected.");
	}

	@Override
	public void disconnect() {
		log.debug("sensor disconnected.");
	}
	
}
