package edu.bocmst.scheduling.mrcpspmax.metric;

import java.util.Collection;
import java.util.List;

import org.uncommons.watchmaker.framework.EvaluatedCandidate;

import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;

public class BestFitnessMetric implements IMrcpspMaxPopulationMetric {

	private static final String NAME = "bestFitness";

	@Override
	public Metric calculateMetric(
			List<EvaluatedCandidate<IMrcpspMaxCandidate>> population) {
		double minFitness = getMinFitness(population);
		Metric metric = new Metric(NAME, minFitness);
		return metric;
	}

	private double getMinFitness(
			Collection<EvaluatedCandidate<IMrcpspMaxCandidate>> population) {
		double min = Double.MAX_VALUE;
		for(EvaluatedCandidate<IMrcpspMaxCandidate> candidate : population) {
			min = Math.min(min, candidate.getFitness());
		}
		return min;
	}

}
