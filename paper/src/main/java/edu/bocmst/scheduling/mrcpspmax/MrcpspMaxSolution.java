package edu.bocmst.scheduling.mrcpspmax;

import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;

public class MrcpspMaxSolution {

	private final Schedule schedule;
	private final int[] modes;
	public MrcpspMaxSolution(Schedule schedule, int[] modes) {
		this.schedule = schedule;
		this.modes = modes;
	}
	public Schedule getSchedule() {
		return schedule;
	}
	public int[] getModes() {
		return modes;
	}
}
