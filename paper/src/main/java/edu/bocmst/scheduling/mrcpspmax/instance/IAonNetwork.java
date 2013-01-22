package edu.bocmst.scheduling.mrcpspmax.instance;

import java.util.Set;

public interface IAonNetwork {

	Set<IAonNetworkEdge> getEdges();

	IAonNetworkEdge getEdge(int source, int target);

	Set<Set<IAonNetworkEdge>> getCycleStructures();
	
	Set<Integer> getSuccessors(int activity);
	
	Set<Integer> getPredecessors(int activity);

}