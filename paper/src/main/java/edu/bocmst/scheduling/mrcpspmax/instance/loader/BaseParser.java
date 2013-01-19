package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import java.util.List;

public abstract class BaseParser {

	protected static int getIntegerParsedIndexWord(String string, int index) {
		String[] words = string.split(InstanceFileConstants.DELIMITER);
		int integer = Integer.parseInt(words[index]);
		return integer;
	}
	
	protected static int getActivitesCount(List<String> instanceLines) {
		String headerString = instanceLines.get(0);
		int activities = getIntegerParsedIndexWord(
				headerString, 
				InstanceFileConstants.Header.ACTIVITY_COUNT_IN_HEADER_INDEX) + 2;
		return activities;
	}

	protected static int getRenewableResourcesCount(List<String> instanceLines) {
		String headerString = instanceLines.get(0);
		int renewableCount = getIntegerParsedIndexWord(
				headerString, 
				InstanceFileConstants.Header.RENEWABLE_COUNT_IN_HEADER_INDEX);
		return renewableCount;
	}

	protected static int getNonRenewableResourcesCount(List<String> instanceLines) {
		String headerString = instanceLines.get(0);
		int nonRenewableCount = getIntegerParsedIndexWord(
				headerString, 
				InstanceFileConstants.Header.NON_RENEWABLE_COUNT_IN_HEADER_INDEX);
		return nonRenewableCount;
	}

}
