package edu.bocmst.scheduling.mrcpspmax.bmap.ga;

import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.TerminationCondition;

import edu.bocmst.scheduling.mrcpspmax.bmap.metric.UniqueValidModeAssignmentMetric;

public class MetricTerminationTermination implements TerminationCondition {

	private UniqueValidModeAssignmentMetric metric;
	private double upperLimit;

	public MetricTerminationTermination(
			UniqueValidModeAssignmentMetric uniqueValidModeAssignmentMetric,
			double upperLimit) {
		this.metric = uniqueValidModeAssignmentMetric;
		this.upperLimit = upperLimit;
	}

	@Override
	public boolean shouldTerminate(PopulationData<?> populationData) {
		if(upperLimit < this.metric.getValue()) {
			return true;
		}
		return false;
	}

}
