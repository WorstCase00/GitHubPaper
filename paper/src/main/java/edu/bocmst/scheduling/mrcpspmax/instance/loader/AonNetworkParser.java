package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetwork;
import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetworkEdge;

abstract class AonNetworkParser extends BaseParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(AonNetworkParser.class);
	
	static IAonNetwork parseProjectNetwork(
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
}
