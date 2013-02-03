package edu.bocmst.scheduling.mrcpspmax.candidate.schedule;

@Deprecated
class LegacyTimeWindow {
	public int earliest;
	public int latest;
	
	public LegacyTimeWindow(int earliest, int latest) {
		this.earliest = earliest;
		this.latest = latest;
	}
}
