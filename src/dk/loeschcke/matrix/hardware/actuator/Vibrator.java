package dk.loeschcke.matrix.hardware.actuator;

public interface Vibrator {
	
	static int ACCEPT = 1;

	void vibrate(int type);
}
