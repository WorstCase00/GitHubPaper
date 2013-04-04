package edu.bocmst.scheduling.mrcpspmax.bmap.metric;

import java.util.List;

import org.uncommons.watchmaker.framework.EvaluatedCandidate;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;
import edu.bocmst.scheduling.mrcpspmax.metric.Metric;
import edu.bocmst.utils.IntArrays;

public class UniqueValidModeAssignmentMetric implements
		IModeAssignmentCollectionMetric {

	private static final String NAME = "validUniqueModeAssignments";
	
	private int uniqueCount;

	@Override
	public Metric calculateMetric(
			List<EvaluatedCandidate<IModeAssignment>> population) {
		List<EvaluatedCandidate<IModeAssignment>> validCandidates = MrcpspMaxUtils.getValidCandidates(population);
		List<int[]> modeAssignments = MrcpspMaxUtils.extractModeArrays(validCandidates);
		this.uniqueCount = IntArrays.countUnique(modeAssignments);
		Metric metric = new Metric(NAME, (double) uniqueCount);
		return metric;
	}

	public double getValue() {
		return uniqueCount;
	}

}
