package edu.bocmst.scheduling.mrcpspmax.bmap.ga;

import org.apache.commons.configuration.PropertiesConfiguration;

import edu.bocmst.metaheuristic.AbstractGaSolverConfiguration;
import edu.bocmst.metaheuristic.EvolutionEngineConfiguration;
import edu.bocmst.metaheuristic.TerminationConditionConfiguration;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.evaluation.BmapEvaluationType;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.factory.BmapFactoryType;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.selection.BampSelectionType;

public class GaBmapSolverConfiguration extends AbstractGaSolverConfiguration {

	public static final class Keys {
		public static final String SELECTION_TYPE = "selectionType";
		public static final String FACTORY_TYPE = "factoryType";
		public static final String EVALUATION_TYPE = "evaluationType";
	}
	
	public static final class Defaults {
		public static final String SELECTION_TYPE = "Tournament";
		public static final String FACTORY_TYPE = "RelativeResource";
		public static final String EVALUATION_TYPE = BmapEvaluationType.PenalizingLongestPath.name();//"PenalizingLongestPath";
	}
	
	protected GaBmapSolverConfiguration(
			PropertiesConfiguration configuration,
			TerminationConditionConfiguration terminationConditionConfiguration,
			EvolutionEngineConfiguration evolutionEngineConfiguration) {
		super(configuration, terminationConditionConfiguration, evolutionEngineConfiguration);
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
