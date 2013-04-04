package edu.bocmst.scheduling.mrcpspmax.ga.selection;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.SelectionStrategy;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.bmap.metric.UniqueValidModeAssignmentMetric;
import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.ga.GaMrcpspMaxSolverConfiguration;
import edu.bocmst.scheduling.mrcpspmax.ga.recombination.ModeAssignmentMetricWrapper;
import edu.bocmst.scheduling.mrcpspmax.metric.BestFitnessMetric;
import edu.bocmst.scheduling.mrcpspmax.metric.IMrcpspMaxPopulationMetric;
import edu.bocmst.scheduling.mrcpspmax.metric.Metric;
import edu.bocmst.scheduling.mrcpspmax.metric.ValidFitnessStdDevMetric;
import edu.bocmst.scheduling.mrcpspmax.metric.ValidPercentageMetric;

public class DocumentingSelectionStrategy implements SelectionStrategy<IMrcpspMaxCandidate> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentingSelectionStrategy.class);

	private final SelectionStrategy<? super IMrcpspMaxCandidate> selection;
	private final GaMrcpspMaxSolverConfiguration configuration;
	private final List<IMrcpspMaxPopulationMetric> metrics;

	public DocumentingSelectionStrategy(
			SelectionStrategy<? super IMrcpspMaxCandidate> selection,
			GaMrcpspMaxSolverConfiguration configuration) {
		this.selection = selection;
		this.configuration = configuration;
		this.metrics = Lists.newArrayList(
				new BestFitnessMetric(),
				new ValidFitnessStdDevMetric(),
				new ValidPercentageMetric(),
				new ModeAssignmentMetricWrapper(new UniqueValidModeAssignmentMetric())
				);
	}

	@Override
	public <S extends IMrcpspMaxCandidate> List<S> select(
			List<EvaluatedCandidate<S>> population,
			boolean naturalFitnessScores, 
			int selectionSize, 
			Random rng) {
		List<EvaluatedCandidate<IMrcpspMaxCandidate>> convertedPopulation = Lists.newArrayList();
		for(EvaluatedCandidate<S> candidate : population) {
			convertedPopulation.add((EvaluatedCandidate<IMrcpspMaxCandidate>) candidate);
		}
		logPopulationStatistics(convertedPopulation);
		LOGGER.debug("call wrapped selection strategy");
		return this.selection.select(population, naturalFitnessScores, selectionSize, rng);
	}
	
	private  void logPopulationStatistics(List<EvaluatedCandidate<IMrcpspMaxCandidate>> population) {
		for(IMrcpspMaxPopulationMetric metric : metrics) {
			Metric metricResult = metric.calculateMetric(population);
			LOGGER.info("metric result: {}", metricResult);
		}
	}

}
