package edu.bocmst.scheduling.mrcpspmax.methaheuristics;

import org.apache.commons.configuration.PropertiesConfiguration;

public class TerminationConditionConfiguration {

	private final PropertiesConfiguration properties;
	
	protected TerminationConditionConfiguration(PropertiesConfiguration properties) {
		this.properties = properties;
	}

	private static class Keys {
		public static final String TYPE = "type";
		public static final String MAX_DURATION_MS = "maxDurationMs";
		public static final String GENERATION_COUNT = "generationCount";
	}
	
	private static class Defaults {
		public static final String TYPE = "ElapsedTime";
		public static final long MAX_DURATION_MS = 1000;
		public static final int GENERATION_COUNT = 100;
	}
	
	public TerminationConditionType getType() {
		String typeString = properties.getString(
				Keys.TYPE,
				Defaults.TYPE);
		TerminationConditionType type = TerminationConditionType.valueOf(typeString);
		return type;
	}

	public long getMaxDurationMs() {
		long maxDuration = properties.getLong(
				Keys.MAX_DURATION_MS,
				Defaults.MAX_DURATION_MS);
		return maxDuration;
	}

	public int getGenerationCount() {
		int generationCount = properties.getInt(
				Keys.GENERATION_COUNT,
				Defaults.GENERATION_COUNT);
		return generationCount;
	}
	
	public static TerminationConditionConfiguration createDefault() {
		PropertiesConfiguration empty = new PropertiesConfiguration();
		TerminationConditionConfiguration instance = new TerminationConditionConfiguration(empty);
		return instance;
	}
}
