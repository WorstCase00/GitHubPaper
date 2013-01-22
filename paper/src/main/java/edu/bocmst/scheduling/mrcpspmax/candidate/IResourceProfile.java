package edu.bocmst.scheduling.mrcpspmax.candidate;

import java.util.Set;

import edu.bocmst.scheduling.mrcpspmax.scheduler.StartTimeWindow;

public interface IResourceProfile {

	int getEarliestPossibleStartInTimeWindow(
			int activity,
			StartTimeWindow startTimeWindow);

	void schedule(int activity, int earliestPossibleStart);

	void unschedule(Set<Integer> unschedule, int[] startTimes);

}
