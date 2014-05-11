package dk.loeschcke.matrix.image;

import dk.loeschcke.matrix.helper.PointV;
import dollarrecognizer.P.Point;

public interface ApproximationStrategy {
	
	PointV approximate(PointV center, PointV x1, PointV x2, PointV y1, PointV y2);
}
