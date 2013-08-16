package dk.loeschcke.matrix;

import $P.PDollarRecognizer;
import $P.Point;
import $P.RecognizerResults;
import $N.NBestList;
import $N.NDollarParameters;
import $N.NDollarRecognizer;
import $N.PointR;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import dk.loeschcke.matrix.filter.AveragingDataFilter;
import dk.loeschcke.matrix.filter.DataFilter;
import dk.loeschcke.matrix.gui.FrameListener;
import dk.loeschcke.matrix.hardware.actuator.Vibrator;
import dk.loeschcke.matrix.hardware.sensor.Sensor;
import dk.loeschcke.matrix.image.PixelFrame;
import dk.loeschcke.matrix.util.Library;

public class Processor implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(Processor.class);

	private Sensor sensor;
	private Vibrator vibrator;
	
	private PixelFrame pixelFrame = null;
	
	private List<FrameListener> listeners = Collections.synchronizedList(new ArrayList<FrameListener>());

	private boolean started = false;
	
	static NDollarRecognizer _rec;
	static PDollarRecognizer _Prec = new PDollarRecognizer();
	
	private static List<DataFilter> dataFilters = new ArrayList<DataFilter>();
	static {
		dataFilters.add(new AveragingDataFilter());
	}

	public Processor(Sensor sensor, Vibrator vibrator, NDollarRecognizer rec) {
		this.vibrator = vibrator;
		_rec = rec;
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
		
		Vector<PointR> points = new Vector<PointR>();
		
		ArrayList<Point> pointCloud = new ArrayList<Point>();
		
		PointR pMaxPrev = new PointR(-1,-1);
		PointR pMax = null;
		
		long startTime = System.currentTimeMillis();
		final long LIMIT = 1200;
		
		long timeout = 800;
		long prevTimestamp = System.currentTimeMillis();
		int strokeID = -1;
		
		while (!Thread.interrupted()) {

			int[] data = sensor.getData();
			for (DataFilter filter : dataFilters) {
				data = filter.filter(data);
			}
			pixelFrame = new PixelFrame(data, Library.FRAME_WIDTH, Library.FRAME_HEIGHT);
			
			if (pixelFrame != null) {
				pMax = pixelFrame.getMax();
				
				if (pointHasChanged(pMax, pMaxPrev)) {
					if (pMax != null) {
						long timestamp = System.currentTimeMillis();
						if (timestamp - prevTimestamp < timeout) {
							log.debug("same stroke: " + strokeID);
						} else {
							strokeID++;
							prevTimestamp = timestamp;
							log.debug("new stroke: " + strokeID);
						}
						//points.add(pMax);
						pointCloud.add(new Point(pMax.X, pMax.Y, strokeID));
						startTime = System.currentTimeMillis();
						//log.debug(points.get(points.size()-1).toString());
						//vibrator.vibrate(Vibrator.ACCEPT);
					}
				} else {
					if (!pointCloud.isEmpty()) {
					//if (!points.isEmpty()) {
						long time = System.currentTimeMillis();
						if (time - startTime > LIMIT) {
							
							if (pointCloud.size() >= 8) {
							//if (points.size() >= 8) { // we need at least 8 points for a gesture
								log.debug("do recognition test");
								strokeID = -1;
								//NBestList result = _rec.Recognize(points, 1);
								
								RecognizerResults recognize = _Prec.Recognize(pointCloud);
								log.info(recognize.mName + ": " + recognize.mScore + " (other info: " + recognize.mOtherInfo + ")");
//								if (result.getScore() == -1) {
//									log.debug("no fucking gesture!!");
//								} else {
//									log.debug("result: " + result.getName());
//								}
								resetViews();
							}
							System.out.println("reset");
							startTime = System.currentTimeMillis();
							points = new Vector<PointR>();
							pointCloud = new ArrayList<Point>();
						}
					}
				}
			
				pMaxPrev = pMax;
				
				updateViews();
			}
		}
	}
	
	/*
	 * Has the point changed within 1 decimal place.
	 */
	private boolean pointHasChanged(double now, double before) {
		int a = (int) (now * 10.0 + 0.5);
		int b = (int) (before * 10.0 + 0.5);
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

	public void start() {
		started = true;
	}
	
}
