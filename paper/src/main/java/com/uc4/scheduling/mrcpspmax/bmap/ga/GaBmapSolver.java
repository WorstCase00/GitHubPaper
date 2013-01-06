package com.uc4.scheduling.mrcpspmax.bmap.ga;

import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
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
import com.uc4.scheduling.mrcpspmax.bmap.IBmapSolver;
import com.uc4.scheduling.mrcpspmax.bmap.ga.crossover.BmapArrayCrossover;
import com.uc4.scheduling.mrcpspmax.bmap.ga.evaluator.ConstrainedEdgeSumEvaluator;
import com.uc4.scheduling.mrcpspmax.bmap.ga.factory.RandomBmapFactory;
import com.uc4.scheduling.mrcpspmax.bmap.ga.selection.BampSelectionType;
import com.uc4.scheduling.mrcpspmax.bmap.ga.selection.SimilaritySampling;
import com.uc4.scheduling.mrcpspmax.bmap.ga.selection.SimilarityTournament;
import com.uc4.scheduling.mrcpspmax.bmap.solution.IModeAssignment;
import com.uc4.scheduling.mrcpspmax.commons.RandomSingleton;
import com.uc4.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import com.uc4.scheduling.mrcpspmax.methaheuristics.EvolutionEngineConfiguration;
import com.uc4.scheduling.mrcpspmax.methaheuristics.EvolutionEngineType;
import com.uc4.scheduling.mrcpspmax.methaheuristics.TerminationConditionConfiguration;
import com.uc4.scheduling.mrcpspmax.methaheuristics.TerminationConditionCreator;

public class GaBmapSolver implements IBmapSolver{

	private final EvolutionEngine<IModeAssignment> engine;
	private final int populationSize;
	private final int eliteCount;
	private final TerminationCondition terminationCondition;
	
	public GaBmapSolver(EvolutionEngine<IModeAssignment> engine,
			int populationSize, int eliteCount,
			TerminationCondition terminationCondition) {
		this.engine = engine;
		this.populationSize = populationSize;
		this.eliteCount = eliteCount;
		this.terminationCondition = terminationCondition;
	}

	public List<IModeAssignment> getRankedModeAssignments(IMrcpspMaxInstance instance) {
		List<EvaluatedCandidate<IModeAssignment>> solutions = engine.evolvePopulation(
				populationSize, 
				eliteCount, 
				terminationCondition);
		List<IModeAssignment> modeAssignments = extractAssignments(solutions);
		return modeAssignments;
	}
	
	private List<IModeAssignment> extractAssignments(
			List<EvaluatedCandidate<IModeAssignment>> solutions) {
		List<IModeAssignment> assignments = Lists.newArrayList();
		for(EvaluatedCandidate<IModeAssignment> solution : solutions) {
			IModeAssignment assignment = solution.getCandidate();
			assignments.add(assignment);
		}
		return assignments;
	}

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
		Random rng = RandomSingleton.getInstance();
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
			IMrcpspMaxInstance problem, GaBmapSolverConfiguration solverConfiguration) {
		// TODO Auto-generated method stub
		return new ConstrainedEdgeSumEvaluator(problem);
	}

	private static SelectionStrategy<? super IModeAssignment> createSelectionStrategy(
			GaBmapSolverConfiguration solverConfiguration) {
		BampSelectionType type = solverConfiguration.getSelectionType();
		switch(type) {
		case Sampling: return new StochasticUniversalSampling();
		case SimilaritySampling: return new SimilaritySampling();
		case SimilarityTournament: return new SimilarityTournament();
		}
		throw new IllegalArgumentException();
	}

	private static EvolutionaryOperator<IModeAssignment> createEvolutionScheme(
			GaBmapSolverConfiguration solverConfiguration, IMrcpspMaxInstance problem) {
		List<EvolutionaryOperator<IModeAssignment>> operators = Lists.newArrayList();
		EvolutionaryOperator<IModeAssignment> crossoverOperator = createCrossoverOperator(
				solverConfiguration,
				problem);
		operators.add(crossoverOperator);
//		operators.add(mutationOperator);
//		operators.add(repairOperator);
		EvolutionaryOperator<IModeAssignment> pipeline = new EvolutionPipeline<IModeAssignment>(operators);
		return pipeline;
	}

	private static EvolutionaryOperator<IModeAssignment> createCrossoverOperator(
			GaBmapSolverConfiguration solverConfiguration, IMrcpspMaxInstance problem) {
		// TODO Auto-generated method stub
		return BmapArrayCrossover.createInstance(2, problem);
	}

	private static CandidateFactory<IModeAssignment> createFactory(
			IMrcpspMaxInstance problem, 
			GaBmapSolverConfiguration solverConfiguration) {
		// TODO Auto-generated method stub
		return new RandomBmapFactory(problem);
	}

}
