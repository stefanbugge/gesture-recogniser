package filter;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dk.loeschcke.matrix.filter.AbstractDataFilter;

public class FrameHistoryTest {
	
	@Before
	public void setUp() {
		
	}

	@Test
	public void testHistory() {
		AbstractDataFilter df = new AbstractDataFilter() { };
		
		List<int[]> frameHistory = df.getFrameHistory();
		assertEquals(0, frameHistory.size());
		
		int[] f3 = {3}; 
		
		df.append(new int[]{1});
		df.append(new int[]{2});
		df.append(f3);
		df.append(new int[]{4});
		
		assertEquals(4, frameHistory.size());
		
		df.append(new int[]{5});
		df.append(new int[]{6});
		df.append(new int[]{7});
		
		assertEquals(5, frameHistory.size());
		
		int[] oldest = frameHistory.get(frameHistory.size() -1);
		assertEquals(f3, oldest);
		
		List<int[]> lookBack = df.lookBack(3);
		assertTrue(lookBack.get(0)[0] == 7);
		assertTrue(lookBack.get(1)[0] == 6);
		assertTrue(lookBack.get(2)[0] == 5);
	}

}
