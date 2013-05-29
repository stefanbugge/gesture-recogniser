package dk.loeschcke.matrix;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.loeschcke.matrix.arduino.SensorArduino;
import dk.loeschcke.matrix.gui.ImagePanel;
import dk.loeschcke.matrix.gui.MatrixFrame;

public class Main  {

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String... args) throws IOException {
		log.info("Starting up...");
		
		final ExecutorService threadPool = Executors.newCachedThreadPool();
		
		final SensorArduino sensorArduino = new SensorArduino("COM6");
		
		threadPool.execute(sensorArduino);
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// Create the GUI
				ImagePanel imagePanel = new ImagePanel();
				
				final JFrame frame = new MatrixFrame("Matrix", imagePanel);
				
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));

				
				frame.add(imagePanel);
				sensorArduino.addObserver(imagePanel);
				sensorArduino.addObserver((MatrixFrame) frame);
				
				frame.pack();
				frame.setVisible(true);
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
