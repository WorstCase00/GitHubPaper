package edu.bocmst.scheduling.mrcpspmax.scheduler;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.IPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;

public interface IRcpspMaxScheduler {
	
	Schedule createSchedule(IModeAssignment candidate, IPriorityRule priorityRule);
}
