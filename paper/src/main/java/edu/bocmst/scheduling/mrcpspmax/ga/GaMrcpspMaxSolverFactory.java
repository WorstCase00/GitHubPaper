package edu.bocmst.scheduling.mrcpspmax.ga;

import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.SteadyStateEvolutionEngine;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.StochasticUniversalSampling;
import org.uncommons.watchmaker.framework.selection.TournamentSelection;

import com.google.common.collect.Lists;

import edu.bocmst.metaheuristic.EvolutionEngineConfiguration;
import edu.bocmst.metaheuristic.EvolutionEngineType;
import edu.bocmst.metaheuristic.IGeneratedSolutionsCounter;
import edu.bocmst.metaheuristic.TerminationConditionConfiguration;
import edu.bocmst.metaheuristic.TerminationConditionCreator;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.factory.MrcpspMaxCandidateFactoryConfiguration;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.mutation.RandomModeMutation;
import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxRandomUtils;
import edu.bocmst.scheduling.mrcpspmax.ga.evaluation.MrcpspMaxFitnessEvaluatorConfiguration;
import edu.bocmst.scheduling.mrcpspmax.ga.evaluation.MrcpspMaxFitnessEvaluatorFactory;
import edu.bocmst.scheduling.mrcpspmax.ga.factory.MrcpspMaxFactoryFactory;
import edu.bocmst.scheduling.mrcpspmax.ga.mutation.ModeAssignmentMutationWrapper;
import edu.bocmst.scheduling.mrcpspmax.ga.recombination.MrcpspMaxRecombinationConfiguration;
import edu.bocmst.scheduling.mrcpspmax.ga.recombination.MrcpspMaxRecombinationFactory;
import edu.bocmst.scheduling.mrcpspmax.ga.repair.ModeAssignmentRepairWrapper;
import edu.bocmst.scheduling.mrcpspmax.ga.selection.DocumentingSelectionStrategy;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public abstract class GaMrcpspMaxSolverFactory {

	public static GaMrcpspMaxSolver create(
			IMrcpspMaxInstance problem, 
			GaMrcpspMaxSolverConfiguration configuration,
			IGeneratedSolutionsCounter solutionsCounter) {
		int populationSize = configuration.getPopulationSize();
		int eliteCount = configuration.getEliteCount();
		
		TerminationConditionConfiguration terminationConfiguration = 
			configuration.getTerminationConfiguration();
		TerminationConditionCreator terminationCreator = new TerminationConditionCreator();
		EvolutionEngine<IMrcpspMaxCandidate> ga = createEngine(
				problem, 
				configuration,
				solutionsCounter);
		TerminationCondition terminationCondition = terminationCreator.createCondition(terminationConfiguration, solutionsCounter);
		GaMrcpspMaxSolver instance = new GaMrcpspMaxSolver(
				ga, 
				populationSize, 
				eliteCount, 
				terminationCondition);
		return instance;
	}

	private static EvolutionEngine<IMrcpspMaxCandidate> createEngine(
			IMrcpspMaxInstance problem,
			GaMrcpspMaxSolverConfiguration configuration,
			IGeneratedSolutionsCounter solutionsCounter) {
		EvolutionEngineConfiguration engineConfiguration = configuration.getEvolutionEngineConfiguration();
		EvolutionEngineType engineType = engineConfiguration.getEngineType();
		CandidateFactory<IMrcpspMaxCandidate> candidateFactory = createFactory(problem, configuration);
		EvolutionaryOperator<IMrcpspMaxCandidate> evolutionScheme = createEvolutionScheme(configuration, problem);
		SelectionStrategy<? super IMrcpspMaxCandidate> selectionStrategy = new DocumentingSelectionStrategy(createSelectionStrategy(configuration), configuration);
		FitnessEvaluator<? super IMrcpspMaxCandidate> fitnessEvaluator = 
			createFitnessEvaluator(problem, configuration, solutionsCounter);
		Random rng = MrcpspMaxRandomUtils.getInstance();
		switch(engineType) {
		case Generational: 	
		return new GenerationalEvolutionEngine<IMrcpspMaxCandidate>(
				candidateFactory, 
				evolutionScheme,
				fitnessEvaluator,
				selectionStrategy, 
				rng);
		case SteadyState: 
			int selectionSize = 2; // 1 if only mutation is used
			boolean forceSingleCandidateUpdate = false; // if two are produced by crossover - use them
			return new SteadyStateEvolutionEngine<IMrcpspMaxCandidate>(
				candidateFactory, 
				evolutionScheme, 
				fitnessEvaluator, 
				selectionStrategy, 
				selectionSize, 
				forceSingleCandidateUpdate, 
				rng);
		}
		throw new IllegalArgumentException(engineType.name());
	}

	private static FitnessEvaluator<? super IMrcpspMaxCandidate> createFitnessEvaluator(
			IMrcpspMaxInstance problem,
			GaMrcpspMaxSolverConfiguration configuration,
			IGeneratedSolutionsCounter solutionsCounter) {
		MrcpspMaxFitnessEvaluatorConfiguration evaluatorConfiguration = configuration.getMrcpspMaxEvaluatorConfiguration();
		FitnessEvaluator<? super IMrcpspMaxCandidate> evaluator = MrcpspMaxFitnessEvaluatorFactory.createInstance(
				problem, 
				evaluatorConfiguration, 
				solutionsCounter);
		return evaluator;
	}

	private static SelectionStrategy<? super IMrcpspMaxCandidate> createSelectionStrategy(
			GaMrcpspMaxSolverConfiguration configuration) {
		// TODO Auto-generated method stub
//		return new TournamentSelection(new Probability(1d));// 
		return new StochasticUniversalSampling();
	}

	private static EvolutionaryOperator<IMrcpspMaxCandidate> createEvolutionScheme(
			GaMrcpspMaxSolverConfiguration configuration, IMrcpspMaxInstance problem) {
		List<EvolutionaryOperator<IMrcpspMaxCandidate>> pipeline = Lists.newArrayList();
		MrcpspMaxRecombinationConfiguration recombinationConfiguration = configuration.getRecombinationConfiguration();
		EvolutionaryOperator<IMrcpspMaxCandidate> crossover = MrcpspMaxRecombinationFactory.create(recombinationConfiguration, problem);
		pipeline.add(crossover);
		
		EvolutionaryOperator<IMrcpspMaxCandidate> mutation = createMutationOperator(problem);
		EvolutionaryOperator<IMrcpspMaxCandidate> repair = ModeAssignmentRepairWrapper.createInstance(problem);
		pipeline.add(mutation);
		pipeline.add(repair);
		EvolutionaryOperator<IMrcpspMaxCandidate> evolutionPipeline = new EvolutionPipeline<IMrcpspMaxCandidate>(pipeline);
		return evolutionPipeline;
	}

	private static EvolutionaryOperator<IMrcpspMaxCandidate> createMutationOperator(
			IMrcpspMaxInstance problem) {
		// TODO Auto-generated method stub
		return new ModeAssignmentMutationWrapper(new RandomModeMutation(problem));
	}

	private static CandidateFactory<IMrcpspMaxCandidate> createFactory(
			IMrcpspMaxInstance problem, 
			GaMrcpspMaxSolverConfiguration configuration) {
		
		MrcpspMaxCandidateFactoryConfiguration factoryConfiguration = configuration.getCandidateFactoryConfiguration();
		CandidateFactory<IMrcpspMaxCandidate> factory = MrcpspMaxFactoryFactory.createCandidateFactory(problem, factoryConfiguration);
		return factory;
	}
}
