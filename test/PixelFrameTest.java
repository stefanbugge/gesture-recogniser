import static org.junit.Assert.*;
import gesturefun.PointR;
import junit.framework.Assert;

import org.junit.Test;

import dk.loeschcke.matrix.helper.MatrixHelper;
import dk.loeschcke.matrix.helper.MatrixHelper.Direction;
import dk.loeschcke.matrix.helper.PointV;
import dk.loeschcke.matrix.image.ApproximationStrategy;
import dk.loeschcke.matrix.image.ApproximationStrategy2;
import dk.loeschcke.matrix.image.ApproximationStrategy1;


public class PixelFrameTest {

	final int WIDTH = 7;
	final int HEIGHT = 7;
	// 7x7 matrix
	final int[] pixels = new int[] {
			0,	1,	2,	3,	4,	5,	6,
			7,	8,	9,	10,	11,	12,	13,
			14,	15,	16,	17,	18,	19,	20,	
			21,	22,	23,	24,	25,	26,	27,
			28,	29,	30,	31,	32,	33,	34,
			35,	36,	37,	38,	39,	40,	41,
			42,	43,	44,	45,	46,	47,	48
	};
	
	@Test
	public void test1dTo2d() {
		// corners
		assertEquals("value at (0,0) should be 0.", 0, MatrixHelper.getValueAt(0, 0, pixels));
		assertEquals("value at (6,0) should be 6.", 6, MatrixHelper.getValueAt(6, 0, pixels));
		assertEquals("value at (6,6) should be 48.", 48, MatrixHelper.getValueAt(6, 6, pixels));
		assertEquals("value at (0,6) should be 42.", 42, MatrixHelper.getValueAt(0, 6, pixels));
		// random
		assertEquals("value at (2,2) should be 16.", 16, MatrixHelper.getValueAt(2, 2, pixels));
		assertEquals("value at (4,5) should be 39.", 39, MatrixHelper.getValueAt(4, 5, pixels));
	}
	
	@Test
	public void testNeighbourIndeces() {
//		int x = 1, y = 1;
//		int index = MatrixHelper.getNeighbourIndex(x, y, Direction.UP, WIDTH);
//		assertEquals("", 1, index);
//		index = MatrixHelper.getNeighbourIndex(x, y, Direction.RIGHT, WIDTH);
//		assertEquals("", 9, index);
//		index = MatrixHelper.getNeighbourIndex(x, y, Direction.DOWN, WIDTH);
//		assertEquals("", 15, index);
//		index = MatrixHelper.getNeighbourIndex(x, y, Direction.LEFT, WIDTH);
//		assertEquals("", 7, index);
	}
	
	@Test
	public void testNeighbours() {
		int x = 1, y = 1;
		
		int value = MatrixHelper.getNeighbourValue(x, y, Direction.UP, pixels);
		assertEquals("up of (1,1) should be 1.", 	1, value);
		value = MatrixHelper.getNeighbourValue(x, y, Direction.RIGHT, pixels);
		assertEquals("right of (1,1) should be 9.", 	9, value);
		value = MatrixHelper.getNeighbourValue(x, y, Direction.DOWN, pixels);
		assertEquals("down of (1,1) should be 15.", 	15, value);
		value = MatrixHelper.getNeighbourValue(x, y, Direction.LEFT, pixels);
		assertEquals("left of (1,1) should be 7.", 	7, value);
		
		//corners
		value = MatrixHelper.getNeighbourValue(0, 0, Direction.UP, pixels);
		assertEquals("up of (0,0).", 	MatrixHelper.OUT_OF_BOUNDS, value);
		value = MatrixHelper.getNeighbourValue(6, 0, Direction.RIGHT, pixels);
		assertEquals("right of (6,0).", 	MatrixHelper.OUT_OF_BOUNDS, value);
		value = MatrixHelper.getNeighbourValue(6, 6, Direction.DOWN, pixels);
		assertEquals("down of (6,6).", 	MatrixHelper.OUT_OF_BOUNDS, value);
		value = MatrixHelper.getNeighbourValue(0, 6, Direction.LEFT, pixels);
		assertEquals("left of (0,6).", 	MatrixHelper.OUT_OF_BOUNDS, value);
		
	}

	@Test
	public void testPointApproximation() {
		ApproximationStrategy as = new ApproximationStrategy1();
		
		/**
		 * 		-	6	-
		 * 		7	10	8
		 * 		-	4	-
		 */

		PointV center 	= new PointV(1,1, 10);
		
		PointV x1 		= new PointV(0,1,  7);
		PointV x2 		= new PointV(2,1,  8);
		
		PointV y1 		= new PointV(1,0,  6);
		PointV y2 		= new PointV(1,2,  4);
		
		PointR approximation = as.approximate(center, x1, x2, y1, y2);
		System.out.println(approximation);
		assertTrue("x should be in the range of +-0.5 from the center x", approximation.X >= center.X-0.5 && center.X+0.5 >= approximation.X);
		assertTrue("y should be in the range of +-0.5 from the center y", approximation.Y >= center.Y-0.5 && center.Y+0.5 >= approximation.Y);
		
		assertEquals("", 1.07, approximation.X, 0.01);
		assertEquals("", 0.75, approximation.Y, 0.01);
		
//		/**
//		 * 		-	2	-
//		 * 		2	10	10
//		 * 		-	2	-
//		 */
//		
//		center 	= new PointV(1,1, 10);
//		x1 		= new PointV(0,1,  2);
//		x2 		= new PointV(2,1, 10);
//		
//		y1 		= new PointV(1,0,  2);
//		y2 		= new PointV(1,2,  2);
//		
//		approximation = as.approximate(center, x1, x2, y1, y2);
//		System.out.println(approximation);
//		assertTrue("x should be in the range of +-0.5 from the center x", approximation.X >= center.X-0.5 && center.X+0.5 >= approximation.X);
//		assertTrue("y should be in the range of +-0.5 from the center y", approximation.Y >= center.Y-0.5 && center.Y+0.5 >= approximation.Y);
//		
//		assertEquals("", 1.07, approximation.X, 0.01);
//		assertEquals("", 0.75, approximation.Y, 0.01);
		
	}
	
	@Test
	public void testPointApproximation2() {
		ApproximationStrategy as = new ApproximationStrategy2();
		
		/**
		 * 		-	6	-
		 * 		7	10	8
		 * 		-	4	-
		 */

		PointV center 	= new PointV(1,1, 10);
		
		PointV x1 		= new PointV(0,1,  7);
		PointV x2 		= new PointV(2,1,  8);
		
		PointV y1 		= new PointV(1,0,  6);
		PointV y2 		= new PointV(1,2,  4);
		
		PointR approximation = as.approximate(center, x1, x2, y1, y2);
		//System.out.println(approximation);
		assertTrue("x should be in the range of +-0.5 from the center x", approximation.X >= center.X-0.5 && center.X+0.5 >= approximation.X);
		assertTrue("y should be in the range of +-0.5 from the center y", approximation.Y >= center.Y-0.5 && center.Y+0.5 >= approximation.Y);
		
		/**
		 * 		-	2	-
		 * 		2	10	10
		 * 		-	2	-
		 */
		
		center 	= new PointV(1,1, 10);
		x1 		= new PointV(0,1,  2);
		x2 		= new PointV(2,1, 10);
		
		y1 		= new PointV(1,0,  2);
		y2 		= new PointV(1,2,  2);
		
		approximation = as.approximate(center, x1, x2, y1, y2);
		//System.out.println(approximation);
		assertTrue("x should be in the range of +-0.5 from the center x", approximation.X >= center.X-0.5 && center.X+0.5 >= approximation.X);
		assertTrue("y should be in the range of +-0.5 from the center y", approximation.Y >= center.Y-0.5 && center.Y+0.5 >= approximation.Y);
		
		/**
		 * 		-	2	-
		 * 		10	10	10
		 * 		-	2	-
		 */
		
		center 	= new PointV(1,1, 10);
		x1 		= new PointV(0,1, 10);
		x2 		= new PointV(2,1, 10);
		
		y1 		= new PointV(1,0,  2);
		y2 		= new PointV(1,2,  2);
		
		approximation = as.approximate(center, x1, x2, y1, y2);
		//System.out.println(approximation);
		assertTrue("x should be in the range of +-0.5 from the center x", approximation.X >= center.X-0.5 && center.X+0.5 >= approximation.X);
		assertTrue("y should be in the range of +-0.5 from the center y", approximation.Y >= center.Y-0.5 && center.Y+0.5 >= approximation.Y);
	}
}
