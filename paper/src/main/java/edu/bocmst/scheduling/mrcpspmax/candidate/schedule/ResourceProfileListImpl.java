package edu.bocmst.scheduling.mrcpspmax.candidate.schedule;

import java.util.Set;

import com.google.common.collect.ImmutableList;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IRcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.IRenewableResource;
import edu.bocmst.utils.IntInterval;

public class ResourceProfileListImpl implements IResourceProfile {

	private final LegacyResourceProfileImpl wrapped;
	private final IRcpspMaxInstance instance;
	
	public ResourceProfileListImpl(IRcpspMaxInstance instance) {
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
			IntInterval startTimeWindow) {
		int lowerBound = startTimeWindow.getLowerBound();
		int duration = instance.getProcessingTime(activity);
		int[] resourceDemands = instance.getRenewableResourceConsumption(activity);
		Integer result = wrapped.findEarliestResourceFeasibleStart(lowerBound, duration, resourceDemands);
		if(result > startTimeWindow.getUpperBound()) {
			return startTimeWindow.getUpperBound() - result;
		}
		return result.intValue();
	}

	@Override
	public void schedule(int activity, int startTime) {
		int duration = instance.getProcessingTime(activity);
		int[] resourceDemands = instance.getRenewableResourceConsumption(activity);
		wrapped.bindResources(startTime, duration, resourceDemands);
		
	}

	@Override
	public void unschedule(int activity, int startTime) {
		int duration = instance.getProcessingTime(activity);
		int[] resourceDemands = instance.getRenewableResourceConsumption(activity);
		wrapped.freeResources(startTime, duration, resourceDemands);
	}

}
