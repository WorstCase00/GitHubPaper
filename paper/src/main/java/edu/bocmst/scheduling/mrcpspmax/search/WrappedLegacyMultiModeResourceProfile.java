package edu.bocmst.scheduling.mrcpspmax.search;

import com.google.common.collect.ImmutableList;

import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.LegacyResourceProfileImpl;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.IRenewableResource;
import edu.bocmst.utils.IntInterval;

public class WrappedLegacyMultiModeResourceProfile implements IMultiModeResourceProfile {
	private final LegacyResourceProfileImpl wrapped;
	private final IMrcpspMaxInstance instance;
	
	public WrappedLegacyMultiModeResourceProfile(IMrcpspMaxInstance instance) {
		this.instance = instance;
		ImmutableList<IRenewableResource> resources = instance.getRenewableResourceList();
		int[] resourceLimits = new int[resources.size()];
		for(int resource = 0; resource < resourceLimits.length; resource ++) {
			resourceLimits[resource] = resources.get(resource).getSupply();
		}
		wrapped = new LegacyResourceProfileImpl(resourceLimits);
	}

	@Override
	public int getEarliestPossibleStartInTimeWindowOrNegativeMissingTimeSpan(
			int activity,
			int mode,
			IntInterval startTimeWindow) {
		int lowerBound = startTimeWindow.getLowerBound();
		int duration = instance.getProcessingTime(activity, mode);
		int[] resourceDemands = instance.getRenewableResourceConsumption(activity, mode);
		Integer result = wrapped.findEarliestResourceFeasibleStart(lowerBound, duration, resourceDemands);
		if(result > startTimeWindow.getUpperBound()) {
			return startTimeWindow.getUpperBound() - result;
		}
		return result.intValue();
	}

	@Override
	public void schedule(
			int activity,
			int mode,
			int startTime) {
		int duration = instance.getProcessingTime(activity, mode);
		int[] resourceDemands = instance.getRenewableResourceConsumption(activity, mode);
		wrapped.bindResources(startTime, duration, resourceDemands);
		
	}

	@Override
	public void unschedule(
			int activity,
			int mode,
			int startTime) {
		int duration = instance.getProcessingTime(activity, mode);
		int[] resourceDemands = instance.getRenewableResourceConsumption(activity, mode);
		wrapped.freeResources(startTime, duration, resourceDemands);
	}
}
