package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


abstract class ActivityParser extends BaseParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityParser.class);
	
	private ActivityParser() {}
	
	static Map<Integer, List<Integer>> parseProcessingTimes(
			List<String> instanceLines) {
		Map<Integer, List<Integer>> processingTimesMap = Maps.newHashMap();
		List<String> activityBlockLines = getActivityBlockLines(instanceLines);
		List<List<Integer>> processingTimesLists =  parseModeWise(
				activityBlockLines,
				InstanceFileConstants.ActivityBlock.PROCESSING_TIME_INDEX);
		for(int vertex = 0; vertex < processingTimesLists.size(); vertex++) {
			List<Integer> processingTimes = processingTimesLists.get(vertex);
			LOGGER.debug("processing times parsed for vertex {}: {}", vertex, processingTimes);
			processingTimesMap.put(vertex, processingTimes);
		}
		return processingTimesMap;
	}

	private static List<List<Integer>> parseModeWise(
			List<String> lines, 
			int valueIndex) {
		List<List<Integer>> valueLists = Lists.newArrayList();
		for(String line : lines) {
			if(!line.startsWith(InstanceFileConstants.DELIMITER)) {
				List<Integer> newList = Lists.newArrayList();
				valueLists.add(newList);
			}
			List<Integer> actualValues = valueLists.get(valueLists.size() - 1);
			Integer value = getIntegerParsedIndexWord(line, valueIndex);
			actualValues.add(value);
		}
		return valueLists;
	}

	private static List<String> getActivityBlockLines(List<String> instanceLines) {
		int activites = getActivitesCount(instanceLines);
		List<String> lines = instanceLines.subList(
				activites + 1, 
				instanceLines.size() - 1);
		return lines;
	}

	private static Map<Integer, List<List<Integer>>> createConsumptionsMap(
			List<List<List<Integer>>> consumptionsForAllResources,
			int renewableResourcesCount, 
			List<String> instanceLines) {
		int activityCount = getActivitesCount(instanceLines);
		Map<Integer, List<List<Integer>>> consumptionsMap = Maps.newHashMap();
		for(int activityIndex = 0; activityIndex < activityCount; activityIndex++) {
			List<List<Integer>> resourceConsumptions = Lists.newArrayList();
			for(int resourceIndex = 0; resourceIndex < renewableResourcesCount; resourceIndex ++) {
				List<Integer> consumption = consumptionsForAllResources.get(resourceIndex).get(activityIndex);
				resourceConsumptions.add(consumption);
			}
			consumptionsMap.put(activityIndex, resourceConsumptions);
			
		}
		return consumptionsMap;
	}

	static Map<Integer, List<List<Integer>>> parseRenewableResourceConsumptions(List<String> instanceLines) {
		int renewableResourcesCount = getRenewableResourcesCount(instanceLines);
		List<List<List<Integer>>> consumptionsForAllResources = Lists.newArrayList();
		for(int resourceIndex = 0; resourceIndex < renewableResourcesCount; resourceIndex ++) {
			List<String> activityBlockLines = getActivityBlockLines(instanceLines);
			List<List<Integer>> consumptionsForResource =  parseModeWise(
					activityBlockLines,
					InstanceFileConstants.ActivityBlock.RENEWABLE_RESOURCE_START_INDEX + resourceIndex);
			consumptionsForAllResources.add(consumptionsForResource);
		}
		
		Map<Integer, List<List<Integer>>> consumptionsMap = createConsumptionsMap(
				consumptionsForAllResources,
				renewableResourcesCount,
				instanceLines);
		return consumptionsMap;
	}

	static Map<Integer, List<List<Integer>>> parseNonRenewableResourceConsumptions(List<String> instanceLines) {
		int nonRenewableResourcesCount = getNonRenewableResourcesCount(instanceLines);
		int renewableResourcesCount = getRenewableResourcesCount(instanceLines); // as offset
		List<List<List<Integer>>> consumptionsForAllResources = Lists.newArrayList();
		for(int resourceIndex = 0; resourceIndex < nonRenewableResourcesCount; resourceIndex ++) {
			List<String> activityBlockLines = getActivityBlockLines(instanceLines);
			List<List<Integer>> consumptionsForResource =  parseModeWise(
					activityBlockLines,
					InstanceFileConstants.ActivityBlock.RENEWABLE_RESOURCE_START_INDEX + renewableResourcesCount + resourceIndex);
			consumptionsForAllResources.add(consumptionsForResource);
		}
		
		Map<Integer, List<List<Integer>>> consumptionsMap = createConsumptionsMap(
				consumptionsForAllResources,
				nonRenewableResourcesCount,
				instanceLines);
		return consumptionsMap;
	}
}
