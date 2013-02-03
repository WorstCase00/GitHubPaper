package edu.bocmst.scheduling.benchmarking.bmap.metric;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;

public interface ISimilarityMetric {

	double getSimilarityMetric(IModeAssignment candidate1, IModeAssignment candidate2);
}
