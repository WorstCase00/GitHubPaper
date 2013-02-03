package edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment;

import java.util.Arrays;

class ModeAssignment implements IModeAssignment {

	private final int[] modeArray;
	private final IRcpspMaxInstance instance;
	private final int[] resourceRemainingVector;
	private final boolean resourceFeasible;
	private final boolean timeFeasible;

	protected ModeAssignment(
			int[] modeArray, 
			IRcpspMaxInstance instance,
			int[] resourceRemainingVector, 
			boolean resourceFeasible,
			boolean timeFeasible) {
		this.modeArray = modeArray;
		this.instance = instance;
		this.resourceRemainingVector = resourceRemainingVector;
		this.resourceFeasible = resourceFeasible;
		this.timeFeasible = timeFeasible;
	}

	@Override
	public int[] getResourceRemainingVector() {
		return resourceRemainingVector;
	}

	@Override
	public int[] getModeArray() {
		return modeArray;
	}

	@Override
	public boolean isResourceFeasible() {
		return resourceFeasible;
	}

	@Override
	public boolean isTimeFeasible() {
		return this.timeFeasible;
	}

	@Override
	public double getTimeLagMakespan() {
		return this.instance.getAdjacencyMatrix()[0][modeArray.length-1];
	}

	@Override
	public int getProcessingTime(int activity) {
		return instance.getProcessingTime(activity);
	}

	@Override
	public IRcpspMaxInstance getInstance() {
		return instance;
	}

	@Override
	public String toString() {
		return "ModeAssignment [modeArray=" + Arrays.toString(modeArray)
		+ ", resourceFeasible=" + resourceFeasible + ", timeFeasible="
		+ timeFeasible + "]";
	}
}