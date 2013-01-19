package edu.bocmst.scheduling.mrcpspmax.instance;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.SetUtils;
import org.apache.commons.lang.ArrayUtils;
import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DirectedSubgraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;


public class AonNetworkJGraphTImpl implements IAonNetwork {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AonNetworkJGraphTImpl.class);
	
	private final DirectedGraph<Integer, IAonNetworkEdge> network;
	private final Set<Set<IAonNetworkEdge>> cycleStructures;

	public AonNetworkJGraphTImpl(DirectedGraph<Integer, IAonNetworkEdge> network) {
		this.network = network;
		this.cycleStructures = calculateCycleStructures(network);
	}

	private Set<Set<IAonNetworkEdge>> calculateCycleStructures(
			DirectedGraph<Integer, IAonNetworkEdge> network) {
		Set<Set<IAonNetworkEdge>> structures = Sets.newHashSet();
		StrongConnectivityInspector<Integer, IAonNetworkEdge> inspector = 
			new StrongConnectivityInspector<Integer, IAonNetworkEdge>(network);
		List<DirectedSubgraph<Integer, IAonNetworkEdge>> connectedComponents = inspector.stronglyConnectedSubgraphs();
		for(DirectedSubgraph<Integer, IAonNetworkEdge> connectedCmponent : connectedComponents) {
			addCyclesInConnectedComponent(structures, connectedCmponent);
		}
		return structures;
	}

	private void addCyclesInConnectedComponent(
			Set<Set<IAonNetworkEdge>> structures,
			DirectedSubgraph<Integer, 
			IAonNetworkEdge> connectedCmponent) {
		for(Integer startVertex : connectedCmponent.vertexSet()) {
			KShortestPaths<Integer, IAonNetworkEdge> spp = new KShortestPaths<Integer, IAonNetworkEdge>(
					connectedCmponent, 
					startVertex, 
					Integer.MAX_VALUE, 
					connectedCmponent.vertexSet().size() + 1);
			Set<IAonNetworkEdge> incomingEdges = connectedCmponent.incomingEdgesOf(startVertex);
			for(IAonNetworkEdge incomingEdge : incomingEdges) {
				List<GraphPath<Integer, IAonNetworkEdge>> pathsForCycles = spp.getPaths(incomingEdge.getSource());
				if(pathsForCycles == null) {
					continue;
				}
				for(GraphPath<Integer, IAonNetworkEdge> pathForCycle : pathsForCycles) {
					Set<IAonNetworkEdge> edgeSet = Sets.newHashSet(pathForCycle.getEdgeList());
					edgeSet.add(incomingEdge);
					LOGGER.debug("found path: ", ArrayUtils.toString(edgeSet.toArray()));
					if(structures.contains(edgeSet)) {
						LOGGER.debug("cycle structure already found");
					} else {
						structures.add(edgeSet);
					}
				}
			}
		}
	}

	protected DirectedGraph<Integer, IAonNetworkEdge> getNetwork() {
		return network;
	}
	
	@Override
	public Set<IAonNetworkEdge> getEdges() {
		return network.edgeSet();
	}

	@Override
	public IAonNetworkEdge getEdge(int source, int target) {
		IAonNetworkEdge edge = network.getEdge(source, target);
		return edge;
	}

	@Override
	public Set<Set<IAonNetworkEdge>> getCycleStructures() {
		return this.cycleStructures;
	}
}
