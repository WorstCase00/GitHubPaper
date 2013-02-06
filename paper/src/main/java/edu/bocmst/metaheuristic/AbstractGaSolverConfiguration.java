package edu.bocmst.metaheuristic;

import org.apache.commons.configuration.PropertiesConfiguration;

import edu.bocmst.utils.AbstractPropertyFileConfiguration;

public class AbstractGaSolverConfiguration extends AbstractPropertyFileConfiguration{

	static class Keys {
		public static final String ELITE_COUNT = "eliteCount";
		public static final String POPULATION_COUNT = "populationCount";
	}
	
	static class Defaults {
		public static final int ELITE_COUNT = 5;
		public static final int POPULATION_COUNT = 100;
	}
	
	private final TerminationConditionConfiguration terminationConditionConfiguration;
	private final EvolutionEngineConfiguration evolutionEngineConfiguration;
	
	public AbstractGaSolverConfiguration(
			PropertiesConfiguration configuration,
			TerminationConditionConfiguration terminationConditionConfiguration,
			EvolutionEngineConfiguration evolutionEngineConfiguration) {
		super(configuration);
		this.terminationConditionConfiguration = terminationConditionConfiguration;
		this.evolutionEngineConfiguration = evolutionEngineConfiguration;
	}

	public TerminationConditionConfiguration getTerminationConfiguration() {
		return this.terminationConditionConfiguration;
	}

	public int getEliteCount() {
		return configuration.getInt(
				Keys.ELITE_COUNT,
				Defaults.ELITE_COUNT);
	}

	public int getPopulationSize() {
		return configuration.getInt(
				Keys.POPULATION_COUNT,
				Defaults.POPULATION_COUNT);
	}

	public EvolutionEngineConfiguration getEvolutionEngineConfiguration() {
		return this.evolutionEngineConfiguration;
	}

}
