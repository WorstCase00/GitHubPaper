package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.bocmst.scheduling.mrcpspmax.instance.AonNetworkJGraphTImpl;
import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetwork;
import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetworkEdge;

public abstract class AonNetworkParser extends BaseParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(AonNetworkParser.class);
	
	public static IAonNetwork parseProjectNetwork(
			List<String> instanceLines) {
		EdgeFactory<Integer, IAonNetworkEdge> edgeFactory = new AonNetworkEdgeFactory();
		DirectedGraph<Integer, IAonNetworkEdge> network = 
			new DefaultDirectedWeightedGraph<Integer, IAonNetworkEdge>(edgeFactory);

		addVertices(network, instanceLines);
		addEdges(network, instanceLines);
		
		IAonNetwork aonNetwork = AonNetworkJGraphTImpl.createInstance(network);
		return aonNetwork;
	}

	private static void addEdges(
			DirectedGraph<Integer, IAonNetworkEdge> network,
			List<String> instanceLines) {
		
		int vertexCount = network.vertexSet().size();
		for(int activityIndex = 0; activityIndex < vertexCount; activityIndex++) {
			String vertexLine = instanceLines.get(activityIndex + 1);
			LOGGER.debug("parse edges from line: ", vertexLine);
			int modesCount = getIntegerParsedIndexWord(
					vertexLine, 
					InstanceFileConstants.EdgeLine.MODE_COUNT_INDEX);
			LOGGER.debug("parsed mode count: {}", modesCount);
			int successorCount = getIntegerParsedIndexWord(
					vertexLine, 
					InstanceFileConstants.EdgeLine.SUCCESSOR_COUNT_INDEX);
			LOGGER.debug("parsed successor count: {}", successorCount);
			for(int successorIndex = 0; successorIndex < successorCount; successorIndex++) {
				Integer successor = getIntegerParsedIndexWord(
						vertexLine, 
						InstanceFileConstants.EdgeLine.SUCCESSOR_COUNT_INDEX + successorIndex + 1);
				LOGGER.debug("add successor {} for activity {}", successor, activityIndex);
				network.addEdge(activityIndex, successor);
			}
		}
	}

	private static void addVertices(
			DirectedGraph<Integer, IAonNetworkEdge> network,
			List<String> instanceLines) {
		String header = instanceLines.get(0);
		int vertexCount = getIntegerParsedIndexWord(
				header, 
				InstanceFileConstants.Header.ACTIVITY_COUNT_IN_HEADER_INDEX) + 2;
		LOGGER.debug("parsed number of activities in network: ", vertexCount);
		for(int activity = 0; activity < vertexCount; activity++) {
			network.addVertex(activity);
		}
	}

	public static Map<IAonNetworkEdge, int[][]> parseTimeLags(
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
