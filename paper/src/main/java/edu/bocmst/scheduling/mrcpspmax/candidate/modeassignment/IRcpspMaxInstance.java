package edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment;

import com.google.common.collect.ImmutableList;

import edu.bocmst.graph.IDirectedGraph;
import edu.bocmst.scheduling.mrcpspmax.instance.IRenewableResource;

public interface IRcpspMaxInstance {

	int getProcessingTime(int activity);

	int[] getRenewableResourceConsumption(int acitivity);

	int[][] getPathMatrix();

	int[][] getAdjacencyMatrix();

	ImmutableList<IRenewableResource> getRenewableResourceList();

	int getActivityCount();

	IDirectedGraph getAonNetwork();
}
