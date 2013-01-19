package edu.bocmst.scheduling.mrcpspmax.commons;

import java.util.List;

import com.google.common.primitives.Ints;

import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.INonRenewableResource;

public abstract class MrcpspMaxHelper {

	private MrcpspMaxHelper() {}
	
	public static boolean isModeAssignmentResourceValid(
			int[] modes, 
			IMrcpspMaxInstance instance) {
		int[] remainingResourcesVector = calculateResourceRemainingVector(modes, instance);
		if(Ints.min(remainingResourcesVector) >= 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isModeAssignmentTimeValid(
			int[] modes, 
			IMrcpspMaxInstance instance) {
		int[][] adjacencyMatrix = GraphUtils.getAdjacencyMatrix(modes, instance);
		int[][] floydMatrix = GraphUtils.floydWarshallLongestPathWithoutPositiveCycleDetection(adjacencyMatrix);
		for(int i = 0; i < floydMatrix.length; i++) {
			if(floydMatrix[i][i] > 0) {
				return false;
			}
		}
		return true;
	}
	
	public static int[] calculateResourceRemainingVector(
			int[] modes,
			IMrcpspMaxInstance instance) {
		List<INonRenewableResource> resources = instance.getNonRenewableResourceList();
		int[] remainingResources = new int[resources.size()];
		for(int resourceIndex = 0; resourceIndex < remainingResources.length; resourceIndex++) {
			INonRenewableResource resource = resources.get(resourceIndex);
			int consumptionsSum = 0;
			for(int activity = 0; activity < modes.length; activity ++) {
				int consumption = instance.getNonRenewableResourceConsumption(
						activity,
						modes[activity],
						resourceIndex);
				consumptionsSum += consumption;
			}
			int supply = resource.getSupply();
			remainingResources[resourceIndex] = supply - consumptionsSum;
		}
		return remainingResources;
	}

	public static int[] getNonRenewableConsumptionVector(
			int activity, 
			int mode,
			IMrcpspMaxInstance instance) {
		int[] actualConsumption = new int[instance.getNonRenewableResourceCount()];
		for(int resource = 0; resource < actualConsumption.length; resource ++) {
			int consumption = instance.getNonRenewableResourceConsumption(activity, mode, resource);
			actualConsumption[resource] = consumption;
		}
		return actualConsumption;
	}
}
