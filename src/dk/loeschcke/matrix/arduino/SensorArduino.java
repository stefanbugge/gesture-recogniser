package dk.loeschcke.matrix.arduino;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import marvin.image.MarvinImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.loeschcke.matrix.image.PointR;
import dk.loeschcke.matrix.image.ScaleStrategy;

/**
 * SensorArduino reads distances from the Arduino with the Ping sensor, and puts
 * these values in a datatype.
 * 
 * @author markus
 * 
 */
public class SensorArduino extends Arduino implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(SensorArduino.class);

	private static final int INPUT_WIDTH = 7;
	private static final int INPUT_HEIGHT = 7;
	
	private final int SCALE = 100;
	
	private ScaleStrategy scaleStrategy;

	public SensorArduino(String portname, ScaleStrategy scaleStrategy) {
		super(portname);
		this.scaleStrategy = scaleStrategy;
	}

	@Override
	public void run() {
		connect();
		
		BufferedImage image;
		MarvinImage marvinImage;
		
		final int SCALED_WIDTH = INPUT_WIDTH*SCALE;
		final int SCALED_HEIGHT = INPUT_HEIGHT*SCALE;
		
		
		ArrayList<PointR> points = new ArrayList<PointR>();
		
		try {
			
			int xMaxPrev = -1;
			int yMaxPrev = -1;
			
			long startTime = System.currentTimeMillis();
			final long LIMIT = 2000;
			
			int[] pixels = new int[INPUT_WIDTH*INPUT_HEIGHT];
			while (!Thread.interrupted()) {

				int xMax = -1;
				int yMax = -1;
				int maxValue = 0;
				
				
				
				int x = 0;
				int y = 0;
				
				int MIN = 50;

				String line = reader.readLine(); // 1-dim
				String[] split = line.split(",");
				if (split.length == INPUT_WIDTH * INPUT_HEIGHT) {
					// 1 image frame
					for (int i = 0; i < split.length; i++) {
						
						if (x == INPUT_WIDTH-1) {
							y++;
						}
						x = i % INPUT_WIDTH;
						
						
						try {
							pixels[i] = Integer.parseInt(split[i]);
							if (pixels[i] > MIN && pixels[i] > maxValue) {
								maxValue = pixels[i];
								xMax = x;
								yMax = y;
							}
							
						} catch (NumberFormatException e) {
							pixels[i] = 0;
						}
					}
					
				}
				
				PointR pMax = new PointR(xMax, yMax);
				PointR pMaxPrev = new PointR(xMaxPrev, yMaxPrev);
				
				if ((xMax != xMaxPrev || yMax != yMaxPrev) && xMax != -1 && yMax != -1) {
					points.add(new PointR(xMax, yMax));
					startTime = System.currentTimeMillis();
					System.out.println(points.get(points.size()-1));
				} else {
					if (!points.isEmpty()) {
						long time = System.currentTimeMillis();
						if (time - startTime > LIMIT) {
							System.out.println("do recognition test");
							startTime = System.currentTimeMillis();
							points = new ArrayList<PointR>();
						}
					}
				}
				
				// previous value stays the same
				// and value < MIN
				
				
				xMaxPrev = xMax;
				yMaxPrev = yMax;
				
				image = scaleStrategy.scale(pixels, INPUT_WIDTH, INPUT_HEIGHT, SCALED_WIDTH, SCALED_HEIGHT);
				marvinImage = new MarvinImage(image);
				
				this.setChanged();
				this.notifyObservers(marvinImage);

			}
		} catch (IOException e) {
			log.error("Lost connection", e);
		}

		log.debug("Interrupted, exiting.");
		disconnect();
	}

	
}
