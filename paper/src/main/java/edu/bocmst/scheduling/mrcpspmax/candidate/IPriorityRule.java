package edu.bocmst.scheduling.mrcpspmax.candidate;

import java.util.Set;

public interface IPriorityRule {

	int getNextActivity(Set<Integer> scheduledActivities);

}
