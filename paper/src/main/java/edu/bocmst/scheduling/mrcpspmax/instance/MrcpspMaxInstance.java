package edu.bocmst.scheduling.mrcpspmax.instance;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;


public class MrcpspMaxInstance implements IMrcpspMaxInstance {

	private final IAonNetwork aonNetwork;
	private final ImmutableList<IRenewableResource> renewableResourceList;
	private final ImmutableList<INonRenewableResource> nonRenewableResourceList;
	private final ImmutableList<int[]> processingTimesList; //int[mode]
	private final ImmutableList<int[][]> renewableResourceConsumptionsList; //int[mode][resource]
	private final ImmutableList<int[][]>  nonRenewableResourceConsumptionsList; //int[mode][resource]
	private final ImmutableMap<IAonNetworkEdge, int[][]> timelagsMap;
	
	protected MrcpspMaxInstance(
			IAonNetwork aonNetwork,
			List<IRenewableResource> renewableResourceList,
			List<INonRenewableResource> nonRenewableResourceList,
			List<int[]> processingTimesList,
			List<int[][]> renewableResourceConsumptionsList,
			List<int[][]>  nonRenewableResourceConsumptionsList,
			Map<IAonNetworkEdge, int[][]> timelagsMap) {
		super();
		this.aonNetwork = aonNetwork;
		this.renewableResourceList = ImmutableList.copyOf(renewableResourceList);
		this.nonRenewableResourceList = ImmutableList.copyOf(nonRenewableResourceList);
		this.processingTimesList = ImmutableList.copyOf(processingTimesList);
		this.renewableResourceConsumptionsList = ImmutableList.copyOf(renewableResourceConsumptionsList);
		this.nonRenewableResourceConsumptionsList = ImmutableList.copyOf(nonRenewableResourceConsumptionsList);
		this.timelagsMap = ImmutableMap.copyOf(timelagsMap);
	}

	@Override
	public ImmutableList<IRenewableResource> getRenewableResourceList() {
		return renewableResourceList;
	}

	@Override
	public ImmutableList<INonRenewableResource> getNonRenewableResourceList() {
		return nonRenewableResourceList;
	}

	@Override
	public int getNonRenewableResourceConsumption(
			int activity, 
			int mode,
			int resource) {
		int[][] consumptions = nonRenewableResourceConsumptionsList.get(activity);
		int consumption = consumptions[mode-1][resource];
		return consumption;
	}

	@Override
	public int getRenewableResourceConsumption(
			int activity, 
			int mode,
			int resource) {
		int[][] consumptions = renewableResourceConsumptionsList.get(activity);
		int consumption = consumptions[mode-1][resource];
		return consumption;
	}

	@Override
	public int getTimeLag(
			int source, 
			int sourceMode, 
			int target, 
			int targetMode) {
		IAonNetworkEdge edge = aonNetwork.getEdge(source, target);
		int[][] timeLags = timelagsMap.get(edge);
		int timeLag = timeLags[sourceMode-1][targetMode-1];
		return timeLag;
	}

	@Override
	public int getModeCount(int activity) {
		return processingTimesList.get(activity).length;
	}

	@Override
	public int getNonRenewableResourceCount() {
		return nonRenewableResourceList.size();
	}

	@Override
	public int getRenewableResourceCount() {
		return renewableResourceList.size();
	}

	@Override
	public int getActivityCount() {
		return processingTimesList.size();
	}

	@Override
	public int getProcessingTime(int activity, int mode) {
		return processingTimesList.get(activity)[mode - 1];
	}

	public static IMrcpspMaxInstance createInstance( // TODO rework loader method
			IAonNetwork network,
			List<IRenewableResource> renewableResourceList,
			List<INonRenewableResource> nonRenewableResourceList,
			Map<Integer, List<Integer>> processingTimes,
			Map<Integer, List<List<Integer>>> renewableresourceConsumptionsMap,
			Map<Integer, List<List<Integer>>> nonRenewableResourceConsumptionsMap,
			Map<IAonNetworkEdge, int[][]> timelagsMap) {
		List<int[]> processingTimesList = convertProcessingTimes(processingTimes);
		List<int[][]> renewableResourceConsumptions = convertConsumptions(renewableresourceConsumptionsMap);
		List<int[][]> nonRenewableResourceConsumptions = convertConsumptions(nonRenewableResourceConsumptionsMap);
		IMrcpspMaxInstance instance = new MrcpspMaxInstance(
				network, 
				renewableResourceList, 
				nonRenewableResourceList, 
				processingTimesList, 
				renewableResourceConsumptions, 
				nonRenewableResourceConsumptions, 
				timelagsMap);
		return instance;
	}

	// TODO TBR
	private static List<int[][]> convertConsumptions(
			Map<Integer, List<List<Integer>>> consumptionsMap) {
		int activityCount = consumptionsMap.size();
		List<int[][]> consumptionMatrices  = Lists.newArrayListWithCapacity(activityCount);
		for(int i = 0; i < activityCount; i++) {
			consumptionMatrices.add(null);
		}
		for(Entry<Integer, List<List<Integer>>> entry : consumptionsMap.entrySet()) {
			int activity = entry.getKey();
			consumptionMatrices.remove(activity);
			int[][] consumptionMatrix = convertLists(entry.getValue());
			consumptionMatrices.add(activity, consumptionMatrix);
		}
		return consumptionMatrices;
	}

	// TODO TBR
	private static int[][] convertLists(List<List<Integer>> value) {
		int resourceCount = value.size();
		int modeCount = value.get(0).size();
		int[][] matrix = new int[modeCount][resourceCount];
		for(int resourceIndex = 0; resourceIndex < resourceCount; resourceIndex ++) {
			for(int modeIndex = 0; modeIndex < modeCount; modeIndex ++) {
				matrix[modeIndex][resourceIndex] = value.get(resourceIndex).get(modeIndex);
			}
		}
		return matrix;
	}

	// TODO TBR
	private static List<int[]> convertProcessingTimes(
			Map<Integer, List<Integer>> processingTimesMap) {
		int activityCount = processingTimesMap.size();
		List<int[]> processingTimes = Lists.newArrayListWithCapacity(activityCount);
		for(int i = 0; i < activityCount; i++) {
			processingTimes.add(null);
		}
		
		for(Entry<Integer, List<Integer>> entry : processingTimesMap.entrySet()) {
			int activity = entry.getKey();
			List<Integer> timesList = entry.getValue();
			processingTimes.remove(activity);
			int[] timesArray = ArrayUtils.toPrimitive(timesList.toArray(new Integer[0]));
			processingTimes.add(activity, timesArray);
		}
		return processingTimes;
	}

	@Override
	public Set<IAonNetworkEdge> getAonNetworkEdges() {
		return aonNetwork.getEdges();
	}

	@Override
	public Set<Set<IAonNetworkEdge>> getCycleStructures() {
		return aonNetwork.getCycleStructures();
	}

	@Override
	public int[] getNonRenewableResourceConsumption(int activity, int mode) {
		return nonRenewableResourceConsumptionsList.get(activity)[mode-1];
	}

	@Override
	public int[] getRenewableResourceConsumption(int activity, int mode) {
		return renewableResourceConsumptionsList.get(activity)[mode-1];
	}

	@Override
	public IAonNetwork getAonNetwork() {
		return this.aonNetwork;
	}
}
