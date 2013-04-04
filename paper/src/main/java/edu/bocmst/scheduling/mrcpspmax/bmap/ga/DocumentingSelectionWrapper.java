package edu.bocmst.scheduling.mrcpspmax.bmap.ga;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.SelectionStrategy;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.bmap.metric.IModeAssignmentCollectionMetric;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.metric.Metric;

public class DocumentingSelectionWrapper implements
		SelectionStrategy<IModeAssignment> {

		private static final Logger LOGGER = LoggerFactory
				.getLogger(DocumentingSelectionWrapper.class);

	private final SelectionStrategy<? super IModeAssignment> selection;
	private final GaBmapSolverConfiguration configuration;
	private final List<IModeAssignmentCollectionMetric> metrics;

	public DocumentingSelectionWrapper(
			SelectionStrategy<? super IModeAssignment> selection,
			GaBmapSolverConfiguration configuration, 
			List<IModeAssignmentCollectionMetric> metrics) {
		this.selection = selection;
		this.configuration = configuration;
		this.metrics = metrics;
	}

	@Override
	public <S extends IModeAssignment> List<S> select(
			List<EvaluatedCandidate<S>> population,
			boolean naturalFitnessScores, int selectionSize, Random rng) {
		List<EvaluatedCandidate<IModeAssignment>> convert = Lists.newArrayList();
		for(EvaluatedCandidate<S> candidate : population) {
			convert.add((EvaluatedCandidate<IModeAssignment>) candidate);
		}
		logStatistics(convert);
		return this.selection.select(population, naturalFitnessScores, selectionSize, rng);
	}
	
	private void logStatistics(List<EvaluatedCandidate<IModeAssignment>> population) {
		for(IModeAssignmentCollectionMetric metric : metrics) {
			Metric metricResult = metric.calculateMetric(population);
			LOGGER.info("metric result: {}", metricResult);
		}
	}
}
