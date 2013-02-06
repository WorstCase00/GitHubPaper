package edu.bocmst.scheduling.mrcpspmax.candidate.schedule;

import java.util.Arrays;

public class Schedule {

	private final int[] startTimes;
	private final IResourceProfile resourceProfile;
	
	public Schedule(int[] startTimes, IResourceProfile resourceProfile) {
		this.startTimes = startTimes;
		this.resourceProfile = resourceProfile;
	}

	public int[] getStartTimes() {
		return startTimes;
	}

	public IResourceProfile getResourceProfile() {
		return resourceProfile;
	}

	@Override
	public String toString() {
		return "Schedule [startTimes=" + Arrays.toString(startTimes)
				+ ", resourceProfile=" + resourceProfile + "]";
	}

	public int getMakespan() {
		return startTimes[startTimes.length - 1];
	}

}
