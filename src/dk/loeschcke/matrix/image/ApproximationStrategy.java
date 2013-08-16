package dk.loeschcke.matrix.image;

import $N.PointR;
import dk.loeschcke.matrix.helper.PointV;

public interface ApproximationStrategy {
	
	PointR approximate(PointV center, PointV x1, PointV x2, PointV y1, PointV y2);
}
