package edu.bocmst.scheduling.mrcpspmax.bmap.ga.evaluation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IModeAssignment;

public class PenalizingLongestPathEvaluation implements
		FitnessEvaluator<IModeAssignment> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PenalizingEdgeSumEvaluator.class);
	private static final double RESOURCE_PENALTY = Double.MAX_VALUE;
	private static final double TIME_PENALTY = Double.MAX_VALUE / 2;

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
		}
		double longestPath = candidate.getTimeLagMakespan();
		return longestPath;
	}

	@Override
	public boolean isNatural() {
		return false;
	}

}
