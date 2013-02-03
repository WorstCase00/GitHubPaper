package edu.bocmst.scheduling.mrcpspmax.candidate.schedule;

import java.util.Set;

import edu.bocmst.utils.IntInterval;

public interface IResourceProfile {

	int getEarliestPossibleStartInTimeWindowOrNegativeMissingTimeSpan(
			int activity, 
			IntInterval startTimeWindow);

	void schedule(int activity, int earliestPossibleStart);

	void unschedule(Set<Integer> unschedule, int[] startTimes);

}
