package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import edu.bocmst.graph.IDirectedEdge;
import edu.bocmst.graph.IDirectedGraph;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.INonRenewableResource;
import edu.bocmst.scheduling.mrcpspmax.instance.IRenewableResource;


class MrcpspMaxInstance implements IMrcpspMaxInstance {

	private final int instanceId;
	private final IDirectedGraph aonNetwork;
	private final ImmutableList<IRenewableResource> renewableResourceList;
	private final ImmutableList<INonRenewableResource> nonRenewableResourceList;
	private final ImmutableList<int[]> processingTimesList; //int[mode]
	private final ImmutableList<int[][]> renewableResourceConsumptionsList; //int[mode][resource]
	private final ImmutableList<int[][]>  nonRenewableResourceConsumptionsList; //int[mode][resource]
	private final ImmutableMap<IDirectedEdge, int[][]> timelagsMap;
	
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
	public String toString() {
		return "MrcpspMaxInstance [" +
				"aonNetwork=" + aonNetwork
				+ ", renewableResourceList=" + renewableResourceList
				+ ", nonRenewableResourceList=" + nonRenewableResourceList
				+ ", processingTimesList=" + processingTimesList
				+ ", renewableResourceConsumptionsList=" + renewableResourceConsumptionsList
				+ ", nonRenewableResourceConsumptionsList=" + nonRenewableResourceConsumptionsList 
				+ ", timelagsMap="+ timelagsMap + "]";
	}
	
	
}
