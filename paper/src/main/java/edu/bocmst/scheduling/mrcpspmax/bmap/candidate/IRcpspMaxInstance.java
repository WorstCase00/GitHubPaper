package edu.bocmst.scheduling.mrcpspmax.bmap.candidate;

import com.google.common.collect.ImmutableList;

import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetwork;
import edu.bocmst.scheduling.mrcpspmax.instance.IRenewableResource;

public interface IRcpspMaxInstance {

	int getProcessingTime(int activity);

	int[] getNonRenewableResourceConsumption(int activity);
	
	int[] getRenewableResourceConsumption(int acitivity);

	int[][] getPathMatrix();

	int[][] getAdjacencyMatrix();

	ImmutableList<IRenewableResource> getRenewableResourceList();

	int getActivityCount();

	IAonNetwork getAonNetwork();
}
