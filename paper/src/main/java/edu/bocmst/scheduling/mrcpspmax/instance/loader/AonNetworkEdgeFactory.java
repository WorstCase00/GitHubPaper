package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import org.jgrapht.EdgeFactory;

import edu.bocmst.scheduling.mrcpspmax.instance.INetworkEdge;
import edu.bocmst.scheduling.mrcpspmax.instance.INetworkVertex;

public class AonNetworkEdgeFactory implements
		EdgeFactory<INetworkVertex, INetworkEdge> {

	public INetworkEdge createEdge(
			INetworkVertex sourceVertex,
			INetworkVertex targetVertex) {
		INetworkEdge edge = new AonNetworkEdge(sourceVertex, targetVertex);
		return edge;
	}

}
