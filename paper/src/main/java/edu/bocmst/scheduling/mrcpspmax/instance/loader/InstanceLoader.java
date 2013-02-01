package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetwork;
import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetworkEdge;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.INonRenewableResource;
import edu.bocmst.scheduling.mrcpspmax.instance.IRenewableResource;

public abstract class InstanceLoader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InstanceLoader.class);
	
	private InstanceLoader() {}
	
	public static IMrcpspMaxInstance loadInstance(String path) throws IOException {
		LOGGER.debug("load MrcpspMaxInstance from file: {}", path);
		File instanceFile = new File(path);
		List<String> instanceLines = FileUtils.readLines(instanceFile);
		ResourceParser.verifyNoMixedResources(instanceLines.get(0));
		List<IRenewableResource> renewableResourceList = ResourceParser.parseRenewableResourcesList(instanceLines);
		List<INonRenewableResource> nonRenewableResourceList = ResourceParser.parseNonRenewableResourceList(instanceLines);Map<Integer, List<Integer>> processingTimes = ActivityParser.parseProcessingTimes(instanceLines);
		Map<Integer, List<List<Integer>>> nonRenewableResourceConsumptionsMap = ActivityParser.parseNonRenewableResourceConsumptions(instanceLines);
		Map<Integer, List<List<Integer>>> renewableresourceConsumptionsMap = ActivityParser.parseRenewableResourceConsumptions(instanceLines);
		
		IAonNetwork network = AonNetworkParser.parseProjectNetwork(instanceLines);
		Map<IAonNetworkEdge, int[][]> timelagsMap = TimeLagsParser.parseTimeLags(instanceLines, processingTimes.size());
		List<int[]> procTimeArrays = convertProcessingTimes(processingTimes);
		List<int[][]> renewableConsumptionsMatrices = convertConsumptions(renewableresourceConsumptionsMap);
		List<int[][]> nonRenewableConsumptionMatrices = convertConsumptions(nonRenewableResourceConsumptionsMap);
		IMrcpspMaxInstance instance = new MrcpspMaxInstance(
				network, 
				renewableResourceList, 
				nonRenewableResourceList, 
				procTimeArrays, 
				renewableConsumptionsMatrices, 
				nonRenewableConsumptionMatrices, 
				timelagsMap);
		LOGGER.debug("create MrcpspMaxInstance: {}", instance);
		return instance;
	}

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
}
