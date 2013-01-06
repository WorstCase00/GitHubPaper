package com.uc4.scheduling.mrcpspmax.instance.loader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jgrapht.DirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.uc4.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import com.uc4.scheduling.mrcpspmax.instance.INetworkEdge;
import com.uc4.scheduling.mrcpspmax.instance.INetworkVertex;
import com.uc4.scheduling.mrcpspmax.instance.INonRenewableResource;
import com.uc4.scheduling.mrcpspmax.instance.IRenewableResource;
import com.uc4.scheduling.mrcpspmax.instance.MrcpspMaxInstance;

public abstract class InstanceLoader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InstanceLoader.class);
	
	private InstanceLoader() {}
	
	public static IMrcpspMaxInstance loadInstance(String path) throws IOException {
		File instanceFile = new File(path);
		List<String> instanceLines = FileUtils.readLines(instanceFile);
		ResourceParser.verifyNoMixedResources(instanceLines.get(0));
		List<IRenewableResource> renewableResourceList = ResourceParser.parseRenewableResourcesList(instanceLines);
		List<INonRenewableResource> nonRenewableResourceList = ResourceParser.parseNonRenewableResourceList(instanceLines);
		IAonNetwork network = AonNetworkParser.parseProjectNetwork(instanceLines);
		Map<INetworkVertex, List<Integer>> processingTimes = ActivityParser.parseProcessingTimes(instanceLines);
		Map<INetworkVertex, List<List<Integer>>> renewableresourceConsumptionsMap = ActivityParser.parseRenewableResourceConsumptions(instanceLines);
		Map<INetworkVertex, List<List<Integer>>> nonRenewableResourceConsumptionsMap = ActivityParser.parseNonRenewableResourceConsumptions(instanceLines);
		Map<INetworkEdge, List<List<Integer>>> timelagsMap = Maps.newHashMap();
		IMrcpspMaxInstance instance = MrcpspMaxInstance.createInstance(
				network, 
				renewableResourceList,
				nonRenewableResourceList, 
				processingTimes,
				renewableresourceConsumptionsMap,
				nonRenewableResourceConsumptionsMap,
				timelagsMap);
		return instance;
	}
	
	public static void main(String[] args) throws IOException {
		IMrcpspMaxInstance instance = loadInstance("instances/100/psp1.sch");
		LOGGER.info("loaded instance: {}", instance);
	}

}
