package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DefaultEdge;

import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetworkEdge;


public class AonNetworkEdgeFactory implements EdgeFactory<Integer, IAonNetworkEdge> {

	public IAonNetworkEdge createEdge(
			Integer sourceVertex,
			Integer targetVertex) {
		IAonNetworkEdge edge = new AonNetworkEdge(sourceVertex.intValue(), targetVertex.intValue());
		return edge;
	}

}
