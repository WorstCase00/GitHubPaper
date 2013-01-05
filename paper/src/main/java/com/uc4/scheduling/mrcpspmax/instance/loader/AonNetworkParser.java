package com.uc4.scheduling.mrcpspmax.instance.loader;

import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.uc4.scheduling.mrcpspmax.instance.INetworkEdge;
import com.uc4.scheduling.mrcpspmax.instance.INetworkVertex;
import com.uc4.scheduling.mrcpspmax.instance.NetworkVertex;

public abstract class AonNetworkParser extends BaseParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(AonNetworkParser.class);
	
	public static IAonNetwork parseProjectNetwork(
			List<String> instanceLines) {
		EdgeFactory<INetworkVertex, INetworkEdge> edgeFactory = new AonNetworkEdgeFactory();
		DirectedGraph<INetworkVertex, INetworkEdge> network = 
			new DefaultDirectedWeightedGraph<INetworkVertex, INetworkEdge>(edgeFactory);

		addVertices(network, instanceLines);
		addEdges(network, instanceLines);
		
		IAonNetwork aonNetwork = new AonNetworkJGraphTImpl(network);
		return aonNetwork;
	}

	private static void addEdges(
			DirectedGraph<INetworkVertex, INetworkEdge> network,
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
				network.addEdge(NetworkVertex.createInstance(activityIndex), NetworkVertex.createInstance(successor));
			}
		}
	}

	private static void addVertices(
			DirectedGraph<INetworkVertex, INetworkEdge> network,
			List<String> instanceLines) {
		String header = instanceLines.get(0);
		int vertexCount = getIntegerParsedIndexWord(
				header, 
				InstanceFileConstants.Header.ACTIVITY_COUNT_IN_HEADER_INDEX) + 2;
		LOGGER.debug("parsed number of activities in network: ", vertexCount);
		for(int i = 0; i < vertexCount; i++) {
			INetworkVertex vertex = new NetworkVertex(i);
			network.addVertex(vertex);
		}
	}
}
