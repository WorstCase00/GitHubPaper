package edu.bocmst.graph;

import org.jgrapht.DirectedGraph;

public abstract class DirectedGraphFactory {
	
	private DirectedGraphFactory() {}
	
	public static IDirectedGraph wrapJGraphTGraph(
			DirectedGraph<Integer, IDirectedEdge> graph) {
		return DirectedGraphJGraphTImpl.createInstance(graph);
	}
}
