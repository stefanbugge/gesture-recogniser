package dk.loeschcke.matrix;


import $N.NDollarParameters;
import $N.NDollarRecognizer;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.loeschcke.matrix.gui.FrameListener;
import dk.loeschcke.matrix.gui.MatrixFrame;
import dk.loeschcke.matrix.gui.panels.PostGesturePanel;
import dk.loeschcke.matrix.gui.panels.PostRawPanel;
import dk.loeschcke.matrix.gui.panels.PreRawPanel;
import dk.loeschcke.matrix.gui.panels.PreSmoothedPanel;
import dk.loeschcke.matrix.hardware.actuator.ArduinoActuator;
import dk.loeschcke.matrix.hardware.actuator.Vibrator;
import dk.loeschcke.matrix.hardware.sensor.ArduinoSensor;
import dk.loeschcke.matrix.hardware.sensor.MockSensor;
import dk.loeschcke.matrix.hardware.sensor.Sensor;
import dk.loeschcke.matrix.image.CustomScaleStrategy;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	private static final int PANEL_SIZE = 400;

	static NDollarRecognizer _rec = new NDollarRecognizer();
	
	public static void main(String... args) throws IOException {
		log.info("Starting up...");
		
		final ExecutorService threadPool = Executors.newCachedThreadPool();
		
		log.info("loading sensors...");
		final Sensor arduinoSensor = new ArduinoSensor("COM6");
		final Sensor mockSensor = new MockSensor();
		
		final ArduinoActuator arduinoVibrator = new ArduinoActuator("COM4");
		
		log.info("loading gesture framework ...");
		
		String samplesDir = NDollarParameters.getInstance().SamplesDirectory;

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
		
		//threadPool.execute(sensorArduino);
		final Processor processor = new Processor(mockSensor, arduinoVibrator, _rec);
		threadPool.execute(processor);
		threadPool.execute(arduinoVibrator);
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				
				MatrixFrame frame = new MatrixFrame("Matrix");
				
				FrameListener preRawPanel = new PreRawPanel(PANEL_SIZE, PANEL_SIZE);
				frame.addView((JPanel) preRawPanel);
				processor.addListener(preRawPanel);
				
				FrameListener preSmoothedPanel = new PreSmoothedPanel(PANEL_SIZE, PANEL_SIZE, new CustomScaleStrategy());
				frame.addView((JPanel) preSmoothedPanel);
				processor.addListener(preSmoothedPanel);
				
				FrameListener postRawPanel = new PostRawPanel(PANEL_SIZE, PANEL_SIZE);
				frame.addView((JPanel) postRawPanel);
				processor.addListener(postRawPanel);
				
				FrameListener postGesturePanel = new PostGesturePanel(PANEL_SIZE, PANEL_SIZE);
				frame.addView((JPanel) postGesturePanel);
				processor.addListener(postGesturePanel);
				
				JButton arduinoSensorBtn = new JButton();
				arduinoSensorBtn.setText("Arduino Sensor");
				arduinoSensorBtn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						processor.setSensor(arduinoSensor);
					}
					
				});
				frame.addButton(arduinoSensorBtn);
				
				JButton mockSensorBtn = new JButton();
				mockSensorBtn.setText("Mock Sensor");
				mockSensorBtn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						processor.setSensor(mockSensor);
					}
					
				});
				frame.addButton(mockSensorBtn);
				
				final Sensor sensor = processor.getSensor();
				if (sensor instanceof MockSensor) {
					JButton circleBtn = new JButton();
					circleBtn.setText("circle");
					circleBtn.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							((MockSensor) sensor).update("circle");
						}
						
					});
					frame.addButton(circleBtn);
				}
				processor.start();
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
