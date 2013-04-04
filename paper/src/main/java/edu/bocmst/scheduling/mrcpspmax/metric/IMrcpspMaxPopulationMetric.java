package edu.bocmst.scheduling.mrcpspmax.metric;

import java.util.List;

import org.uncommons.watchmaker.framework.EvaluatedCandidate;

import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;

public interface IMrcpspMaxPopulationMetric {

	Metric calculateMetric(List<EvaluatedCandidate<IMrcpspMaxCandidate>> population);
}
