package dk.loeschcke.matrix.model;

import dk.loeschcke.matrix.helper.MatrixHelper;
import dk.loeschcke.matrix.helper.MatrixHelper.Direction;
import dk.loeschcke.matrix.helper.PointV;
import dk.loeschcke.matrix.image.ApproximationStrategy;
import dk.loeschcke.matrix.image.ApproximationStrategy2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PixelFrame {
	
	private static final Logger log = LoggerFactory.getLogger(PixelFrame.class);
	
	private static final int THRESHOLD_MIN = 40;

	private int[] pixels;
	
	private int width;
	private int height;

    private List<PointV> maxPoints = new ArrayList<PointV>();

	private int xMax;
	private int yMax;
	private int maxValue = 0;
	
	private PointV max = null;
	
	private ApproximationStrategy as = new ApproximationStrategy2();
	
	public PixelFrame(int[] data, int width, int height) {
		pixels = new int[width*height];
		this.width = width;
		this.height = height;
		if (data.length == width * height) {
			process(data);
		} else {
			log.warn("frame skipped: wrong size (" + data.length + ")");
		}
	}
	
	private void process(int[] data) {
		pixels = data;
		int x = 0, y = 0;
		for (int i = 0; i < data.length; i++) {
			if (x == width-1) {
				y++;
			}
			x = i % height;
			try {
				if (pixels[i] > THRESHOLD_MIN) {
                    PointV p = new PointV(x,y,pixels[i]);
                    maxPoints.add(p);
                    if (pixels[i] > maxValue) {
                        maxValue = pixels[i];
                        xMax = x;
                        yMax = y;
                    }
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

	public PointV getMax() {
		return max;
	}

    public List<PointV> getMaxPoints() {
        return maxPoints;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pixels.length; i++) {
            sb.append(pixels[i]);
            if (i < pixels.length-1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
