package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import org.jgrapht.EdgeFactory;

import edu.bocmst.graph.IDirectedEdge;

class AonNetworkEdgeFactory implements EdgeFactory<Integer, IDirectedEdge> {

	public IDirectedEdge createEdge(
			Integer sourceVertex,
			Integer targetVertex) {
		IDirectedEdge edge = new AonNetworkEdge(sourceVertex.intValue(), targetVertex.intValue());
		return edge;
	}

}
