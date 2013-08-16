package dk.loeschcke.matrix.hardware.sensor;

import dk.loeschcke.matrix.image.PixelFrame;

public interface Sensor {

//	PixelFrame getFrame();
	
	int[] getData();
	
	void connect();
	
	void disconnect();
}
