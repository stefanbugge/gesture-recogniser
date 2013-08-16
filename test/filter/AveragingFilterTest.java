package filter;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import dk.loeschcke.matrix.filter.AveragingDataFilter;
import dk.loeschcke.matrix.filter.DataFilter;

public class AveragingFilterTest {
	
	private DataFilter df;

	@Before
	public void setUp() throws Exception {
		df = new AveragingDataFilter();
	}

	@Test
	public void test() {

		int[] data = {
			0, 2, 5, 120	
		};
		
		int[] average = df.filter(data);
		assertTrue(Arrays.equals(data, average));
		
		data = new int[] {
			0, 0, 0, 0	
		};
		
		int[] expected = {
			0, 1, 2, 60	
		};
		
		average = df.filter(data);
		assertTrue(Arrays.equals(expected, average));
		
		data = new int[] {
			3, 3, 2, 0	
		};
			
		expected = new int[] {
			1, 2, 2, 30
		};
		
		average = df.filter(data);
		assertTrue(Arrays.equals(expected, average));
		
	}

}
