package dk.loeschcke.matrix;


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
import dk.loeschcke.matrix.gui.ImagePanel;
import dk.loeschcke.matrix.gui.MatrixFrame;
import dk.loeschcke.matrix.image.CustomScaleStrategy;

public class Main extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	private MarvinImage image;
	private MarvinImagePanel panel;
	
	public Main(String name) {
		super(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));
		panel = new MarvinImagePanel();
		add(panel);
		setVisible(true);
		setSize(600,600);
	}
	
	public static void main(String... args) throws IOException {
		log.info("Starting up...");
		
		final ExecutorService threadPool = Executors.newCachedThreadPool();
		
		final SensorArduino sensorArduino = new SensorArduino("COM6", new CustomScaleStrategy());
		
		threadPool.execute(sensorArduino);
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				
				Main main = new Main("Matrix");
				
				sensorArduino.addObserver(main);
				
				
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

	@Override
	public void update(Observable o, Object arg) {
		image = (MarvinImage) arg;
		image.update();
		panel.setImage(image);
	}
}
