package edu.bocmst.graph;

import org.jgrapht.EdgeFactory;


public class DirectedGraphEdgeFactory implements EdgeFactory<Integer, IDirectedEdge> {

	private static final DirectedGraphEdgeFactory SINGLETON = new DirectedGraphEdgeFactory();
	
	private DirectedGraphEdgeFactory() {}
	
	public IDirectedEdge createEdge(
			Integer sourceVertex,
			Integer targetVertex) {
		IDirectedEdge edge = new DirectedGraphEdge(sourceVertex.intValue(), targetVertex.intValue());
		return edge;
	}
	
	public static DirectedGraphEdgeFactory getInstance() {
		return SINGLETON;
	}

}
