package dk.loeschcke.matrix.image;

import $N.PointR;
import dk.loeschcke.matrix.helper.PointV;

public class ApproximationStrategy2 implements ApproximationStrategy {

	/**
	 * Approximates the center coordinate relative to the weights neighboring coordinates
	 */
	@Override
	public PointR approximate(PointV center, PointV x1, PointV x2, PointV y1, PointV y2) {
		
		PointR result = new PointR();
		
		// X axis
		double pctLeft	= Math.min(1.0, (1.0*x1.V / center.V));
		double pctRight	= Math.min(1.0, (1.0*x2.V / center.V));
//		System.out.println(pctLeft);
//		System.out.println(pctRight);
		double diff = Math.abs(pctLeft-pctRight);
//		System.out.println("pull x: " + (diff * 100) + "%");
		
		double change = Math.min(0.5, diff);
		if (pctLeft > pctRight) {
			change *= -1;
		}
		result.X = center.X + change;
		
		// Y axis
		double pctUp	= Math.min(1.0, (1.0*y1.V / center.V));
		double pctDown	= Math.min(1.0, (1.0*y2.V / center.V));
		diff = Math.abs(pctUp-pctDown);
//		System.out.println("pull y: " + (diff * 100) + "%");
		
		change = Math.min(0.5, diff);
		if (pctUp > pctDown) {
			change *= -1;
		}
		result.Y = center.Y + change;

		return result;
	}

}
