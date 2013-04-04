package edu.bocmst.scheduling.mrcpspmax.search;

import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.IResourceProfile;
import edu.bocmst.utils.IntInterval;

public class WrappedResourceProfile implements IResourceProfile {

	private final IMultiModeResourceProfile wrapped;
	private final int[] modes;

	public WrappedResourceProfile(
			IMultiModeResourceProfile resourceProfile,
			int[] modes) {
		this.wrapped = resourceProfile;
		this.modes = modes;
	}

	@Override
	public int getEarliestPossibleStartInTimeWindowOrNegativeMissingTimeSpan(
			int activity, IntInterval startTimeWindow) {
		return wrapped.getEarliestPossibleStartInTimeWindowOrNegativeMissingTimeSpan(
				activity, 
				modes[activity], 
				startTimeWindow);
	}

	@Override
	public void schedule(int activity, int startTime) {
		wrapped.schedule(activity, modes[activity], startTime);
	}

	@Override
	public void unschedule(int activity, int startTime) {
		wrapped.unschedule(activity, modes[activity], startTime);
	}

}
