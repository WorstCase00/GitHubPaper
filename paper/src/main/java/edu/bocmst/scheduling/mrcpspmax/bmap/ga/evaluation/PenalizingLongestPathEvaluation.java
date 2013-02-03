package edu.bocmst.scheduling.mrcpspmax.bmap.ga.evaluation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import edu.bocmst.metaheuristic.IGeneratedSolutionsCounter;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;

public class PenalizingLongestPathEvaluation implements
		FitnessEvaluator<IModeAssignment> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PenalizingEdgeSumEvaluator.class);
	private static final double RESOURCE_PENALTY = Double.MAX_VALUE;
	private static final double TIME_PENALTY = Double.MAX_VALUE / 2;
	
	private final IGeneratedSolutionsCounter solutionsCounter;

	public PenalizingLongestPathEvaluation(
			IGeneratedSolutionsCounter solutionsCounter) {
		this.solutionsCounter = solutionsCounter;
	}

	@Override
	public double getFitness(
			IModeAssignment candidate,
			List<? extends IModeAssignment> population) {
		if(!candidate.isResourceFeasible()) {
			LOGGER.debug("candidate not resource feasible - fitness value: {}", RESOURCE_PENALTY);
			return RESOURCE_PENALTY;
		} else if(!candidate.isTimeFeasible()) {
			LOGGER.debug("candidate not resource feasible - fitness value: {}", RESOURCE_PENALTY);
			return TIME_PENALTY;
		} else {
			solutionsCounter.increment();
			double longestPath = candidate.getTimeLagMakespan();
			LOGGER.debug("valid candidate with longest path: {}", longestPath);
			return longestPath;
		}
	}

	@Override
	public boolean isNatural() {
		return false;
	}

}
