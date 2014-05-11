package dk.loeschcke.matrix.filter;

import java.util.List;

public class AveragingDataFilter extends AbstractDataFilter implements DataFilter {

	private static final int LOOK_BACK = 1;
	
	@Override
	public int[] filter(int[] data) {
		
		List<int[]> frameHistory = lookBack(LOOK_BACK);
		
		int[] averageData = new int[data.length];
		for (int i = 0; i < averageData.length; i++) {
			int sum = data[i]; // current data value;
			for (int[] arr : frameHistory) {
				sum += arr[i];
			}
			averageData[i] = sum / (frameHistory.size() + 1);
			sum = 0;
		}
		append(data);
		return averageData;
	}
}
