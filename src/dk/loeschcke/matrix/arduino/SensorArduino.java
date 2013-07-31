package dk.loeschcke.matrix.arduino;

import gesturefun.NBestList;
import gesturefun.NDollarParameters;
import gesturefun.NDollarRecognizer;
import gesturefun.PointR;

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

import dk.loeschcke.matrix.image.PixelFrame;

import dk.loeschcke.matrix.image.ScaleStrategy;
import dk.loeschcke.matrix.util.FrameListener;
import dk.loeschcke.matrix.util.Library;

/**
 * SensorArduino reads distances from the Arduino with the Ping sensor, and puts
 * these values in a datatype.
 * 
 * @author markus
 * 
 */
public class SensorArduino extends Arduino implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(SensorArduino.class);
	
	static NDollarRecognizer _rec = null;

	private PixelFrame pixelFrame = null;
	private List<FrameListener> listeners = new ArrayList<FrameListener>();

	public SensorArduino(String portname) {
		super(portname);
		
		String samplesDir = NDollarParameters.getInstance().SamplesDirectory;

		_rec = new NDollarRecognizer();

		// create the set of filenames to read in
		File currentDir = new File(samplesDir);
		File[] allXMLFiles = currentDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".xml");
			}
		});

		// read them
		for (int i = 0; i < allXMLFiles.length; ++i) {
			_rec.LoadGesture(allXMLFiles[i]);
		}
	}
	
	@Override
	public void run() {
		connect();
		
		Vector<PointR> points = new Vector<PointR>();
		
		int i = 0;
		long start = System.currentTimeMillis();
		try {
			
			PointR pMaxPrev = new PointR(-1,-1);
			PointR pMax = null;
			
			long startTime = System.currentTimeMillis();
			final long LIMIT = 1200;
			
			while (!Thread.interrupted()) {

				String line = reader.readLine(); // 1-dim
				
				String[] split = line.split(",");
//				if (i % 100 == 0) {
//					long now = System.currentTimeMillis();
//					System.out.println((now - start) + " ms");
//					start = now;
//				}
//				i++;
				
				if (split.length == Library.FRAME_WIDTH * Library.FRAME_HEIGHT) {
					
					pixelFrame = new PixelFrame(split, Library.FRAME_WIDTH, Library.FRAME_HEIGHT);
					
					pMax = pixelFrame.getMax();
					
					if (pointHasChanged(pMax, pMaxPrev)) {
						if (pMax != null) {
							points.add(pMax);
							startTime = System.currentTimeMillis();
							log.debug(points.get(points.size()-1).toString());
						}
					} else {
						if (!points.isEmpty()) {
							long time = System.currentTimeMillis();
							if (time - startTime > LIMIT) {
								
								if (points.size() >= 8) { // we need at least 8 points for a gesture
									log.debug("do recognition test");
									
									NBestList result = _rec.Recognize(points, 1);
									
									String resultTxt;
									if (result.getScore() == -1) {
										log.debug("no fucking gesture!!");
									} else {
										log.debug("result: " + result.getName());
									}
									resetViews();
								}
								System.out.println("reset");
								startTime = System.currentTimeMillis();
								points = new Vector<PointR>();
							}
						}
					}
					
				}
				
				pMaxPrev = pMax;
				
				updateViews();

			}
		} catch (IOException e) {
			log.error("Lost connection", e);
		}

		log.debug("Interrupted, exiting.");
		disconnect();
	}

	private boolean pointHasChanged(double now, double before) {
		int a = (int) (now * 10.0 + 0.5);
		int b = (int) (before * 10.0 + 0.5);
		//System.out.println(now + " : " + a);
		return a != b;
	}
	
	private boolean pointHasChanged(PointR now, PointR before) {
		if (now == null || before == null) {
			if (now == null && before == null) return false;
			return true;
		}
		return pointHasChanged(now.X, before.X) || pointHasChanged(now.Y, before.Y);
	}
	
	public void addListener(FrameListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void updateViews() {
		if (pixelFrame != null) {
			for (FrameListener frame : listeners) {
				frame.update(pixelFrame.getPixels(), pixelFrame.getMax());
			}
		}
	}
	
	public void resetViews() {
		for (FrameListener frame : listeners) {
			frame.reset();
		}
	}
}
