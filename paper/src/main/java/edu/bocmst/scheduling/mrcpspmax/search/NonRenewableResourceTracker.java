package edu.bocmst.scheduling.mrcpspmax.search;

import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.primitives.Ints;

import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.utils.IntArrays;

@NotThreadSafe
public class NonRenewableResourceTracker {
	
	private final IMrcpspMaxInstance instance;
	
	// state
	private int[] remainingResources;

	public NonRenewableResourceTracker(
			int[] remainingResources,
			IMrcpspMaxInstance instance) {
		this.instance = instance;
		this.remainingResources = remainingResources;
	}

	public void freeResourcesForActivity(int activity, int mode) {
		int[] consumption = instance.getNonRenewableResourceConsumption(activity, mode);
		remainingResources = IntArrays.plus(remainingResources, consumption);
	}

	public void update(int activity, int mode, int newStart) {
		int[] consumption = instance.getNonRenewableResourceConsumption(activity, mode);
		remainingResources = IntArrays.minus(remainingResources, consumption);
	}

	public boolean isFeasibleActivityWithMode(int activity, int mode) {
		int[] remainingClone = remainingResources.clone();
		int[] consumption = instance.getNonRenewableResourceConsumption(activity, mode);
		remainingClone = IntArrays.minus(remainingClone, consumption);
		int minValue = Ints.min(remainingClone);
		return (minValue >= 0);
	}

}
