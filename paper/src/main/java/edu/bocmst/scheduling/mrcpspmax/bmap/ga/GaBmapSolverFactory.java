package edu.bocmst.scheduling.mrcpspmax.bmap.ga;

import java.util.List;
import java.util.Random;

import org.apache.commons.math3.genetics.TournamentSelection;
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

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.evaluation.BmapEvaluationType;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.evaluation.PenalizingEdgeSumEvaluator;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.evaluation.PenalizingLongestPathEvaluation;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.factory.BmapFactoryType;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.factory.RandomBmapFactory;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.factory.RelativeResourceConsumptionBmapFactory;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.recombination.BmapArrayCrossover;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.repair.BarriosBmapRepair;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.selection.BampSelectionType;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.selection.SimilaritySampling;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.selection.SimilarityTournament;
import edu.bocmst.scheduling.mrcpspmax.commons.RandomUtils;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.methaheuristics.EvolutionEngineConfiguration;
import edu.bocmst.scheduling.mrcpspmax.methaheuristics.EvolutionEngineType;
import edu.bocmst.scheduling.mrcpspmax.methaheuristics.TerminationConditionConfiguration;
import edu.bocmst.scheduling.mrcpspmax.methaheuristics.TerminationConditionCreator;
import gov.sandia.cognition.learning.algorithm.genetic.selector.TournamentSelector;

public abstract class GaBmapSolverFactory {

	private GaBmapSolverFactory() {}
	
	public static GaBmapSolver createInstance(
			IMrcpspMaxInstance problem, 
			GaBmapSolverConfiguration configuration) {
		EvolutionEngine<IModeAssignment> ga = createEngine(problem, configuration);
		
		int popSize = configuration.getPopulationSize();
		int elite = configuration.getEliteCount();
		
		TerminationConditionConfiguration terminationConfiguration = 
			configuration.getTerminationConfiguration();
		TerminationConditionCreator terminationCreator = new TerminationConditionCreator();
		
		TerminationCondition termination = terminationCreator.createCondition(terminationConfiguration);
		GaBmapSolver instance = new GaBmapSolver(
				ga, 
				popSize, 
				elite, 
				termination);
		return instance;
	}

	private static EvolutionEngine<IModeAssignment> createEngine(
			IMrcpspMaxInstance problem, 
			GaBmapSolverConfiguration solverConfiguration) {
		EvolutionEngineConfiguration engineConfiguration = solverConfiguration.getEvolutionEngineConfiguration();
		EvolutionEngineType engineType = engineConfiguration.getEngineType();
		CandidateFactory<IModeAssignment> candidateFactory = createFactory(problem, solverConfiguration);
		EvolutionaryOperator<IModeAssignment> evolutionScheme = createEvolutionScheme(solverConfiguration, problem);
		SelectionStrategy<? super IModeAssignment> selectionStrategy = createSelectionStrategy(solverConfiguration);
		FitnessEvaluator<? super IModeAssignment> fitnessEvaluator = 
			createFitnessEvaluator(problem, solverConfiguration);
		Random rng = RandomUtils.getInstance();
		switch(engineType) {
		case Generational: 	
		return new GenerationalEvolutionEngine<IModeAssignment>(
				candidateFactory, 
				evolutionScheme,
				fitnessEvaluator,
				selectionStrategy, 
				rng);
		case SteadyState: 
			int selectionSize = 2; // 1 if only mutation is used
			boolean forceSingleCandidateUpdate = false; // if two are produced by crossover - use them
			return new SteadyStateEvolutionEngine<IModeAssignment>(
				candidateFactory, 
				evolutionScheme, 
				fitnessEvaluator, 
				selectionStrategy, 
				selectionSize, 
				forceSingleCandidateUpdate, 
				rng);
		// here shoud be another one for barrios replacement strategy
		}
		throw new IllegalArgumentException(engineType.name());
	}

	private static FitnessEvaluator<? super IModeAssignment> createFitnessEvaluator(
			IMrcpspMaxInstance instance, 
			GaBmapSolverConfiguration solverConfiguration) {
		BmapEvaluationType type = solverConfiguration.getEvaluationType();
		switch(type) {
		case PenalizingEdgeSum: return new PenalizingEdgeSumEvaluator(instance);
		case PenalizingLongestPath: return new PenalizingLongestPathEvaluation();
		}
		throw new IllegalArgumentException();
	}

	private static SelectionStrategy<? super IModeAssignment> createSelectionStrategy(
			GaBmapSolverConfiguration solverConfiguration) {
		BampSelectionType type = solverConfiguration.getSelectionType();
		return new org.uncommons.watchmaker.framework.selection.TournamentSelection(new Probability(1.0));
//		switch(type) {
//		case Sampling: return new StochasticUniversalSampling();
//		case SimilaritySampling: return new SimilaritySampling();
//		case SimilarityTournament: return new SimilarityTournament();
//		}
//		throw new IllegalArgumentException();
	}

	private static EvolutionaryOperator<IModeAssignment> createEvolutionScheme(
			GaBmapSolverConfiguration solverConfiguration, IMrcpspMaxInstance instance) {
		List<EvolutionaryOperator<IModeAssignment>> operators = Lists.newArrayList();
		EvolutionaryOperator<IModeAssignment> crossoverOperator = createCrossoverOperator(
				solverConfiguration,
				instance);
		operators.add(crossoverOperator);
//		operators.add(mutationOperator);
		EvolutionaryOperator<IModeAssignment> repairOperator = createRepairOperator(
				solverConfiguration,
				instance
		);
		operators.add(repairOperator);
		EvolutionaryOperator<IModeAssignment> pipeline = new EvolutionPipeline<IModeAssignment>(operators);
		return pipeline;
	}

	private static EvolutionaryOperator<IModeAssignment> createRepairOperator(
			GaBmapSolverConfiguration solverConfiguration,
			IMrcpspMaxInstance instance) {
		// TODO Auto-generated method stub
		return BarriosBmapRepair.createInstance(instance);
	}

	private static EvolutionaryOperator<IModeAssignment> createCrossoverOperator(
			GaBmapSolverConfiguration solverConfiguration, IMrcpspMaxInstance problem) {
		// TODO Auto-generated method stub
		return BmapArrayCrossover.createInstance(2, problem);
	}

	private static CandidateFactory<IModeAssignment> createFactory(
			IMrcpspMaxInstance instance, 
			GaBmapSolverConfiguration solverConfiguration) {
		BmapFactoryType type = solverConfiguration.getFactoryType();
		switch(type) {
		case Random: return new RandomBmapFactory(instance);
		case RelativeResource: return new RelativeResourceConsumptionBmapFactory(instance);
		}
		throw new IllegalArgumentException();
	}
}
