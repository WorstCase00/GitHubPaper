package edu.bocmst.scheduling.mrcpspmax.bmap.candidate;

public interface IModeAssignment {

	int[] getModeArray();

	boolean isResourceFeasible();

	boolean isTimeFeasible();

	double getTimeLagMakespan();
	
	int getProcessingTime(int activity);
	
	int[] getResourceRemainingVector();

	IRcpspMaxInstance getInstance();

}
