package dk.loeschcke.matrix.util;

import gesturefun.PointR;

public interface FrameListener {

	void update(int[] pixels, PointR max);
	
	void reset();
}
