package com.uc4.scheduling.mrcpspmax.instance.loader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jgrapht.DirectedGraph;
import org.junit.Test;
import static org.junit.Assert.*;

import com.uc4.scheduling.mrcpspmax.instance.INetworkEdge;
import com.uc4.scheduling.mrcpspmax.instance.INetworkVertex;

public class AonNetworkParserTest {

	private static final String THESIS_FILE = "src/test/resources/thesisExample.sch";
	private static final int VERTEX_COUNT = 8;
	private static final int EDGE_COUNT = 10;

	@Test
	public void testFunctionality() throws FileNotFoundException, IOException {
		List<String> instanceLines = IOUtils.readLines(new FileInputStream(THESIS_FILE));
		
		AonNetworkJGraphTImpl result = (AonNetworkJGraphTImpl) AonNetworkParser.parseProjectNetwork(instanceLines);
		DirectedGraph<INetworkVertex, INetworkEdge> network = result.getNetwork();
		
		assertEquals(VERTEX_COUNT, network.vertexSet().size());
		assertEquals(EDGE_COUNT, network.edgeSet().size());
	}
}
