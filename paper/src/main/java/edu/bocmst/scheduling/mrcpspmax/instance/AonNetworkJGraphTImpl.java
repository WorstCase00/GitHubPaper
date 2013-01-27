package edu.bocmst.scheduling.mrcpspmax.instance;

import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import edu.bocmst.scheduling.mrcpspmax.commons.GraphUtils;


public class AonNetworkJGraphTImpl implements IAonNetwork {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AonNetworkJGraphTImpl.class);
	
	private final DirectedGraph<Integer, IAonNetworkEdge> network;
	private final Set<Set<IAonNetworkEdge>> cycleStructures;
	private final ImmutableList<Set<Integer>> successors;
	private final ImmutableList<Set<Integer>> predecessors;

	protected AonNetworkJGraphTImpl(
			DirectedGraph<Integer, IAonNetworkEdge> network,
			Set<Set<IAonNetworkEdge>> cycleStructures,
			ImmutableList<Set<Integer>> successors,
			ImmutableList<Set<Integer>> predecessors) {
		this.network = network;
		this.cycleStructures = cycleStructures;
		this.successors = successors;
		this.predecessors = predecessors;
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

	@Override
	public Set<Integer> getSuccessors(int activity) {
		return this.successors.get(activity);
	}

	@Override
	public Set<Integer> getPredecessors(int activity) {
		return this.predecessors.get(activity);
	}

	public static IAonNetwork createInstance(
			DirectedGraph<Integer, IAonNetworkEdge> graph) {
		LOGGER.debug("create aon network instance for graph {}", graph);
		Set<Set<IAonNetworkEdge>> cycles = GraphUtils.calculateCycleStructures(graph);
		LOGGER.debug("found {} cycle structures", cycles.size());
		ImmutableList<Set<Integer>> succs = GraphUtils.getSuccessors(graph);
		ImmutableList<Set<Integer>> preds = GraphUtils.getPredecessors(graph);
		IAonNetwork instance = new AonNetworkJGraphTImpl(graph, cycles, succs, preds);
		return instance;
	}



	@Override
	public Set<Integer> getVertexSet() {
		return network.vertexSet();
	}
}
