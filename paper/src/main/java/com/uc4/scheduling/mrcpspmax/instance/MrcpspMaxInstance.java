package com.uc4.scheduling.mrcpspmax.instance;

import java.util.List;
import java.util.Map;

import org.jgrapht.DirectedGraph;

import com.uc4.scheduling.mrcpspmax.instance.loader.IAonNetwork;

public class MrcpspMaxInstance implements IMrcpspMaxInstance {

	private final IAonNetwork aonNetwork;
	private final List<IRenewableResource> renewableResourceList;
	private final List<INonRenewableResource> nonRenewableResourceList;
	private final Map<INetworkVertex, List<Integer>> processingTimes;
	private final Map<INetworkVertex, List<List<Integer>>> renewableResourceConsumptionsMap;
	private final Map<INetworkVertex, List<List<Integer>>> nonRenewableResourceConsumptionsMap;
	private final Map<INetworkEdge, List<List<Integer>>> timelagsMap;
	
	public MrcpspMaxInstance(
			IAonNetwork network,
			List<IRenewableResource> renewableResourceList,
			List<INonRenewableResource> nonRenewableResourceList,
			Map<INetworkVertex, List<Integer>> processingTimes,
			Map<INetworkVertex, List<List<Integer>>> renewableResourceConsumptionsMap,
			Map<INetworkVertex, List<List<Integer>>> nonRenewableResourceConsumptionsMap,
			Map<INetworkEdge, List<List<Integer>>> timelagsMap) {
		super();
		this.aonNetwork = network;
		this.renewableResourceList = renewableResourceList;
		this.nonRenewableResourceList = nonRenewableResourceList;
		this.processingTimes = processingTimes;
		this.renewableResourceConsumptionsMap = renewableResourceConsumptionsMap;
		this.nonRenewableResourceConsumptionsMap = nonRenewableResourceConsumptionsMap;
		this.timelagsMap = timelagsMap;
	}

	public IAonNetwork getAonNetwork() {
		return aonNetwork;
	}

	public List<IRenewableResource> getRenewableResourceList() {
		return renewableResourceList;
	}

	public List<INonRenewableResource> getNonRenewableResourceList() {
		return nonRenewableResourceList;
	}
}
