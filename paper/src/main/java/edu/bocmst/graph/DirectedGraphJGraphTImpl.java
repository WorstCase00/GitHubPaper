package edu.bocmst.graph;

import java.util.Arrays;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import edu.bocmst.scheduling.mrcpspmax.commons.GraphUtils;


class DirectedGraphJGraphTImpl implements IDirectedGraph {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DirectedGraphJGraphTImpl.class);
	
	private final DirectedGraph<Integer, IDirectedEdge> network;
	private final Set<Set<IDirectedEdge>> cycleStructures;
	private final ImmutableList<Set<Integer>> successors;
	private final ImmutableList<Set<Integer>> predecessors;

	private final boolean[][] pathMatrix;

	protected DirectedGraphJGraphTImpl(
			DirectedGraph<Integer, IDirectedEdge> network,
			Set<Set<IDirectedEdge>> cycleStructures,
			ImmutableList<Set<Integer>> successors,
			ImmutableList<Set<Integer>> predecessors, 
			boolean[][] pathMatrix) {
		this.network = network;
		this.cycleStructures = cycleStructures;
		this.successors = successors;
		this.predecessors = predecessors;
		this.pathMatrix = pathMatrix;
	}

	protected DirectedGraph<Integer, IDirectedEdge> getNetwork() {
		return network;
	}
	
	@Override
	public Set<IDirectedEdge> getEdges() {
		return network.edgeSet();
	}

	@Override
	public IDirectedEdge getEdge(int source, int target) {
		IDirectedEdge edge = network.getEdge(source, target);
		return edge;
	}

	@Override
	public Set<Set<IDirectedEdge>> getCycleStructures() {
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

	@Override
	public Set<Integer> getVertexSet() {
		return network.vertexSet();
	}
	
	@Override
	public boolean[][] getPathMatrix() {
		return this.pathMatrix;
	}

	static IDirectedGraph createInstance(
			DirectedGraph<Integer, IDirectedEdge> graph) {
		LOGGER.debug("create aon network instance for graph {}", graph);
		Set<Set<IDirectedEdge>> cycles = GraphUtils.calculateCycleStructures(graph);
		LOGGER.debug("found {} cycle structures", cycles.size());
		ImmutableList<Set<Integer>> succs = GraphUtils.getSuccessors(graph);
		ImmutableList<Set<Integer>> preds = GraphUtils.getPredecessors(graph);
		boolean[][] paths = GraphUtils.getPathPresenceMatrix(graph);
		IDirectedGraph instance = new DirectedGraphJGraphTImpl(graph, cycles, succs, preds, paths);
		return instance;
	}

	@Override
	public String toString() {
		return "DirectedGraphJGraphTImpl ["
				+ "cycleStructureCount=" + cycleStructures.size()
				+ ", cycleStructures=" + Arrays.toString(cycleStructures.toArray())
				+ ", successors=" + Arrays.toString(successors.toArray()) + "]";
	}
	
	
}
