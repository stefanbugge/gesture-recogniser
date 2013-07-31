package dk.loeschcke.matrix.image;

import dk.loeschcke.matrix.helper.PointV;
import gesturefun.PointR;

public interface ApproximationStrategy {
	
	PointR approximate(PointV center, PointV x1, PointV x2, PointV y1, PointV y2);
}
