package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetwork;
import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetworkEdge;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.INonRenewableResource;
import edu.bocmst.scheduling.mrcpspmax.instance.IRenewableResource;
import edu.bocmst.scheduling.mrcpspmax.instance.MrcpspMaxInstance;

public abstract class InstanceLoader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InstanceLoader.class);
	
	private InstanceLoader() {}
	
	public static IMrcpspMaxInstance loadInstance(String path) throws IOException {
		File instanceFile = new File(path);
		List<String> instanceLines = FileUtils.readLines(instanceFile);
		ResourceParser.verifyNoMixedResources(instanceLines.get(0));
		List<IRenewableResource> renewableResourceList = ResourceParser.parseRenewableResourcesList(instanceLines);
		List<INonRenewableResource> nonRenewableResourceList = ResourceParser.parseNonRenewableResourceList(instanceLines);Map<Integer, List<Integer>> processingTimes = ActivityParser.parseProcessingTimes(instanceLines);
		Map<Integer, List<List<Integer>>> nonRenewableResourceConsumptionsMap = ActivityParser.parseNonRenewableResourceConsumptions(instanceLines);
		Map<Integer, List<List<Integer>>> renewableresourceConsumptionsMap = ActivityParser.parseRenewableResourceConsumptions(instanceLines);
		
		IAonNetwork network = AonNetworkParser.parseProjectNetwork(instanceLines);
		Map<IAonNetworkEdge, int[][]> timelagsMap = AonNetworkParser.parseTimeLags(instanceLines, processingTimes.size());
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
//		IMrcpspMaxInstance instance = loadInstance("instances/100/psp1.sch");
		IMrcpspMaxInstance instance = loadInstance("src/test/resources/thesisExample.sch");
		LOGGER.info("loaded instance: {}", instance);
		System.out.println(instance.getTimeLag(5, 3, 7, 1));
	}

}
