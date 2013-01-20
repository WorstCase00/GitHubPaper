package edu.bocmst.scheduling.mrcpspmax.bmap.candidate;

import java.util.Arrays;

import com.google.common.primitives.Ints;

import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxHelper;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public class ModeAssignment implements IModeAssignment {

	private final int[] modeArray;
	private final IRcpspMaxInstance instance;
	private final int[] resourceRemainingVector;
	private final boolean resourceFeasible;
	private final boolean timeFeasible;

	public ModeAssignment(
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
	public int[] getNonRenewableResourceConsumption(int acitivity) {
		return instance.getNonRenewableResourceConsumption(acitivity);
	}

	public static IModeAssignment createInstance(
			int[] modes,
			IMrcpspMaxInstance instance) {
		int[] remaining = MrcpspMaxHelper.calculateResourceRemainingVector(modes, instance);
		boolean resourceFeasible = (Ints.min(remaining) >= 0);
		
		boolean timeFeasible = MrcpspMaxHelper.isModeAssignmentTimeValid(modes, instance);
		
		IRcpspMaxInstance rcpspsMaxInstance = RcpspMaxInstance.create(modes, instance);
		
		return new ModeAssignment(modes, rcpspsMaxInstance, remaining, resourceFeasible, timeFeasible);
	}

	@Override
	public String toString() {
		return "ModeAssignment [modeArray=" + Arrays.toString(modeArray)
				+ ", resourceFeasible=" + resourceFeasible + ", timeFeasible="
				+ timeFeasible + "]";
	}
}
