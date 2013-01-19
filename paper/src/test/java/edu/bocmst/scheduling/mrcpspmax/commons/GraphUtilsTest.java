package edu.bocmst.scheduling.mrcpspmax.commons;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.bocmst.scheduling.mrcpspmax.TestConstants;
import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetworkEdge;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.loader.AonNetworkEdge;
import edu.bocmst.scheduling.mrcpspmax.instance.loader.InstanceLoader;

public class GraphUtilsTest {

	private static final int[][] EXPECTED_ADJACENCY_MATRIX = new int[][] {
		{GraphUtils.NO_EDGE, 0, 0, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE},
		{GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, 1, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE},
		{GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, 4, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE},
		{GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, 4, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE},
		{GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, -2, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, 4, GraphUtils.NO_EDGE},
		{GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, -4, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, 2},
		{GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, 4},
		{GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE, GraphUtils.NO_EDGE},
	};
	
	@Test
	public void testAdjacencyMatrixGeneration() throws IOException {
		
		int[] modes = new int[] {1,2,2,3,3,1,3,1};
		int[][] result = GraphUtils.getAdjacencyMatrix(modes, TestConstants.THESIS_INSTANCE);
		
		assertArrayEquals(EXPECTED_ADJACENCY_MATRIX, result);
	}
	
	@Test
	public void testPositiveCycleDetection() {
		int[] modes = new int[] {1,2,2,3,3,1,3,1};
		Set<Set<IAonNetworkEdge>> result = GraphUtils.getPositiveCycles(modes, TestConstants.THESIS_INSTANCE);
		
		assertTrue(result.size() == 1);
		Set<IAonNetworkEdge> cycle = result.iterator().next();
		assertTrue(cycle.contains(new AonNetworkEdge(2, 4)));
		assertTrue(cycle.contains(new AonNetworkEdge(4, 2)));
	}
	
	@Test
	public void testNoPositiveCycleDetection() {
		int[] modes = new int[] {1,2,1,1,1,3,3,1};
		Set<Set<IAonNetworkEdge>> result = GraphUtils.getPositiveCycles(modes, TestConstants.THESIS_INSTANCE);
		
		assertTrue(result.size() == 0);
	}
}
