package edu.bocmst.scheduling.mrcpspmax.instance.loader;

abstract class InstanceFileConstants {
	private InstanceFileConstants() {}
	
	static final String DELIMITER = "\t";
	static class ActivityBlock {

		static final int PROCESSING_TIME_INDEX = 2;
		static final int RENEWABLE_RESOURCE_START_INDEX = 3;
		
	}
	
	static class Header {

		static final int ACTIVITY_COUNT_IN_HEADER_INDEX = 0;
		static final int RENEWABLE_COUNT_IN_HEADER_INDEX = 1;
		static final int NON_RENEWABLE_COUNT_IN_HEADER_INDEX = 2;
		static final int MIXED_RENEWABLE_COUNT_INDEX = 3;
	}
	
	static class EdgeLine {
		static final int MODE_COUNT_INDEX = 1;
		static final int SUCCESSOR_COUNT_INDEX = 2;
	}
}
