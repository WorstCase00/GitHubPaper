package edu.bocmst.scheduling.mrcpspmax.bmap.candidate;

import java.util.List;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.commons.GraphUtils;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public class RcpspMaxInstance implements IRcpspMaxInstance {

	private final List<Integer> processingTimes;
	private final List<int[]> nonRenewableResourceConsumptions;
	private final List<int[]> renewableResourceConsumptions;
	private final int[][] adjacencyMatrix;
	private final int[][] pathMatrix;

	public RcpspMaxInstance(
			List<Integer> processingTimes,
			List<int[]> nonRenewableResourceConsumptions,
			List<int[]> renewableResourceConsumptions, 
			int[][] adjacencyMatrix,
			int[][] pathMatrix) {
		this.processingTimes = processingTimes;
		this.nonRenewableResourceConsumptions = nonRenewableResourceConsumptions;
		this.renewableResourceConsumptions = renewableResourceConsumptions;
		this.adjacencyMatrix = adjacencyMatrix;
		this.pathMatrix = pathMatrix;
	}
	
	@Override
	public int[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}
	
	@Override
	public int[][] getPathMatrix() {
		return pathMatrix;
	}

	@Override
	public int getProcessingTime(int activity) {
		return processingTimes.get(activity).intValue();
	}

	@Override
	public int[] getNonRenewableResourceConsumption(int acitivity) {
		return nonRenewableResourceConsumptions.get(acitivity);
	}
	
	@Override
	public int[] getRenewableResourceConsumption(int acitivity) {
		return renewableResourceConsumptions.get(acitivity);
	}

	public static IRcpspMaxInstance create(
			int[] modes,
			IMrcpspMaxInstance instance) {
		int[][] adjMatrix = GraphUtils.getAdjacencyMatrix(modes, instance);
		int[][] pathMatrix = GraphUtils.floydWarshallLongestPathWithoutPositiveCycleDetection(adjMatrix);
		List<int[]> nonRenewConsumptions = getNonRenewConsumptions(modes, instance);
		List<int[]> renewConsumptions = getRenewConsumptions(modes, instance);
		List<Integer> processingTimes = getProcessingTimes(modes, instance);
		IRcpspMaxInstance rcpspMaxInstance = new RcpspMaxInstance(
				processingTimes, 
				nonRenewConsumptions, 
				renewConsumptions, 
				adjMatrix, 
				pathMatrix);
		return rcpspMaxInstance;
	}

	private static List<Integer> getProcessingTimes(
			int[] modes,
			IMrcpspMaxInstance instance) {
		List<Integer> procTimes = Lists.newArrayListWithCapacity(modes.length);
		for(int activity = 0; activity < modes.length; activity ++) {
			Integer procTime = instance.getProcessingTime(activity, modes[activity]);
			procTimes.add(procTime);
		}
		return procTimes;
	}

	private static List<int[]> getRenewConsumptions(
			int[] modes,
			IMrcpspMaxInstance instance) {
		List<int[]> consumptions = Lists.newArrayListWithCapacity(modes.length);
		for(int activity = 0; activity < modes.length; activity ++) {
			int[] consumptionVector = instance.getRenewableResourceConsumption(activity, modes[activity]);
			consumptions.add(consumptionVector);
		}
		return consumptions;
	}

	private static List<int[]> getNonRenewConsumptions(
			int[] modes,
			IMrcpspMaxInstance instance) {
		List<int[]> consumptions = Lists.newArrayListWithCapacity(modes.length);
		for(int activity = 0; activity < modes.length; activity ++) {
			int[] consumptionVector = instance.getNonRenewableResourceConsumption(activity, modes[activity]);
			consumptions.add(consumptionVector);
		}
		return consumptions;
	}

}
