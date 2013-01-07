package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import java.util.Set;

import edu.bocmst.scheduling.mrcpspmax.instance.INetworkEdge;

public interface IAonNetwork {

	Set<INetworkEdge> getEdges();

	INetworkEdge getEdge(int source, int target);

}