package dk.loeschcke.matrix.hardware.sensor;

public interface Sensor {

//	PixelFrame getFrame();
	
	int[] getData();
	
	void connect();
	
	void disconnect();
}
