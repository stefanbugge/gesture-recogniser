package dk.loeschcke.matrix.filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractDataFilter {

	private static final int MAX_FRAMES = 5;
	
	private List<int[]> frameHistory = new LinkedList<int[]>();
	
	public void append(int[] frame) {
		frameHistory.add(0, frame);
		if (frameHistory.size() > MAX_FRAMES) {
			frameHistory.remove(frameHistory.size()-1);
		}
	}
	
	public List<int[]> getFrameHistory() {
		return frameHistory;
	}
	
	public List<int[]> lookBack(int frameCount) {
		if (frameHistory.isEmpty()) return new ArrayList<int[]>(); // the empty list
		int toIndex = frameCount;
		if (frameCount > frameHistory.size()) {
			toIndex = frameHistory.size();
		}
		return frameHistory.subList(0, toIndex);
	}
	
}
