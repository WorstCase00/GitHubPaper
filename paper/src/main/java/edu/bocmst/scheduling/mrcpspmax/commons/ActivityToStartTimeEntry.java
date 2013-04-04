package edu.bocmst.scheduling.mrcpspmax.commons;


class ActivityToStartTimeEntry {

	private final int activity;
	private final int startTime;
	
	ActivityToStartTimeEntry(int activity, int startTime) {
		this.activity = activity;
		this.startTime = startTime;
	}

	int getActivity() {
		return activity;
	}

	int getStartTime() {
		return startTime;
	}
	
}
