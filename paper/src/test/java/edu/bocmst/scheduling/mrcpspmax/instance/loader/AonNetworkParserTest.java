package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import edu.bocmst.graph.IDirectedGraph;


public class AonNetworkParserTest extends BaseInstanceFileTest {

	private static final int EDGE_COUNT = 10;
	
	@Test
	public void testNetworkParsing() throws FileNotFoundException, IOException {
		List<String> instanceLines = readFromBarriosExampleFile();
		
		IDirectedGraph result = AonNetworkParser.parseProjectNetwork(instanceLines);
		
		assertEquals(EDGE_COUNT, result.getEdges().size());
	}
}
