package edu.bocmst.scheduling.mrcpspmax.candidate.priority;

import java.util.Set;

public interface IPriorityRule {

	int getNextActivity(Set<Integer> scheduledActivities);

}
