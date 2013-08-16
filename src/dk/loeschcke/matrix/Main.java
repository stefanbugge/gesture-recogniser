package dk.loeschcke.matrix;


import gesturefun.PointR;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.loeschcke.matrix.arduino.SensorArduino;
import dk.loeschcke.matrix.image.CustomScaleStrategy;
import dk.loeschcke.matrix.util.FrameListener;
import dk.loeschcke.matrix.util.PostRawPanel;
import dk.loeschcke.matrix.util.PreRawPanel;
import dk.loeschcke.matrix.util.PreSmoothedPanel;
import dk.loeschcke.matrix.utilu.PostGesturePanel;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	private static final int PANEL_SIZE = 400;
	
	public Main(String name) {
		super(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(2, 2, 10, 10));
		setVisible(true);
		setSize(900, 900);
	}
	
	public static void main(String... args) throws IOException {
		log.info("Starting up...");
		
		final ExecutorService threadPool = Executors.newCachedThreadPool();
		
		final SensorArduino sensorArduino = new SensorArduino("/dev/tty.usbserial-A600aid6");
		
		threadPool.execute(sensorArduino);
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				
				Main main = new Main("Matrix");
				
				FrameListener preRawPanel = new PreRawPanel(PANEL_SIZE, PANEL_SIZE);
				FrameListener preSmoothedPanel = new PreSmoothedPanel(PANEL_SIZE, PANEL_SIZE, new CustomScaleStrategy());
				FrameListener postRawPanel = new PostRawPanel(PANEL_SIZE, PANEL_SIZE);
				FrameListener postGesturePanel = new PostGesturePanel(PANEL_SIZE, PANEL_SIZE);
				
				main.add((JPanel) preRawPanel);
				main.add((JPanel) preSmoothedPanel);
				main.add((JPanel) postRawPanel);
				main.add((JPanel) postGesturePanel);
				
				sensorArduino.addListener(preRawPanel);
				sensorArduino.addListener(preSmoothedPanel);
				sensorArduino.addListener(postRawPanel);
				sensorArduino.addListener(postGesturePanel);
			}
			
		});
		
		
		// Wait for newline, then interrupt threads and exit
		log.info("Press enter to exit program.");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();

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
