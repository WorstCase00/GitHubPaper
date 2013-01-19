package edu.bocmst.scheduling.mrcpspmax.bmap.candidate;

public interface IRcpspMaxInstance {

	int getProcessingTime(int activity);

	int[] getNonRenewablwResourceConsumption(int acitivity);
	
	int[] getRenewablwResourceConsumption(int acitivity);

	int[][] getPathMatrix();

	int[][] getAdjacencyMatrix();

}
