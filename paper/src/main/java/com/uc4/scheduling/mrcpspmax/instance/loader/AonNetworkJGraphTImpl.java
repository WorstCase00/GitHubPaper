package com.uc4.scheduling.mrcpspmax.instance.loader;

import org.jgrapht.DirectedGraph;

import com.uc4.scheduling.mrcpspmax.instance.INetworkEdge;
import com.uc4.scheduling.mrcpspmax.instance.INetworkVertex;

public class AonNetworkJGraphTImpl implements IAonNetwork {
	
	private final DirectedGraph<INetworkVertex, INetworkEdge> network;

	public AonNetworkJGraphTImpl(DirectedGraph<INetworkVertex, INetworkEdge> network) {
		super();
		this.network = network;
	}

	protected DirectedGraph<INetworkVertex, INetworkEdge> getNetwork() {
		return network;
	}
	
	
}
