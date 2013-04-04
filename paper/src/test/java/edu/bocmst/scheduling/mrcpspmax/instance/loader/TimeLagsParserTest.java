package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import static org.junit.Assert.assertArrayEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.bocmst.graph.DirectedGraphEdgeFactory;
import edu.bocmst.graph.IDirectedEdge;

public class TimeLagsParserTest extends BaseInstanceFileTest {
	
	private static final int VERTEX_COUNT = 8;
	
	private static final int[][] TL_MATRIX01 = new int[][] {{0,0,0}};
	private static final int[][] TL_MATRIX02 = new int[][] {{0,0,0}};
	private static final int[][] TL_MATRIX13 = new int[][] {{3,2,1},{6,4,1},{1,2,3}};
	private static final int[][] TL_MATRIX24 = new int[][] {{4,4,1},{1,1,4},{2,2,1}};
	private static final int[][] TL_MATRIX35 = new int[][] {{1,1,2},{2,2,1},{4,1,2}};
	private static final int[][] TL_MATRIX42 = new int[][] {{-4,-4,-6},{-2,-1,-2},{-6,-2,-2}};
	private static final int[][] TL_MATRIX46 = new int[][] {{0,4,1},{2,1,7},{1,1,4}};
	private static final int[][] TL_MATRIX53 = new int[][] {{-3,-1,-4},{-4,-4,-6},{-3,-3,-2}};
	private static final int[][] TL_MATRIX57 = new int[][] {{2},{2},{1}};
	private static final int[][] TL_MATRIX67 = new int[][] {{1},{1},{4}};

	@Test
	public void testTimeLagParsing() throws FileNotFoundException, IOException {
		List<String> instanceLines = readFromBarriosExampleFile();
		
		Map<IDirectedEdge, int[][]> result = TimeLagsParser.parseTimeLags(instanceLines, VERTEX_COUNT);
		
		DirectedGraphEdgeFactory edgeFactory = DirectedGraphEdgeFactory.getInstance();
		assertArrayEquals(TL_MATRIX01, result.get(edgeFactory.createEdge(0, 1)));
		assertArrayEquals(TL_MATRIX02, result.get(edgeFactory.createEdge(0, 2)));
		assertArrayEquals(TL_MATRIX13, result.get(edgeFactory.createEdge(1, 3)));
		assertArrayEquals(TL_MATRIX24, result.get(edgeFactory.createEdge(2, 4)));
		assertArrayEquals(TL_MATRIX35, result.get(edgeFactory.createEdge(3, 5)));
		assertArrayEquals(TL_MATRIX42, result.get(edgeFactory.createEdge(4, 2)));
		assertArrayEquals(TL_MATRIX46, result.get(edgeFactory.createEdge(4, 6)));
		assertArrayEquals(TL_MATRIX53, result.get(edgeFactory.createEdge(5, 3)));
		assertArrayEquals(TL_MATRIX57, result.get(edgeFactory.createEdge(5, 7)));
		assertArrayEquals(TL_MATRIX67, result.get(edgeFactory.createEdge(6, 7)));
	}
}
