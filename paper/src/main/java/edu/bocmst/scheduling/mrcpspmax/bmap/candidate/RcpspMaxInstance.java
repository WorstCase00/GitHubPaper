package edu.bocmst.scheduling.mrcpspmax.bmap.candidate;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.commons.GraphUtils;
import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetwork;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.IRenewableResource;

public class RcpspMaxInstance implements IRcpspMaxInstance {

	private final List<Integer> processingTimes;
	private final List<int[]> renewableResourceConsumptions;
	private final int[][] adjacencyMatrix;
	private final int[][] pathMatrix;
	private final ImmutableList<IRenewableResource> resources;
	private final IAonNetwork aonNetwork;

	public RcpspMaxInstance(
			List<Integer> processingTimes,
			List<int[]> renewableResourceConsumptions, 
			int[][] adjacencyMatrix,
			int[][] pathMatrix, 
			ImmutableList<IRenewableResource> resources, 
			IAonNetwork aonNetwork) {
		this.processingTimes = processingTimes;
		this.renewableResourceConsumptions = renewableResourceConsumptions;
		this.adjacencyMatrix = adjacencyMatrix;
		this.pathMatrix = pathMatrix;
		this.resources = resources;
		this.aonNetwork = aonNetwork;
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
	public int[] getRenewableResourceConsumption(int acitivity) {
		return renewableResourceConsumptions.get(acitivity);
	}

	public static IRcpspMaxInstance create(
			int[] modes,
			IMrcpspMaxInstance instance) {
		int[][] adjMatrix = GraphUtils.getAdjacencyMatrix(modes, instance);
		int[][] pathMatrix = GraphUtils.floydWarshallLongestPathWithoutPositiveCycleDetection(adjMatrix);
		List<int[]> renewConsumptions = getRenewConsumptions(modes, instance);
		List<Integer> processingTimes = getProcessingTimes(modes, instance);
		ImmutableList<IRenewableResource> renewableResources = instance.getRenewableResourceList();
		IAonNetwork aonNetwork = instance.getAonNetwork();
		IRcpspMaxInstance rcpspMaxInstance = new RcpspMaxInstance(
				processingTimes, 
				renewConsumptions, 
				adjMatrix, 
				pathMatrix,
				renewableResources,
				aonNetwork);
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

	@Override
	public ImmutableList<IRenewableResource> getRenewableResourceList() {
		return this.resources;
	}

	@Override
	public int getActivityCount() {
		return this.processingTimes.size();
	}

	@Override
	public IAonNetwork getAonNetwork() {
		return this.aonNetwork;
	}

}
