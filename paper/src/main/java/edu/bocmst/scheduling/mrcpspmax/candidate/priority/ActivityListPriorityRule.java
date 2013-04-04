package edu.bocmst.scheduling.mrcpspmax.candidate.priority;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public class ActivityListPriorityRule implements IPriorityRule {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityListPriorityRule.class);

	private final ImmutableList<Integer> activityList;

	public ActivityListPriorityRule(List<Integer> activityList) {
		this.activityList = ImmutableList.copyOf(activityList);
	}

	@Override
	public int getNextActivityFromEligibleSet(Set<Integer> eligibleActivities) {
		if(eligibleActivities.isEmpty()) {
			LOGGER.error("no elgible activity available");
			throw new RuntimeException("no eligible activity");
		}
		for(int activity : activityList) {
			LOGGER.debug("examine next eligible activity {}", activity);
			if(eligibleActivities.contains(activity)) {
				LOGGER.debug("return top priority activity {}", activity);
				return activity;
			}
		}
		// impossibru!
		throw new RuntimeException();
	}

	@Override
	public ImmutableList<Integer> getIntegerListRepresentation() {
		return this.activityList;
	}

	@Override
	public int compare(int activity1, int activity2) {
		int indexActivity1 = activityList.indexOf(activity1);
		int indexActivity2 = activityList.indexOf(activity2);
		int compareValue = (new Integer(indexActivity1)).compareTo(indexActivity2);
		return compareValue;
	}

}
