package dk.loeschcke.matrix.image;

import gesturefun.PointR;
import dk.loeschcke.matrix.helper.PointV;

public class ApproximationStrategy1 implements ApproximationStrategy {

	/**
	 * Approximates the center coordinate relative to the weights neighboring coordinates
	 */
	@Override
	public PointR approximate(PointV center, PointV x1, PointV x2, PointV y1, PointV y2) {
		
		PointR result = new PointR();
		// X axis
		double pct = ((1.0 * Math.max(x1.V, x2.V)) / Math.min(x1.V, x2.V)) -1;
		System.out.println("x: " + pct + "% ");
		double change = 0.5 * pct;
		if (Math.max(x1.V, x2.V) == x1.V) {
			result.X = center.X - change;
		} else {
			result.X = center.X + change;
		}
		// Y axis
		pct = ((1.0 * Math.max(y1.V, y2.V)) / Math.min(y1.V, y2.V)) -1;
		//System.out.println("y: " + pct + "% ");
		change = 0.5 * pct;
		if (Math.max(y1.V, y2.V) == y1.V) {
			result.Y = center.Y - change;
		} else {
			result.Y = center.Y + change;
		}
		return result;
	}

}
