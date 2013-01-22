package edu.bocmst.scheduling.mrcpspmax.scheduler;

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.IPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.candidate.Schedule;

public interface IRcpspMaxScheduler {
	
	Schedule createSchedule(IModeAssignment candidate, IPriorityRule priorityRule);
}
