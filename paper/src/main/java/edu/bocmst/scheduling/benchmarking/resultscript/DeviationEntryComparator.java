package edu.bocmst.scheduling.benchmarking.resultscript;

import java.util.Comparator;

public class DeviationEntryComparator implements
		Comparator<DeviationEntry> {

	public int compare(DeviationEntry entry1, DeviationEntry entry2) {
		Double dev1 = entry1.getDeviation();
		Double dev2 = entry2.getDeviation();
		return dev1.compareTo(dev2);
	}

}
