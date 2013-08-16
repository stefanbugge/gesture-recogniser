package dk.loeschcke.matrix.hardware.sensor;

import $N.NBestList;
import $N.NDollarParameters;
import $N.NDollarRecognizer;
import $N.PointR;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import marvin.image.MarvinImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.loeschcke.matrix.gui.FrameListener;
import dk.loeschcke.matrix.hardware.Arduino;
import dk.loeschcke.matrix.image.PixelFrame;

import dk.loeschcke.matrix.image.ScaleStrategy;
import dk.loeschcke.matrix.util.Library;

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

//	@Override
//	public PixelFrame getFrame() {
//		PixelFrame pixelFrame = null;
//		if (reader != null) {
//			try {
//				String line = reader.readLine(); // 1-dim
//				pixelFrame = new PixelFrame(line.split(","), Library.FRAME_WIDTH, Library.FRAME_WIDTH);
//			} catch (IOException e) {
//				// With high BAUD rates a zero-byte error may occur.
//			}
//		}
//		return pixelFrame;
//	}
	
	@Override
	public int[] getData() {
		int[] data = new int[Library.FRAME_WIDTH*Library.FRAME_HEIGHT];
		if (reader != null) {
			try {
				String line = reader.readLine(); // 1-dim
				String[] split = line.split(",");
				for (int i = 0; i < data.length; i++) { // split.length may be too large
					try {
						data[i] = Integer.parseInt(split[i]);
					} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
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
