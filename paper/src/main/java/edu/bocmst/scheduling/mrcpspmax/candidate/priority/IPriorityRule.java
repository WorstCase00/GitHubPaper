package edu.bocmst.scheduling.mrcpspmax.candidate.priority;

import java.util.Set;

import com.google.common.collect.ImmutableList;

public interface IPriorityRule {

	int getNextActivityFromEligibleSet(Set<Integer> eligibleActivities);

	ImmutableList<Integer> getIntegerListRepresentation();
	
	int compare(int activity1, int activity2);
}
