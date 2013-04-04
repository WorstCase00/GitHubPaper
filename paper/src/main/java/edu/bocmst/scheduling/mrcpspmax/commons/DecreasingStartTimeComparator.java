package edu.bocmst.scheduling.mrcpspmax.commons;

import java.util.Comparator;

public class DecreasingStartTimeComparator implements
		Comparator<ActivityToStartTimeEntry> {

	@Override
	public int compare(
			ActivityToStartTimeEntry entry0,
			ActivityToStartTimeEntry entry1) {
		Integer startTime0 = entry0.getStartTime();
		Integer startTime1 = entry1.getStartTime();
		return startTime1.compareTo(startTime0);
	}

}
