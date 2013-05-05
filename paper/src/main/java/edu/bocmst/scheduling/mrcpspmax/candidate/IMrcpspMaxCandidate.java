package edu.bocmst.scheduling.mrcpspmax.candidate;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.IPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.scheduler.IRcpspMaxScheduler;

public interface IMrcpspMaxCandidate {

	IPriorityRule getPriorityRule();
	
	IModeAssignment getModeAssignment();

	IRcpspMaxScheduler getScheduler();
	
	void setPriorityRule(IPriorityRule priorityRule);
	
	void setModeAssignment(IModeAssignment modeAssignment);

}
