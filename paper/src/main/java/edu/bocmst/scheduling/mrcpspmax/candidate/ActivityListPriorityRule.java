package edu.bocmst.scheduling.mrcpspmax.candidate;

import java.util.Set;

import com.google.common.collect.ImmutableList;

public class ActivityListPriorityRule implements IPriorityRule {

	private final ImmutableList<Integer> activityList;
	
	public ActivityListPriorityRule(ImmutableList<Integer> activityList) {
		this.activityList = activityList;
	}

	@Override
	public int getNextActivity(Set<Integer> eligibleActivities) {
		for(int activity : activityList) {
			if(eligibleActivities.contains(activity)) {
				return activity;
			}
		}
		throw new RuntimeException();
	}

}
