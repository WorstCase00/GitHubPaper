package com.uc4.scheduling.mrcpspmax.instance.loader;

import java.util.Set;

import com.uc4.scheduling.mrcpspmax.instance.INetworkEdge;

public interface IAonNetwork {

	Set<INetworkEdge> getEdges();

	INetworkEdge getEdge(int source, int target);

}