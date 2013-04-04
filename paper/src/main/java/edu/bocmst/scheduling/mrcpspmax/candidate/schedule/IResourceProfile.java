package edu.bocmst.scheduling.mrcpspmax.candidate.schedule;

import edu.bocmst.utils.IntInterval;

public interface IResourceProfile {

	int getEarliestPossibleStartInTimeWindowOrNegativeMissingTimeSpan(
			int activity, 
			IntInterval startTimeWindow);

	void schedule(int activity, int startTime);

	void unschedule(int activity, int startTime);

}
