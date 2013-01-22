package edu.bocmst.scheduling.mrcpspmax.commons;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
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

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IRcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetworkEdge;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public abstract class GraphUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GraphUtils.class);
	static final int NO_EDGE = Integer.MIN_VALUE;
	
	private GraphUtils() {}

	public static Set<Set<IAonNetworkEdge>> getPositiveCycles(
			int[] modes,
			IMrcpspMaxInstance instance) {
		LOGGER.debug("find positive cycles in instance");
		Set<Set<IAonNetworkEdge>> cycleStructures = instance.getCycleStructures();
		Set<Set<IAonNetworkEdge>> positiveCycles = Sets.newHashSet();
		for(Set<IAonNetworkEdge> cycleStructure : cycleStructures) {
			if(isWeightSumPositive(cycleStructure, modes, instance)) {
				LOGGER.debug("positive cycle detected: {}", cycleStructure);
				positiveCycles.add(cycleStructure);
			}
		}
		return positiveCycles;
	}

	public static boolean isWeightSumPositive(
			Set<IAonNetworkEdge> edges, 
			int[] modes,
			IMrcpspMaxInstance instance) {
		LOGGER.debug("check if weight sum is positive");
		int sum = 0;
		for(IAonNetworkEdge edge : edges) {
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
		Set<IAonNetworkEdge> edges = instance.getAonNetworkEdges();
		for(IAonNetworkEdge edge : edges) {
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
	
	public static Set<Set<IAonNetworkEdge>> calculateCycleStructures(
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

	private static void addCyclesInConnectedComponent(
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

	public static ImmutableList<Set<Integer>> getSuccessors(
			DirectedGraph<Integer, IAonNetworkEdge> graph) {
		List<Set<Integer>> list = initEmptySets(graph.vertexSet().size());
		for(IAonNetworkEdge edge : graph.edgeSet()) {
			int successor = edge.getTarget();
			int activity = edge.getSource();
			list.get(activity).add(successor);
		}
		ImmutableList<Set<Integer>> immutableList = ImmutableList.copyOf(list);
		return immutableList;
	}

	private static List<Set<Integer>> initEmptySets(int n) {
		List<Set<Integer>> list = Lists.newArrayList();
		for(int activity = 0; activity < n; activity ++) {
			Set<Integer> emptySet = Sets.newHashSet();
			list.add(emptySet);
		}
		return list;
	}

	public static ImmutableList<Set<Integer>> getPredecessors(
			DirectedGraph<Integer, IAonNetworkEdge> graph) {
		List<Set<Integer>> list = initEmptySets(graph.vertexSet().size());
		for(IAonNetworkEdge edge : graph.edgeSet()) {
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
		for(IAonNetworkEdge edge : instance.getAonNetwork().getEdges()) {
			int source = edge.getSource();
			int target = edge.getTarget();
			int weight = instance.getAdjacencyMatrix()[source][target];
			if(weight > 0) {
				predecessors.get(target).add(source);
			}
		}
		return predecessors;
	}
}
