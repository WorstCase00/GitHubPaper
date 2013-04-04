package edu.bocmst.scheduling.mrcpspmax.metric;

import java.util.Collection;
import java.util.List;

import org.uncommons.watchmaker.framework.EvaluatedCandidate;

import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;

public class ValidPercentageMetric implements IMrcpspMaxPopulationMetric {

	private static final String NAME = "validPercentage";

	@Override
	public Metric calculateMetric(
			List<EvaluatedCandidate<IMrcpspMaxCandidate>> population) {
		Collection<EvaluatedCandidate<IMrcpspMaxCandidate>> validCandidates = MrcpspMaxUtils.getValidCandidates(population);
		double validPercentage = ((double) validCandidates.size()) / population.size();
		Metric metric = new Metric(NAME, validPercentage);
		return metric;
	}
}
