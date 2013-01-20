package edu.bocmst.scheduling.mrcpspmax.bmap.candidate;

public interface IRcpspMaxInstance {

	int getProcessingTime(int activity);

	int[] getNonRenewableResourceConsumption(int acitivity);
	
	int[] getRenewableResourceConsumption(int acitivity);

	int[][] getPathMatrix();

	int[][] getAdjacencyMatrix();

}
