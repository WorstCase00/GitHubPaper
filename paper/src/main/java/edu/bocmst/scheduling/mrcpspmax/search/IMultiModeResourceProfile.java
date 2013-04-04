package edu.bocmst.scheduling.mrcpspmax.search;

import edu.bocmst.utils.IntInterval;

public interface IMultiModeResourceProfile {
	int getEarliestPossibleStartInTimeWindowOrNegativeMissingTimeSpan(
			int activity,
			int mode,
			IntInterval startTimeWindow);

	void schedule(
			int activity,
			int mode, 
			int startTime);

	void unschedule(
			int activity,
			int mode, 
			int startTime);
}
