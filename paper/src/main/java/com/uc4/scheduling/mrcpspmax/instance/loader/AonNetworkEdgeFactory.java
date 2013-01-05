package com.uc4.scheduling.mrcpspmax.instance.loader;

import org.jgrapht.EdgeFactory;

import com.uc4.scheduling.mrcpspmax.instance.INetworkEdge;
import com.uc4.scheduling.mrcpspmax.instance.INetworkVertex;

public class AonNetworkEdgeFactory implements
		EdgeFactory<INetworkVertex, INetworkEdge> {

	public INetworkEdge createEdge(
			INetworkVertex sourceVertex,
			INetworkVertex targetVertex) {
		INetworkEdge edge = new AonNetworkEdge(sourceVertex, targetVertex);
		return edge;
	}

}
