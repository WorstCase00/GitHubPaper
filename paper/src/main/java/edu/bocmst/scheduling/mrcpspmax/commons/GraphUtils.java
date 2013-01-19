package edu.bocmst.scheduling.mrcpspmax.commons;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

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
}
