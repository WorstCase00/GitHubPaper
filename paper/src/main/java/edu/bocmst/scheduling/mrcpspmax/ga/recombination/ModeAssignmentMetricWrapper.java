package edu.bocmst.scheduling.mrcpspmax.ga.recombination;

import java.util.List;

import org.uncommons.watchmaker.framework.EvaluatedCandidate;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.bmap.metric.IModeAssignmentCollectionMetric;
import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.metric.IMrcpspMaxPopulationMetric;
import edu.bocmst.scheduling.mrcpspmax.metric.Metric;

public class ModeAssignmentMetricWrapper implements IMrcpspMaxPopulationMetric {

	private final IModeAssignmentCollectionMetric modeAssignmentMetric;
	
	public ModeAssignmentMetricWrapper(
			IModeAssignmentCollectionMetric modeAssignmentMetric) {
		this.modeAssignmentMetric = modeAssignmentMetric;
	}

	@Override
	public Metric calculateMetric(
			List<EvaluatedCandidate<IMrcpspMaxCandidate>> population) {
		List<EvaluatedCandidate<IModeAssignment>> evaluatedModeAssignments = convert(population);
		Metric metric = this.modeAssignmentMetric.calculateMetric(evaluatedModeAssignments);
		return metric;
	}

	private List<EvaluatedCandidate<IModeAssignment>> convert(
			List<EvaluatedCandidate<IMrcpspMaxCandidate>> population) {
		List<EvaluatedCandidate<IModeAssignment>> modeAssignments = Lists.newArrayList();
		for(EvaluatedCandidate<IMrcpspMaxCandidate> candidate : population) {
			EvaluatedCandidate<IModeAssignment> modeAssignment = new EvaluatedCandidate<IModeAssignment>(
					candidate.getCandidate().getModeAssignment(), 
					candidate.getFitness());
			modeAssignments.add(modeAssignment);
		}
		return modeAssignments;
	}
}
