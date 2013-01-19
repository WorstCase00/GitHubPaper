package edu.bocmst.scheduling.mrcpspmax.bmap.ga;

import org.apache.commons.configuration.PropertiesConfiguration;

import edu.bocmst.scheduling.mrcpspmax.bmap.ga.evaluation.BmapEvaluationType;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.factory.BmapFactoryType;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.selection.BampSelectionType;
import edu.bocmst.scheduling.mrcpspmax.commons.AbstractPropertyFileConfiguration;
import edu.bocmst.scheduling.mrcpspmax.methaheuristics.EvolutionEngineConfiguration;
import edu.bocmst.scheduling.mrcpspmax.methaheuristics.TerminationConditionConfiguration;

public class GaBmapSolverConfiguration extends AbstractPropertyFileConfiguration {

	static final class Keys {

		public static final String ELITE_COUNT = "eliteCount";
		public static final String POPULATION_COUNT = "populationCount";
		public static final String SELECTION_TYPE = "selectionType";
		public static final String FACTORY_TYPE = "factoryType";
		public static final String EVALUATION_TYPE = "evaluationType";
		
	}
	
	static final class Defaults {

		public static final int ELITE_COUNT = 5;
		public static final int POPULATION_COUNT = 100;
		public static final String SELECTION_TYPE = "Sampling";
		public static final String FACTORY_TYPE = "RelativeResource";
		public static final String EVALUATION_TYPE = BmapEvaluationType.PenalizingLongestPath.name();//"PenalizingLongestPath";
		
	}

	private final TerminationConditionConfiguration terminationConditionConfiguration;
	private final EvolutionEngineConfiguration evolutionEngineConfiguration;

	protected GaBmapSolverConfiguration(
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

	public BampSelectionType getSelectionType() {
		String typeString = configuration.getString(
				Keys.SELECTION_TYPE,
				Defaults.SELECTION_TYPE);
		return BampSelectionType.valueOf(typeString);
	}
	
	public BmapFactoryType getFactoryType() {
		String typeString = configuration.getString(
				Keys.FACTORY_TYPE,
				Defaults.FACTORY_TYPE);
		return BmapFactoryType.valueOf(typeString);
	}
	
	public static GaBmapSolverConfiguration createDefault() {
		PropertiesConfiguration config = new PropertiesConfiguration();
		TerminationConditionConfiguration terminationConditionConfig = TerminationConditionConfiguration.createDefault();
		EvolutionEngineConfiguration evolutionEngineConfig = EvolutionEngineConfiguration.createDefault();
		GaBmapSolverConfiguration instance = new GaBmapSolverConfiguration(
				config, 
				terminationConditionConfig, 
				evolutionEngineConfig);
		return instance;
	}

	public BmapEvaluationType getEvaluationType() {
		String typeString = configuration.getString(
				Keys.EVALUATION_TYPE,
				Defaults.EVALUATION_TYPE);
		return BmapEvaluationType.valueOf(typeString);
	}

	

}
