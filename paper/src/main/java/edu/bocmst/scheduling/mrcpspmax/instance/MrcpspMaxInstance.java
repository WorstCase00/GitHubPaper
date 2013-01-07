package edu.bocmst.scheduling.mrcpspmax.instance;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jgrapht.DirectedGraph;

import com.google.common.collect.Lists;
import edu.bocmst.scheduling.mrcpspmax.instance.loader.IAonNetwork;

public class MrcpspMaxInstance implements IMrcpspMaxInstance {

	private static final int MODE_INDEX = 0;
	private static final int RESOURCE_INDEX = 1;
	
	private final IAonNetwork aonNetwork;
	private final List<IRenewableResource> renewableResourceList;
	private final List<INonRenewableResource> nonRenewableResourceList;
	private final List<int[]> processingTimesList; //int[mode]
	private final List<int[][]> renewableResourceConsumptionsList; //int[mode][resource]
	private final List<int[][]>  nonRenewableResourceConsumptionsList; //int[mode][resource]
	private final Map<INetworkEdge, int[][]> timelagsMap;
	
	protected MrcpspMaxInstance(
			IAonNetwork aonNetwork,
			List<IRenewableResource> renewableResourceList,
			List<INonRenewableResource> nonRenewableResourceList,
			List<int[]> processingTimesList,
			List<int[][]> renewableResourceConsumptionsList,
			List<int[][]>  nonRenewableResourceConsumptionsList,
			Map<INetworkEdge, int[][]> timelagsMap) {
		super();
		this.aonNetwork = aonNetwork;
		this.renewableResourceList = renewableResourceList;
		this.nonRenewableResourceList = nonRenewableResourceList;
		this.processingTimesList = processingTimesList;
		this.renewableResourceConsumptionsList = renewableResourceConsumptionsList;
		this.nonRenewableResourceConsumptionsList = nonRenewableResourceConsumptionsList;
		this.timelagsMap = timelagsMap;
	}

	@Override
	public IAonNetwork getAonNetwork() {
		return aonNetwork;
	}

	@Override
	public List<IRenewableResource> getRenewableResourceList() {
		return renewableResourceList;
	}

	@Override
	public List<INonRenewableResource> getNonRenewableResourceList() {
		return nonRenewableResourceList;
	}

	public static IMrcpspMaxInstance createInstance( // TODO rework loader method
			IAonNetwork network,
			List<IRenewableResource> renewableResourceList,
			List<INonRenewableResource> nonRenewableResourceList,
			Map<INetworkVertex, List<Integer>> processingTimes,
			Map<INetworkVertex, List<List<Integer>>> renewableresourceConsumptionsMap,
			Map<INetworkVertex, List<List<Integer>>> nonRenewableResourceConsumptionsMap,
			Map<INetworkEdge, List<List<Integer>>> timelagsMap) {
		List<int[]> processingTimesList = convertProcessingTimes(processingTimes);
		List<int[][]> renewableResourceConsumptions = convertConsumptions(renewableresourceConsumptionsMap);
		List<int[][]> nonRenewableResourceConsumptions = convertConsumptions(nonRenewableResourceConsumptionsMap);
		Map<INetworkEdge, int[][]> timeLags = convert(timelagsMap);
		IMrcpspMaxInstance instance = new MrcpspMaxInstance(
				network, 
				renewableResourceList, 
				nonRenewableResourceList, 
				processingTimesList, 
				renewableResourceConsumptions, 
				nonRenewableResourceConsumptions, 
				timeLags);
		return instance;
	}

	private static Map<INetworkEdge, int[][]> convert(
			Map<INetworkEdge, List<List<Integer>>> timelagsMap2) {
		// TODO Auto-generated method stub
		return null;
	}

	private static List<int[][]> convertConsumptions(
			Map<INetworkVertex, List<List<Integer>>> renewableresourceConsumptionsMap) {
		// TODO Auto-generated method stub
		return null;
	}

	private static List<int[]> convertProcessingTimes(
			Map<INetworkVertex, List<Integer>> processingTimesMap) {
		List<int[]> processingTimes = Lists.newArrayList();
		for(Entry<INetworkVertex, List<Integer>> entry : processingTimesMap.entrySet()) {
//			in
		}
		return processingTimes;
	}

	@Override
	public int getNonRenewableResourceConsumption(
			int activity, 
			int mode,
			int resource) {
		int[][] consumptions = nonRenewableResourceConsumptionsList.get(activity);
		int consumption = consumptions[mode][resource];
		return consumption;
	}
	
	@Override
	public int getRenewableResourceConsumption(
			int activity, 
			int mode,
			int resource) {
		int[][] consumptions = renewableResourceConsumptionsList.get(activity);
		int consumption = consumptions[mode][resource];
		return consumption;
	}

	@Override
	public int getTimeLag(
			int source, 
			int sourceMode, 
			int target, 
			int targetMode) {
		INetworkEdge edge = aonNetwork.getEdge(source, target);
		int[][] timeLags = timelagsMap.get(edge);
		int timeLag = timeLags[sourceMode][targetMode];
		return timeLag;
	}

}
