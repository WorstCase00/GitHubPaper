package edu.bocmst.metaheuristic;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.swing.evolutionmonitor.EvolutionMonitor;

import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

/**
 * 
 * @author Sturm
 *
 * @param <C> candidates
 * @param <S> solution
 */
public abstract class AbstractGaSolver<C, S> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractGaSolver.class);
	
	private final EvolutionEngine<C> engine;
	private final int populationSize;
	private final int eliteCount;
	private final TerminationCondition[] terminationCondition;
	
	public AbstractGaSolver(
			EvolutionEngine<C> engine,
			int populationSize, 
			int eliteCount,
			TerminationCondition... terminationCondition) {
		this.engine = engine;
		this.populationSize = populationSize;
		this.eliteCount = eliteCount;
		this.terminationCondition = terminationCondition;
	}
	
	public S solve(IMrcpspMaxInstance instance) {
		LOGGER.info("start problem resolution for instance {}", instance);
//		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("debug enabled - enable evolution monitor");
			EvolutionMonitor<C> monitor = new EvolutionMonitor<C>();
			int instanceId = instance.getInstanceId();
			monitor.showInFrame("monitor instance " + instanceId, true);
			engine.addEvolutionObserver(monitor);
//		}
		LOGGER.info("start evlutionary engine with population size = {}, " +
				"eliteCount = {} " +
				"and termination condition: {}",
				new Object[] {populationSize, eliteCount, terminationCondition});
		
		List<EvaluatedCandidate<C>> wrappedSolutions = engine.evolvePopulation(
				populationSize, 
				eliteCount, 
				terminationCondition);
		LOGGER.info("bmap result: {}", Arrays.toString(wrappedSolutions.toArray()));
		S solutions = extractSolutionFromFinalPopultation(wrappedSolutions);
		return solutions;
	}

	protected abstract S extractSolutionFromFinalPopultation(List<EvaluatedCandidate<C>> solutions);	
}
