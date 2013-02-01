package edu.bocmst.scheduling.mrcpspmax.commons;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DirectedSubgraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.bocmst.graph.IDirectedEdge;
import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IRcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public abstract class GraphUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GraphUtils.class);
	public static final int NO_EDGE = Integer.MIN_VALUE;
	
	private GraphUtils() {}

	public static Set<Set<IDirectedEdge>> getPositiveCycles(
			int[] modes,
			IMrcpspMaxInstance instance) {
		LOGGER.debug("find positive cycles in instance");
		Set<Set<IDirectedEdge>> cycleStructures = instance.getCycleStructures();
		Set<Set<IDirectedEdge>> positiveCycles = Sets.newHashSet();
		for(Set<IDirectedEdge> cycleStructure : cycleStructures) {
			if(isWeightSumPositive(cycleStructure, modes, instance)) {
				LOGGER.debug("positive cycle detected: {}", cycleStructure);
				positiveCycles.add(cycleStructure);
			}
		}
		return positiveCycles;
	}

	public static boolean isWeightSumPositive(
			Set<IDirectedEdge> edges, 
			int[] modes,
			IMrcpspMaxInstance instance) {
		LOGGER.debug("check if weight sum is positive");
		int sum = 0;
		for(IDirectedEdge edge : edges) {
			int source = edge.getSource();
			int target = edge.getTarget();
			int weight = instance.getTimeLag(
					source, 
					modes[source], 
					target, 
					modes[target]);
			sum += weight;
		}
		return (sum > 0);
	}
	
	public static int[][] getAdjacencyMatrix(int[] modes, IMrcpspMaxInstance instance) {
		LOGGER.debug("create adjacency matrix");
		int activityCount = modes.length;
		int[][] matrix = initEmptyAdjacencyMatrix(activityCount);
		Set<IDirectedEdge> edges = instance.getAonNetworkEdges();
		for(IDirectedEdge edge : edges) {
			int source = edge.getSource();
			int target = edge.getTarget();
			int weight = instance.getTimeLag(source, modes[source], target, modes[target]);
			matrix[source][target] = weight;
		}
		return matrix;
	}
	
	private static int[][] initEmptyAdjacencyMatrix(int activityCount) {
		int[][] matrix = new int[activityCount][activityCount];
		for(int x = 0; x < activityCount; x ++) {
			for(int y = 0; y < activityCount; y ++) {
				matrix[x][y] = NO_EDGE;
			}
		}
		return matrix;
	}

	public static int[][] floydWarshallLongestPathWithoutPositiveCycleDetection(
			int[][] inputMatrix) {
		LOGGER.debug("start floyd warshall algorithmm without cycle detection");
		int[][] matrix = inputMatrix.clone();
		int n = matrix.length;
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				if (matrix[i][k] != NO_EDGE) {
					for (int j = 0; j < n; j++) {
						if (matrix[k][j] != NO_EDGE) {
							if(matrix[i][j] < matrix[i][k] + matrix[k][j]) {
								matrix[i][j] = matrix[i][k] + matrix[k][j];
							}
						}
					}
				}
			}
		}
		return matrix;
	}
	
	public static Set<Set<IDirectedEdge>> calculateCycleStructures(
			DirectedGraph<Integer, IDirectedEdge> network) {
		Set<Set<IDirectedEdge>> structures = Sets.newHashSet();
		StrongConnectivityInspector<Integer, IDirectedEdge> inspector = 
			new StrongConnectivityInspector<Integer, IDirectedEdge>(network);
		List<DirectedSubgraph<Integer, IDirectedEdge>> connectedComponents = inspector.stronglyConnectedSubgraphs();
		for(DirectedSubgraph<Integer, IDirectedEdge> connectedCmponent : connectedComponents) {
			addCyclesInConnectedComponent(structures, connectedCmponent);
		}
		return structures;
	}

	private static void addCyclesInConnectedComponent(
			Set<Set<IDirectedEdge>> cycleStructures,
			DirectedSubgraph<Integer, 
			IDirectedEdge> connectedCmponent) {
		for(Integer startVertex : connectedCmponent.vertexSet()) {
			KShortestPaths<Integer, IDirectedEdge> spp = new KShortestPaths<Integer, IDirectedEdge>(
					connectedCmponent, 
					startVertex, 
					Integer.MAX_VALUE, 
					connectedCmponent.vertexSet().size() + 1);
			Set<IDirectedEdge> incomingEdges = connectedCmponent.incomingEdgesOf(startVertex);
			for(IDirectedEdge incomingEdge : incomingEdges) {
				List<GraphPath<Integer, IDirectedEdge>> pathsForCycles = spp.getPaths(incomingEdge.getSource());
				if(pathsForCycles == null) {
					continue;
				}
				for(GraphPath<Integer, IDirectedEdge> pathForCycle : pathsForCycles) {
					Set<IDirectedEdge> edgeSet = Sets.newHashSet(pathForCycle.getEdgeList());
					edgeSet.add(incomingEdge);
					LOGGER.debug("found path: {}", Arrays.toString(edgeSet.toArray()));
					if(cycleStructures.contains(edgeSet)) {
						LOGGER.debug("cycle structure already found");
					} else {
						LOGGER.debug("add cycle structure: {}", Arrays.toString(edgeSet.toArray()));
						cycleStructures.add(edgeSet);
					}
				}
			}
		}
	}

	public static ImmutableList<Set<Integer>> getSuccessors(
			DirectedGraph<Integer, IDirectedEdge> graph) {
		List<Set<Integer>> list = initEmptySets(graph.vertexSet().size());
		for(IDirectedEdge edge : graph.edgeSet()) {
			int successor = edge.getTarget();
			int activity = edge.getSource();
			list.get(activity).add(successor);
		}
		ImmutableList<Set<Integer>> immutableList = ImmutableList.copyOf(list);
		return immutableList;
	}

	public static ImmutableList<Set<Integer>> getPredecessors(
			DirectedGraph<Integer, IDirectedEdge> graph) {
		List<Set<Integer>> list = initEmptySets(graph.vertexSet().size());
		for(IDirectedEdge edge : graph.edgeSet()) {
			int predecessor = edge.getSource();
			int activity = edge.getTarget();
			list.get(activity).add(predecessor);
		}
		ImmutableList<Set<Integer>> immutableList = ImmutableList.copyOf(list);
		return immutableList;
	}

	public static List<Set<Integer>> getPositivePredecessors(
			int[] modeArray,
			IRcpspMaxInstance instance) {
		List<Set<Integer>> predecessors = initEmptySets(modeArray.length);
		for(IDirectedEdge edge : instance.getAonNetwork().getEdges()) {
			int source = edge.getSource();
			int target = edge.getTarget();
			int weight = instance.getAdjacencyMatrix()[source][target];
			if(weight >= 0) {
				predecessors.get(target).add(source);
			}
		}
		return predecessors;
	}

	private static List<Set<Integer>> initEmptySets(int n) {
		List<Set<Integer>> list = Lists.newArrayList();
		for(int activity = 0; activity < n; activity ++) {
			Set<Integer> emptySet = Sets.newHashSet();
			list.add(emptySet);
		}
		return list;
	}

	public static List<Set<Integer>> getPositiveSuccessors(
		int[] modeArray,
		IRcpspMaxInstance instance) {
	List<Set<Integer>> successors = initEmptySets(modeArray.length);
	for(IDirectedEdge edge : instance.getAonNetwork().getEdges()) {
		int source = edge.getSource();
		int target = edge.getTarget();
		int weight = instance.getAdjacencyMatrix()[source][target];
		if(weight >= 0) {
			successors.get(source).add(target);
		}
	}
	return successors;
	}
}
