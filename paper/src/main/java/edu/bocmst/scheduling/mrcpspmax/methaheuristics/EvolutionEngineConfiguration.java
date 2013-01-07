package edu.bocmst.scheduling.mrcpspmax.methaheuristics;

import org.apache.commons.configuration.PropertiesConfiguration;


public class EvolutionEngineConfiguration {

	private final PropertiesConfiguration properties;
	
	private static class Keys {

		public static final String TYPE = "type";
		
	}
	
	private static class Defaults {

		public static final String TYPE = "Generational";
		
	}
	
	protected EvolutionEngineConfiguration(PropertiesConfiguration properties) {
		this.properties = properties;
	}

	public EvolutionEngineType getEngineType() {
		String typeString = properties.getString(
				Keys.TYPE,
				Defaults.TYPE);
		EvolutionEngineType type = EvolutionEngineType.valueOf(typeString);
		return type;
	}

}
