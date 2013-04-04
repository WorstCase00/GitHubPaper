package edu.bocmst.scheduling.mrcpspmax.ga;

import org.apache.commons.configuration.PropertiesConfiguration;

import edu.bocmst.metaheuristic.AbstractGaSolverConfiguration;
import edu.bocmst.metaheuristic.EvolutionEngineConfiguration;
import edu.bocmst.metaheuristic.TerminationConditionConfiguration;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.factory.MrcpspMaxCandidateFactoryConfiguration;
import edu.bocmst.scheduling.mrcpspmax.ga.evaluation.MrcpspMaxFitnessEvaluatorConfiguration;
import edu.bocmst.scheduling.mrcpspmax.ga.recombination.MrcpspMaxRecombinationConfiguration;

public class GaMrcpspMaxSolverConfiguration extends AbstractGaSolverConfiguration {

	private final MrcpspMaxCandidateFactoryConfiguration candidateFactoryConfiguration;
	private final MrcpspMaxFitnessEvaluatorConfiguration fitnessEvaluatorConfiguration;

	protected GaMrcpspMaxSolverConfiguration(
			PropertiesConfiguration configuration, 
			TerminationConditionConfiguration terminationConditionConfiguration, 
			EvolutionEngineConfiguration evolutionEngineConfiguration, MrcpspMaxCandidateFactoryConfiguration candidateFactoryConfiguration, MrcpspMaxFitnessEvaluatorConfiguration fitnessEvaluatorConfiguration) {
		super(
				configuration, 
				terminationConditionConfiguration, 
				evolutionEngineConfiguration);
		this.candidateFactoryConfiguration = candidateFactoryConfiguration;
		this.fitnessEvaluatorConfiguration = fitnessEvaluatorConfiguration;
	}

	public static GaMrcpspMaxSolverConfiguration createDefault() {
		PropertiesConfiguration empty = new PropertiesConfiguration();
		GaMrcpspMaxSolverConfiguration defaultInstance = 
			new GaMrcpspMaxSolverConfiguration(empty,
					TerminationConditionConfiguration.createDefault(),
					EvolutionEngineConfiguration.createDefault(),
					MrcpspMaxCandidateFactoryConfiguration.createDefault(),
					MrcpspMaxFitnessEvaluatorConfiguration.createDefault());
		return defaultInstance;
	}

	public MrcpspMaxCandidateFactoryConfiguration getCandidateFactoryConfiguration() {
		return candidateFactoryConfiguration;
	}

	public MrcpspMaxFitnessEvaluatorConfiguration getMrcpspMaxEvaluatorConfiguration() {
		return fitnessEvaluatorConfiguration;
	}

	public MrcpspMaxRecombinationConfiguration getRecombinationConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
