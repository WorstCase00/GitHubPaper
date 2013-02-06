package edu.bocmst.scheduling.mrcpspmax.bmap.ga;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.TerminationCondition;

import com.google.common.collect.Lists;

import edu.bocmst.metaheuristic.AbstractGaSolver;
import edu.bocmst.scheduling.mrcpspmax.bmap.IBmapSolver;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;

class GaBmapSolver extends AbstractGaSolver<IModeAssignment, List<IModeAssignment>>implements IBmapSolver{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GaBmapSolver.class);
	
	public GaBmapSolver(
			EvolutionEngine<IModeAssignment> engine,
			int populationSize, 
			int eliteCount,
			TerminationCondition terminationCondition) {
		super(engine, populationSize, eliteCount, terminationCondition);
	}
	
	@Override
	protected List<IModeAssignment> extractSolutionFromFinalPopultation(
			List<EvaluatedCandidate<IModeAssignment>> solutions) {
		LOGGER.debug("extract mode assignments from final population");
		List<IModeAssignment> assignments = Lists.newArrayList();
		for(EvaluatedCandidate<IModeAssignment> solution : solutions) {
			IModeAssignment assignment = solution.getCandidate();
			assignments.add(assignment);
		}
		return assignments;
	}
}
