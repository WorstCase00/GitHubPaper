package edu.bocmst.scheduling.mrcpspmax.bmap.metric;

import java.util.List;

import org.uncommons.watchmaker.framework.EvaluatedCandidate;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;
import edu.bocmst.scheduling.mrcpspmax.metric.Metric;

public class ValidPercentage implements IModeAssignmentCollectionMetric{

	private static final String NAME = "validModeAssignmentPercentage";

	@Override
	public Metric calculateMetric(
			List<EvaluatedCandidate<IModeAssignment>> population) {
		List<EvaluatedCandidate<IModeAssignment>> validCandidates = MrcpspMaxUtils.getValidCandidates(population);
		double percentage = ((double) validCandidates.size() )/ population.size();
		return new Metric(NAME, percentage);
	}

}
