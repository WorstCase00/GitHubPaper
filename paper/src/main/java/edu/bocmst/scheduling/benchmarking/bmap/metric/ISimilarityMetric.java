package edu.bocmst.scheduling.benchmarking.bmap.metric;

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IModeAssignment;

public interface ISimilarityMetric {

	double getSimilarityMetric(IModeAssignment candidate1, IModeAssignment candidate2);
}
