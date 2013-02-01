package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import org.jgrapht.EdgeFactory;

import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetworkEdge;

class AonNetworkEdgeFactory implements EdgeFactory<Integer, IAonNetworkEdge> {

	public IAonNetworkEdge createEdge(
			Integer sourceVertex,
			Integer targetVertex) {
		IAonNetworkEdge edge = new AonNetworkEdge(sourceVertex.intValue(), targetVertex.intValue());
		return edge;
	}

}
