package edu.bocmst.scheduling.mrcpspmax.bmap.metric;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.uncommons.watchmaker.framework.EvaluatedCandidate;

import com.google.common.collect.Sets;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;
import edu.bocmst.scheduling.mrcpspmax.metric.Metric;

public class UniqueModeArraysSumMetric implements IModeAssignmentCollectionMetric {

	private static final String NAME = "uniqueValidModeArrays";
	private Set<Integer> modeHashes = Sets.newHashSet();
	
	@Override
	public Metric calculateMetric(
			List<EvaluatedCandidate<IModeAssignment>> population) {
		List<EvaluatedCandidate<IModeAssignment>> validCandidates = MrcpspMaxUtils.getValidCandidates(population);
		List<int[]> modeAssignments = MrcpspMaxUtils.extractModeArrays(validCandidates);
		for(int[] modes : modeAssignments) {
			modeHashes.add(Arrays.hashCode(modes));
		}
		Metric metric = new Metric(NAME, (double) modeHashes.size());
		return metric;
	}

}
