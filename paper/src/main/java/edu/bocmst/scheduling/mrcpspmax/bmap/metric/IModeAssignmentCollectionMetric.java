package edu.bocmst.scheduling.mrcpspmax.bmap.metric;

import java.util.List;

import org.uncommons.watchmaker.framework.EvaluatedCandidate;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.metric.Metric;
 
public interface IModeAssignmentCollectionMetric {

	Metric calculateMetric(List<EvaluatedCandidate<IModeAssignment>> population);
}
