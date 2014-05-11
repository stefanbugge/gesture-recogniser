package dk.loeschcke.matrix.image;

import dk.loeschcke.matrix.helper.PointV;
import dollarrecognizer.P.Point;

public class ApproximationStrategy2 implements ApproximationStrategy {

	/**
	 * Approximates the center coordinate relative to the weights neighboring coordinates
	 */
	@Override
	public PointV approximate(PointV center, PointV x1, PointV x2, PointV y1, PointV y2) {
		
		// X axis
		double pctLeft	= Math.min(1.0, (1.0*x1.V / center.V));
		double pctRight	= Math.min(1.0, (1.0*x2.V / center.V));
		double diff = Math.abs(pctLeft-pctRight);

        double vx = diff + 1;
		
		double change = Math.min(0.5, diff);
		if (pctLeft > pctRight) {
			change *= -1;
		}
		double x = center.X + change;
		
		// Y axis
		double pctUp	= Math.min(1.0, (1.0*y1.V / center.V));
		double pctDown	= Math.min(1.0, (1.0*y2.V / center.V));
		diff = Math.abs(pctUp-pctDown);

        double vy = diff + 1;

		change = Math.min(0.5, diff);
		if (pctUp > pctDown) {
			change *= -1;
		}
		double y = center.Y + change;

        double v = center.V * ((vx*vx + vy*vy) /2);
        //System.out.println(result.V + " : " + center.V);
		return new PointV(x,y,v);
	}

}
