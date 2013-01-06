package com.uc4.scheduling.mrcpspmax.bmap.solution;

import java.util.List;

import com.google.common.primitives.Ints;
import com.uc4.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import com.uc4.scheduling.mrcpspmax.instance.INonRenewableResource;

public class ModeAssignment implements IModeAssignment {

	private final int[] modeArray;
	private final IMrcpspMaxInstance problem;
	private final int[] resourceRemainingVector;
	private final boolean isResourceFeasible;

	public ModeAssignment(int[] modes, IMrcpspMaxInstance problem) {
		this.modeArray = modes;
		this.problem = problem;
		this.resourceRemainingVector = calculateResourceRemainingVector(
				modes,
				problem);
		this.isResourceFeasible = checkResourceFeasibility(resourceRemainingVector);
	}

	public int[] getModeArray() {
		return modeArray;
	}

	public boolean isResourceFeasible() {
		return isResourceFeasible;
	}

	private int[] calculateResourceRemainingVector( // TODO outsource?
			int[] modes,
			IMrcpspMaxInstance problem) {
		List<INonRenewableResource> resources = problem.getNonRenewableResourceList();
		int[] remainingResources = new int[resources.size()];
		for(int resourceIndex = 0; resourceIndex < remainingResources.length; resourceIndex++) {
			INonRenewableResource resource = resources.get(resourceIndex);
			int consumptionsSum = 0;
			for(int activity = 0; activity < modes.length; activity ++) {
				int consumption = problem.getNonRenewableResourceConsumption(
						activity,
						modes[resourceIndex],
						resourceIndex);
				consumptionsSum += consumption;
			}
			int supply = resource.getSupply();
			remainingResources[resourceIndex] = supply - consumptionsSum;
		}
		return remainingResources;
	}

	private boolean checkResourceFeasibility(int[] resourceRemainingVector) { // TODO outsource?
		return Ints.min(resourceRemainingVector) >= 0;
	}
}
