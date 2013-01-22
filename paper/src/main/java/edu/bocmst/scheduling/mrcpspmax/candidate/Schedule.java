package edu.bocmst.scheduling.mrcpspmax.candidate;

public class Schedule {

	private final int[] startTimes;
	private final IResourceProfile resourceProfile;
	
	public Schedule(int[] startTimes, IResourceProfile resourceProfile) {
		this.startTimes = startTimes;
		this.resourceProfile = resourceProfile;
	}
	
	
}
