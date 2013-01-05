package com.uc4.scheduling.benchmarking;

import gov.sandia.cognition.statistics.method.ChiSquareConfidence;
import gov.sandia.cognition.statistics.method.MannWhitneyUConfidence;
import gov.sandia.cognition.statistics.method.StudentTConfidence;
import gov.sandia.cognition.statistics.method.WilcoxonSignedRankConfidence;
import gov.sandia.cognition.statistics.method.WilcoxonSignedRankConfidence.Statistic;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.Lists;

public class SignificanceTests {

	private static final double EPS = 0.000001;

	public static void evaluateSignificance(
			double[] lbs, 
			double[] results,
			double[] ballestin) {
		StudentTConfidence test = new StudentTConfidence();
//		ChiSquareConfidence test = new ChiSquareConfidence();
//		MannWhitneyUConfidence test = new MannWhitneyUConfidence();
//		WilcoxonSignedRankConfidence test = new WilcoxonSignedRankConfidence();
		Collection<? extends Number> absDeviations1 = getAbsoluteDeviations(ballestin, lbs);
		Collection<? extends Number> absDeviations2 = getAbsoluteDeviations(results, lbs);
		gov.sandia.cognition.statistics.method.StudentTConfidence.Statistic result = test.evaluateNullHypothesis(absDeviations1, absDeviations2);
		System.out.println("P0 absDevs: " + result.getNullHypothesisProbability());
		
		Collection<? extends Number> relDeviations1 = getRelativeDeviations(absDeviations1, lbs);
		Collection<? extends Number> relDeviations2 = getRelativeDeviations(absDeviations2, lbs);
		result = test.evaluateNullHypothesis(relDeviations1, relDeviations2);
		System.out.println("P0 relDevs: " + result.getNullHypothesisProbability());
	}

	private static Collection<? extends Number> getRelativeDeviations(
			Collection<? extends Number> absDeviations, double[] lbs) {
		Collection<Number> absoluteDevs = Lists.newArrayList();
		Iterator<? extends Number> valueIterator = absDeviations.iterator();
		for(int i = 0; i < absDeviations.size(); i++) {
			double value = (Double) valueIterator.next();
			Double absDev = value / lbs[i];
			absoluteDevs.add(absDev);
		}
		return absoluteDevs;
	}

	private static Collection<? extends Number> getAbsoluteDeviations(
			double[] results, double[] lbs) {
		Collection<Number> absoluteDevs = Lists.newArrayList();
		for(int i = 0; i < results.length; i++) {
			Double absDev = Math.abs(results[i] - lbs[i]) + EPS;
			absoluteDevs.add(absDev);
		}
		return absoluteDevs;
	}

}
