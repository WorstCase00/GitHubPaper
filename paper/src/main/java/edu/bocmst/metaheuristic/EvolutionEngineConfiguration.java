package edu.bocmst.metaheuristic;

import org.apache.commons.configuration.PropertiesConfiguration;

import edu.bocmst.scheduling.mrcpspmax.commons.AbstractPropertyFileConfiguration;


public class EvolutionEngineConfiguration extends AbstractPropertyFileConfiguration {

	private static class Keys {

		public static final String TYPE = "type";
		
	}
	
	private static class Defaults {

		public static final String TYPE = "Generational";
		
	}
	
	protected EvolutionEngineConfiguration(PropertiesConfiguration properties) {
		super(properties);
	}

	public EvolutionEngineType getEngineType() {
		String typeString = configuration.getString(
				Keys.TYPE,
				Defaults.TYPE);
		EvolutionEngineType type = EvolutionEngineType.valueOf(typeString);
		return type;
	}

	public static EvolutionEngineConfiguration createDefault() {
		PropertiesConfiguration properties = new PropertiesConfiguration();
		EvolutionEngineConfiguration instance = new EvolutionEngineConfiguration(properties);
		return instance;
	}

}
