package com.uc4.scheduling.mrcpspmax.instance.loader;

import java.util.Set;

import org.jgrapht.DirectedGraph;

import com.uc4.scheduling.mrcpspmax.instance.INetworkEdge;
import com.uc4.scheduling.mrcpspmax.instance.INetworkVertex;
import com.uc4.scheduling.mrcpspmax.instance.NetworkVertex;

public class AonNetworkJGraphTImpl implements IAonNetwork {
	
	private final DirectedGraph<INetworkVertex, INetworkEdge> network;

	public AonNetworkJGraphTImpl(DirectedGraph<INetworkVertex, INetworkEdge> network) {
		super();
		this.network = network;
	}

	protected DirectedGraph<INetworkVertex, INetworkEdge> getNetwork() {
		return network;
	}
	
	@Override
	public Set<INetworkEdge> getEdges() {
		return network.edgeSet();
	}

	@Override
	public INetworkEdge getEdge(int source, int target) {
		INetworkVertex sourceVertex = NetworkVertex.createInstance(source);
		INetworkVertex targetVertex = NetworkVertex.createInstance(target);
		INetworkEdge edge = network.getEdge(sourceVertex, targetVertex);
		return edge;
	}
}
