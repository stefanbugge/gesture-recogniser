package dk.loeschcke.matrix.gui;

import $N.PointR;

public interface FrameListener {

	void update(int[] pixels, PointR max);
	
	void reset();
}
