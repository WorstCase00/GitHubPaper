package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import edu.bocmst.graph.IDirectedGraph;


public class AonNetworkParserTest {

	private static final String THESIS_FILE = "src/test/resources/thesisExample.sch";
	private static final int EDGE_COUNT = 10;
	
	@Test
	public void testNetworkParsing() throws FileNotFoundException, IOException {
		List<String> instanceLines = IOUtils.readLines(new FileInputStream(THESIS_FILE));
	
		IDirectedGraph result = AonNetworkParser.parseProjectNetwork(instanceLines);
		
		assertEquals(EDGE_COUNT, result.getEdges().size());
	}
}
