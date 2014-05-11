package dk.loeschcke.matrix.helper;


public class MatrixHelper {
	
	public static final int OUT_OF_BOUNDS = -1;

	public enum Direction {
		LEFT,
		RIGHT,
		UP,
		DOWN, NONE
	}
	
	/**
	 * 1d to 2d index mapping.
	 */
	public static int getIndex(int x, int y, int width) {
		return width * y + x;
	}
	
	/**
	 * 1d to 2d value mapping. Assuming a square array
	 */
	public static int getValueAt(int x, int y, int[] pixels) {
		int root = (int) Math.sqrt(pixels.length);
		return getValueAt(x, y, pixels, root, root);
	}

	/**
	 * 1d to 2d value mapping.
	 */
	public static int getValueAt(int x, int y, int[] pixels, int width, int height) {
		if (0 <= x && x < width && 0 <= y && y < height) {
			int index = getIndex(x, y, width);
			return pixels[index];
		}
		return OUT_OF_BOUNDS;
	}
	
	public static PointV getNeighbour(int x, int y, int[] pixels, Direction direction, int width) {
		int x2 = x, y2 = y;
		switch (direction) {
		case UP: y2--; break;
		case RIGHT: x2++; break;
		case DOWN: y2++; break;
		case LEFT: x2--; break;
		}
		
		double value = getValueAt(x2, y2, pixels);
		return new PointV(x2, y2, value);
	}

	public static int getNeighbourValue(int x, int y, Direction direction, int[] pixels) {
		int x2 = x, y2 = y;
		switch (direction) {
		case UP: y2--; break;
		case RIGHT: x2++; break;
		case DOWN: y2++; break;
		case LEFT: x2--; break;
		}
		return getValueAt(x2, y2, pixels);
	}
	
	public static int[] resizePixels(int[] pixels,int w1,int h1,int w2,int h2) {
	    int[] temp = new int[w2*h2] ;
	    // EDIT: added +1 to account for an early rounding problem
	    int x_ratio = (int)((w1<<16)/w2) +1;
	    int y_ratio = (int)((h1<<16)/h2) +1;
	    //int x_ratio = (int)((w1<<16)/w2) ;
	    //int y_ratio = (int)((h1<<16)/h2) ;
	    int x2, y2 ;
	    for (int i=0;i<h2;i++) {
	        for (int j=0;j<w2;j++) {
	            x2 = ((j*x_ratio)>>16) ;
	            y2 = ((i*y_ratio)>>16) ;
	            temp[(i*w2)+j] = pixels[(y2*w1)+x2] ;
	        }                
	    }                
	    return temp ;
	}
	
}
