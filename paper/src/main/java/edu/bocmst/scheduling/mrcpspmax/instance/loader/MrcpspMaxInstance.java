package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import edu.bocmst.graph.IDirectedEdge;
import edu.bocmst.graph.IDirectedGraph;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.INonRenewableResource;
import edu.bocmst.scheduling.mrcpspmax.instance.IRenewableResource;
import edu.bocmst.utils.IntArrays;


class MrcpspMaxInstance implements IMrcpspMaxInstance {

	private final int instanceId;
	private final IDirectedGraph aonNetwork;
	private final ImmutableList<IRenewableResource> renewableResourceList;
	private final ImmutableList<INonRenewableResource> nonRenewableResourceList;
	private final ImmutableList<int[]> processingTimesList; //int[mode]
	private final ImmutableList<int[][]> renewableResourceConsumptionsList; //int[mode][resource]
	private final ImmutableList<int[][]>  nonRenewableResourceConsumptionsList; //int[mode][resource]
	private final ImmutableMap<IDirectedEdge, int[][]> timelagsMap;
	private final int[] modeCounts;
	
	MrcpspMaxInstance(
			int instanceId,
			IDirectedGraph aonNetwork,
			List<IRenewableResource> renewableResourceList,
			List<INonRenewableResource> nonRenewableResourceList,
			List<int[]> processingTimesList,
			List<int[][]> renewableResourceConsumptionsList,
			List<int[][]>  nonRenewableResourceConsumptionsList,
			Map<IDirectedEdge, int[][]> timelagsMap) {
		super();
		this.instanceId = instanceId;
		this.aonNetwork = aonNetwork;
		this.renewableResourceList = ImmutableList.copyOf(renewableResourceList);
		this.nonRenewableResourceList = ImmutableList.copyOf(nonRenewableResourceList);
		this.processingTimesList = ImmutableList.copyOf(processingTimesList);
		this.renewableResourceConsumptionsList = ImmutableList.copyOf(renewableResourceConsumptionsList);
		this.nonRenewableResourceConsumptionsList = ImmutableList.copyOf(nonRenewableResourceConsumptionsList);
		this.timelagsMap = ImmutableMap.copyOf(timelagsMap);
		this.modeCounts = new int[processingTimesList.size()];
		for (int activity = 0; activity < modeCounts.length; activity++) {
			modeCounts[activity] = processingTimesList.get(activity).length;
		}
	}

	@Override
	public int getInstanceId() {
		return this.instanceId;
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
		IDirectedEdge edge = aonNetwork.getEdge(source, target);
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

	@Override
	public Set<IDirectedEdge> getAonNetworkEdges() {
		return aonNetwork.getEdges();
	}

	@Override
	public Set<Set<IDirectedEdge>> getCycleStructures() {
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
	public IDirectedGraph getAonNetwork() {
		return this.aonNetwork;
	}
	
	@Override
	public int[] getModeCounts() {
		return this.modeCounts;
	}
	
	@Override
	public String toString() {
		return "MrcpspMaxInstance [" +
				"aonNetwork=" + aonNetwork
				+ ", renewableResourceList=" + Arrays.toString(renewableResourceList.toArray())
				+ ", nonRenewableResourceList=" + Arrays.toString(nonRenewableResourceList.toArray())
				+ ", processingTimesList=" + Arrays.toString(processingTimesList.toArray())
				+ ", renewableResourceConsumptionsList=" + Arrays.toString(renewableResourceConsumptionsList.toArray())
				+ ", nonRenewableResourceConsumptionsList=" + Arrays.toString(nonRenewableResourceConsumptionsList.toArray()) 
				+ ", timelagsMap="+ createTimeLagsMapString() + "]";
	}

	private String createTimeLagsMapString() {
		StringBuilder string = new StringBuilder();
		for(Entry<IDirectedEdge, int[][]> entry : timelagsMap.entrySet()) {
			string.append(entry.getKey()).append("=").append(IntArrays.toOneLineString(entry.getValue())).append("\n");
		}
		return string.toString();
	}

	
}
