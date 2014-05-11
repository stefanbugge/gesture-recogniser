package dk.loeschcke.matrix.image;

import dk.loeschcke.matrix.helper.PointV;
import dollarrecognizer.P.Point;

public class ApproximationStrategy1 implements ApproximationStrategy {

	/**
	 * Approximates the center coordinate relative to the weights neighboring coordinates
	 */
	@Override
	public PointV approximate(PointV center, PointV x1, PointV x2, PointV y1, PointV y2) {
		
		// X axis
		double pct = ((1.0 * Math.max(x1.V, x2.V)) / Math.min(x1.V, x2.V)) -1;
		System.out.println("x: " + pct + "% ");
		double change = 0.5 * pct;
        double x;
		if (Math.max(x1.V, x2.V) == x1.V) {
			x = center.X - change;
		} else {
			x = center.X + change;
		}
		// Y axis
		pct = ((1.0 * Math.max(y1.V, y2.V)) / Math.min(y1.V, y2.V)) -1;
		//System.out.println("y: " + pct + "% ");
		change = 0.5 * pct;
        double y;
		if (Math.max(y1.V, y2.V) == y1.V) {
			y = center.Y - change;
		} else {
			y = center.Y + change;
		}
        double v = center.V;
		return new PointV(x,y,v);
	}

}
