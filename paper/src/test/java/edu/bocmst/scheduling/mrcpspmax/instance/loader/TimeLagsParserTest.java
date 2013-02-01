package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import static org.junit.Assert.assertArrayEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import edu.bocmst.graph.IDirectedEdge;

public class TimeLagsParserTest {
	
	private static final String THESIS_FILE = "src/test/resources/thesisExample.sch";
	private static final int VERTEX_COUNT = 8;
	
	private static final int[][] TL_MATRIX01 = new int[][] {{0,0,0}};
	private static final int[][] TL_MATRIX02 = new int[][] {{0,0,0}};
	private static final int[][] TL_MATRIX13 = new int[][] {{3,2,1},{6,4,1},{1,2,3}};
	private static final int[][] TL_MATRIX24 = new int[][] {{4,4,1},{1,1,4},{2,2,1}};
	private static final int[][] TL_MATRIX35 = new int[][] {{1,1,2},{2,2,1},{4,1,2}};
	private static final int[][] TL_MATRIX42 = new int[][] {{-4,-4,-6},{-2,-1,-2},{-6,-2,-2}};
	private static final int[][] TL_MATRIX46 = new int[][] {{0,6,1},{2,1,7},{1,1,4}};
	private static final int[][] TL_MATRIX53 = new int[][] {{-3,-1,-4},{-4,-4,-6},{-3,-3,-2}};
	private static final int[][] TL_MATRIX57 = new int[][] {{2},{2},{1}};
	private static final int[][] TL_MATRIX67 = new int[][] {{1},{1},{4}};

	@Test
	public void testTimeLagParsing() throws FileNotFoundException, IOException {
		List<String> instanceLines = IOUtils.readLines(new FileInputStream(THESIS_FILE));
		
		Map<IDirectedEdge, int[][]> result = TimeLagsParser.parseTimeLags(instanceLines, VERTEX_COUNT);
		
		assertArrayEquals(TL_MATRIX01, result.get(new AonNetworkEdge(0, 1)));
		assertArrayEquals(TL_MATRIX02, result.get(new AonNetworkEdge(0, 2)));
		assertArrayEquals(TL_MATRIX13, result.get(new AonNetworkEdge(1, 3)));
		assertArrayEquals(TL_MATRIX24, result.get(new AonNetworkEdge(2, 4)));
		assertArrayEquals(TL_MATRIX35, result.get(new AonNetworkEdge(3, 5)));
		assertArrayEquals(TL_MATRIX42, result.get(new AonNetworkEdge(4, 2)));
		assertArrayEquals(TL_MATRIX46, result.get(new AonNetworkEdge(4, 6)));
		assertArrayEquals(TL_MATRIX53, result.get(new AonNetworkEdge(5, 3)));
		assertArrayEquals(TL_MATRIX57, result.get(new AonNetworkEdge(5, 7)));
		assertArrayEquals(TL_MATRIX67, result.get(new AonNetworkEdge(6, 7)));
	}
}