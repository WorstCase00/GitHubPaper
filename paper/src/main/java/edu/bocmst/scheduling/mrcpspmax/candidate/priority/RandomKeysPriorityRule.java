package edu.bocmst.scheduling.mrcpspmax.candidate.priority;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

public class RandomKeysPriorityRule implements IPriorityRule {

	private static final Logger LOGGER = LoggerFactory.getLogger(RandomKeysPriorityRule.class);

	private final ImmutableList<Integer> priorityList;

	public RandomKeysPriorityRule(List<Integer> priorities) {
		priorityList = ImmutableList.copyOf(priorities);
	}

	@Override
	public int getNextActivityFromEligibleSet(Set<Integer> eligibleActivities) {
		if(eligibleActivities.isEmpty()) {
			LOGGER.error("no elgible activity available");
			throw new RuntimeException("no eligible activity");
		}
		LOGGER.debug("get next activity from eligible set {}", Arrays.toString(eligibleActivities.toArray()));
		int topPriority = Integer.MAX_VALUE;
		int topPriorityActivity = -1;
		for(int activity : eligibleActivities) {
			int priority = priorityList.get(activity);
			LOGGER.debug("examine activity {} with priority {}", activity, priority);
			if(priority < topPriority) {
				LOGGER.debug("new top priority acivity: {}", activity);
				topPriority = priority;
				topPriorityActivity = activity;
			}
		}
		LOGGER.debug("return to activity: {}", topPriorityActivity);
		return topPriorityActivity;
	}

	@Override
	public ImmutableList<Integer> getIntegerListRepresentation() {
		return this.priorityList;
	}

	@Override
	public int compare(int activity1, int activity2) {
		int valueActivity1 = priorityList.get(activity1);
		int valueActivity2 = priorityList.get(activity2);
		int compareValue = (new Integer(valueActivity1)).compareTo(valueActivity2);
		return compareValue;
	}

	public static IPriorityRule toPriorityRule(int[] startTimes) {
		int[] p = new int[startTimes.length];
		TreeSet<Integer> orderedTimes = new TreeSet<Integer>(Ints.asList(startTimes));
		int count = 0;
		for(int time : orderedTimes) {
			for (int i = 0; i < startTimes.length; i++) {
				if(startTimes[i] == time) {
					p[i] = count; // TODO ++ is shit, but else everything blows up at crossover
//					jdkas�fjskljdfa
				}
			}
			count ++;
		}
		IPriorityRule instance = new RandomKeysPriorityRule(Ints.asList(p));
		return instance;
	}

}
