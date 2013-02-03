package edu.bocmst.scheduling.mrcpspmax.candidate.schedule;

@Deprecated
public class LegacyIndexedTimeWindow extends LegacyTimeWindow {

	public int index;
	
	public LegacyIndexedTimeWindow(int earliest, int latest, int index) {
		super(earliest, latest);
		this.index = index;
	}
	
	public String toString() {
		return "[" + earliest + ", " + latest + "]: " + index;
	}
}
