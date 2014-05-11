package dk.loeschcke.matrix.model;

import dk.loeschcke.matrix.helper.MatrixHelper;
import dk.loeschcke.matrix.helper.MatrixHelper.Direction;
import dk.loeschcke.matrix.helper.PointV;
import dk.loeschcke.matrix.helper.SpaceHelper;
import dk.loeschcke.matrix.image.ApproximationStrategy;
import dk.loeschcke.matrix.image.ApproximationStrategy2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PixelFrame2 {

	private static final Logger log = LoggerFactory.getLogger(PixelFrame2.class);

	private static final int THRESHOLD_MIN = 30;

	private int[] pixels;

	private int width;
	private int height;

    //private List<PointV> maxPoints = new ArrayList<PointV>();
    private PointV[] maxPoints;
    private static final double MIN_DISTANCE = 3.0;

//	private int xMax;
//	private int yMax;
//	private int maxValue = 0;

	private PointV max = null;

	private ApproximationStrategy as = new ApproximationStrategy2();
    private int inputCount;

    public PixelFrame2(int[] data, int inputCount, int width, int height) {
        //this.inputCount = inputCount;
        this.maxPoints = new PointV[inputCount];
        for (int i = 0; i < maxPoints.length; i++) {
            maxPoints[i] = new PointV(0,0,0);
        }
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
                    PointV point = new PointV(x,y,pixels[i]);
                    for (int j = 0; j < maxPoints.length; j++) {
                        if (point.V > maxPoints[j].V) {
                            double distance = SpaceHelper.distance(point, maxPoints[j]);
                            //log.debug("dist: " + distance);
                            if (distance >= MIN_DISTANCE) {
                                maxPoints[j] = point;
                                //log.debug("new max point: " + point);
                                break; // only occupy a single entry for this max value.
                            } else if (point.V > maxPoints[j].V) {
                                maxPoints[j] = point; // overwrite that position
                                break;
                            }
                        }
                    }
//                    maxPoints.add(point);
//                    if (pixels[i] > maxValue) {
//                        maxValue = pixels[i];
//                        xMax = x;
//                        yMax = y;
//                    }
				}
			} catch (NumberFormatException e) {
				pixels[i] = 0;
			}
		}
		
//		if (maxValue != 0) {
//			//max = interpolateWithNeighbours();
//			PointV up = MatrixHelper.getNeighbour(xMax, yMax, pixels, Direction.UP, width);
//			PointV down = MatrixHelper.getNeighbour(xMax, yMax, pixels, Direction.DOWN, width);
//			PointV left = MatrixHelper.getNeighbour(xMax, yMax, pixels, Direction.LEFT, width);
//			PointV right = MatrixHelper.getNeighbour(xMax, yMax, pixels, Direction.RIGHT, width);
//			max = as.approximate(new PointV(xMax, yMax, maxValue), left, right, up, down);
//        } else {
//			max = null;
//		}
	}
	
	public int[] getPixels() {
		return pixels;
	}

    private PointV approximate(PointV p) {
        PointV approximation = null;
        if (p != null && p.V != 0) {
            PointV up = MatrixHelper.getNeighbour((int) p.X, (int) p.Y, pixels, Direction.UP, width);
            PointV down = MatrixHelper.getNeighbour((int) p.X, (int) p.Y, pixels, Direction.DOWN, width);
            PointV left = MatrixHelper.getNeighbour((int) p.X, (int) p.Y, pixels, Direction.LEFT, width);
            PointV right = MatrixHelper.getNeighbour((int) p.X, (int) p.Y, pixels, Direction.RIGHT, width);
            approximation = as.approximate(new PointV(p.X, p.Y, p.V), left, right, up, down);
        }
        return approximation;
    }

	public PointV getMax() {
        PointV max = maxPoints[0];
        for (int i = 1; i < maxPoints.length; i++) {
            PointV p = maxPoints[i];
            if (max == null) {
                max = p;
            } else {
                if (p != null && p.V > max.V) {
                    max = p;
                }
            }
        }
		return approximate(max);
	}

    public List<PointV> getMaxPoints() {
        List<PointV> maxList = new ArrayList<PointV>();
        for (PointV p : maxPoints) {
            if (p != null) {
                maxList.add(approximate(p));
            }
        }
        return maxList;
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
