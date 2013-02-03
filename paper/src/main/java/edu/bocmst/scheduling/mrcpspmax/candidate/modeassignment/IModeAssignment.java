package edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment;

public interface IModeAssignment {

	int[] getModeArray();

	boolean isResourceFeasible();

	boolean isTimeFeasible();

	double getTimeLagMakespan();
	
	int getProcessingTime(int activity);
	
	int[] getResourceRemainingVector();

	IRcpspMaxInstance getInstance();

}
