package edu.bocmst.scheduling.mrcpspmax.scheduler;

public class StartTimeWindow {

	private final int lowerBound;
	private final Integer upperBound;

	protected StartTimeWindow(int lowerBound, Integer upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public int getLowerBound() {
		return this.lowerBound;
	}

	public Integer getUpperBound() {
		return this.upperBound;
	}

	@Override
	public String toString() {
		return "StartTimeWindow [" + lowerBound + ", "
				+ upperBound + "]";
	}
}
