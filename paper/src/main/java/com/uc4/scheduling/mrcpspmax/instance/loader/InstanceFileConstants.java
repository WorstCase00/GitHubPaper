package com.uc4.scheduling.mrcpspmax.instance.loader;

public abstract class InstanceFileConstants {
	private InstanceFileConstants() {}
	
	public static final String DELIMITER = "\t";
	public static class ActivityBlock {

		public static final int PROCESSING_TIME_INDEX = 2;
		public static final int RENEWABLE_RESOURCE_START_INDEX = 3;
		
	}
	
	public static class Header {

		public static final int RENEWABLE_COUNT_IN_HEADER_INDEX = 1;
		public static final int ACTIVITY_COUNT_IN_HEADER_INDEX = 0;
		public static final int NON_RENEWABLE_COUNT_IN_HEADER_INDEX = 2;
	}
	
	public static class EdgeLine {
		public static final int MODE_COUNT_INDEX = 1;
		public static final int SUCCESSOR_COUNT_INDEX = 2;
	}
}
