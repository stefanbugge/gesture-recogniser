package dk.loeschcke.matrix.image;

import gesturefun.PointR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.loeschcke.matrix.helper.MatrixHelper;
import dk.loeschcke.matrix.helper.MatrixHelper.Direction;
import dk.loeschcke.matrix.helper.PointV;

public class PixelFrame {
	
	private static final Logger log = LoggerFactory.getLogger(PixelFrame.class);
	
	private static final int THRESHOLD_MIN = 50;

	private int[] pixels;
	
	private int width;
	private int height;

	private int xMax;
	private int yMax;
	private int iMax;
	private int maxValue = 0;
	
	private PointR max = null;
	
	private ApproximationStrategy as = new ApproximationStrategy2();
	
	public PixelFrame(String[] pixelsStr, int width, int height) {
		
		this.width = width;
		this.height = height;
		
		process(pixelsStr);
	}
	
	private void process(String[] pixelsStr) {
		pixels = new int[width*height];
		int x = 0, y = 0;
		
		for (int i = 0; i < pixelsStr.length; i++) {
			
			if (x == width-1) {
				y++;
			}
			x = i % height;
			
			try {
				pixels[i] = Integer.parseInt(pixelsStr[i]);
				if (pixels[i] > THRESHOLD_MIN && pixels[i] > maxValue) {
					maxValue = pixels[i];
					xMax = x;
					yMax = y;
					iMax = i;
				}
				
			} catch (NumberFormatException e) {
				pixels[i] = 0;
			}
		}
		
		if (maxValue != 0) {
			//max = interpolateWithNeighbours();
			PointV up = MatrixHelper.getNeighbour(xMax, yMax, pixels, Direction.UP, width);
			PointV down = MatrixHelper.getNeighbour(xMax, yMax, pixels, Direction.DOWN, width);
			PointV left = MatrixHelper.getNeighbour(xMax, yMax, pixels, Direction.LEFT, width);
			PointV right = MatrixHelper.getNeighbour(xMax, yMax, pixels, Direction.RIGHT, width);
			max = as.approximate(new PointV(xMax, yMax, maxValue), left, right, up, down);
		} else {
			max = null;
		}
	}
	
	public int[] getPixels() {
		return pixels;
	}

	public PointR getMax() {
		return max;
	}
}
