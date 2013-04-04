package edu.bocmst.scheduling.mrcpspmax.metric;

import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;

public class ValidFitnessStdDevMetric implements IMrcpspMaxPopulationMetric {

	private static final String NAME = "validFitnessStdDev";

	@Override
	public Metric calculateMetric(
			List<EvaluatedCandidate<IMrcpspMaxCandidate>> population) {
		List<EvaluatedCandidate<IMrcpspMaxCandidate>> validCandidates = Lists.newArrayList(MrcpspMaxUtils.getValidCandidates(population));
		double[] fitnesses = new double[validCandidates.size()];
		for (int i = 0; i < fitnesses.length; i++) {
			fitnesses[i] = validCandidates.get(i).getFitness();
		}
		StandardDeviation stdDev = new StandardDeviation();
		double standardDeviation = stdDev.evaluate(fitnesses);
		Metric metric = new Metric(NAME, standardDeviation);
		return metric;
	}

}