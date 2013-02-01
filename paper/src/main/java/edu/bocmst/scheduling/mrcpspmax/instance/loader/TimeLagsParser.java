package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetworkEdge;

abstract class TimeLagsParser extends BaseParser {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TimeLagsParser.class);

	static Map<IAonNetworkEdge, int[][]> parseTimeLags(
			List<String> instanceLines, 
			int activityCount) {
		Map<IAonNetworkEdge, int[][]> timeLagsMap = Maps.newHashMap();
		for(int activityIndex = 0; activityIndex < activityCount; activityIndex++) {
			String vertexLine = instanceLines.get(activityIndex + 1);
			LOGGER.debug("parse edges from line: ", vertexLine);
			int modesCount = getIntegerParsedIndexWord(
					vertexLine, 
					InstanceFileConstants.EdgeLine.MODE_COUNT_INDEX);
			
			String timeLagsString = vertexLine.substring(vertexLine.indexOf("[") + 1, vertexLine.length() - 1);
			ArrayList<String> arrayStrings = Lists.newArrayList(timeLagsString.split("\\]\t\\["));
			
			List<Integer> orderedSuccessorList = getOrderedSuccessorList(vertexLine, modesCount);
		
			for(int succIndex = 0; succIndex < orderedSuccessorList.size(); succIndex++) {
				int successor = orderedSuccessorList.get(succIndex);
				String timeLagArrayString = arrayStrings.get(succIndex);
				int[] timeLagArray = convert(timeLagArrayString);
				int modesSuccessor = timeLagArray.length / modesCount;
				int[][] timeLagMatrix = new int[modesCount][modesSuccessor];
				for(int i = 0; i < modesCount; i ++) {
					for(int j = 0; j < modesSuccessor; j++) {
						timeLagMatrix[i][j] = timeLagArray[i * modesSuccessor + j];
					}
				}
				IAonNetworkEdge edge = new AonNetworkEdge(activityIndex, successor);
				timeLagsMap.put(edge, timeLagMatrix);
			}
		}
		return timeLagsMap;
	}

	private static int[] convert(String timeLagArrayString) {
		StringTokenizer tokenizer = new StringTokenizer(timeLagArrayString, " ");
		int[] array = new int[tokenizer.countTokens()];
		int i = 0;
		while(tokenizer.hasMoreTokens()) {
			array[i] = Integer.parseInt(tokenizer.nextToken());
			i++;
		}
		return array;
	}

	private static List<Integer> getOrderedSuccessorList(String vertexLine,
			int modesCount) {
		LOGGER.debug("parsed mode count: {}", modesCount);
		int successorCount = getIntegerParsedIndexWord(
				vertexLine, 
				InstanceFileConstants.EdgeLine.SUCCESSOR_COUNT_INDEX);
		LOGGER.debug("parsed successor count: {}", successorCount);
		List<Integer> orderedSuccessors = Lists.newArrayList();
		for(int successorIndex = 0; successorIndex < successorCount; successorIndex++) {
			Integer successor = getIntegerParsedIndexWord(
					vertexLine, 
					InstanceFileConstants.EdgeLine.SUCCESSOR_COUNT_INDEX + successorIndex + 1);
			orderedSuccessors.add(successor);
		}
		return orderedSuccessors;
	}
}
