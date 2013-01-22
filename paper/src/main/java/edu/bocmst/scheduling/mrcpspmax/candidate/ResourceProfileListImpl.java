package edu.bocmst.scheduling.mrcpspmax.candidate;

import java.util.Set;

import com.google.common.collect.ImmutableList;

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IRcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.IRenewableResource;
import edu.bocmst.scheduling.mrcpspmax.scheduler.StartTimeWindow;

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
	public int getEarliestPossibleStartInTimeWindow(
			int activity,
			StartTimeWindow startTimeWindow) {
		int lowerBound = startTimeWindow.getLowerBound();
		int duration = instance.getProcessingTime(activity);
		int[] resourceDemands = instance.getNonRenewableResourceConsumption(activity);
		Integer result = wrapped.findEarliestResourceFeasibleStart(lowerBound, duration, resourceDemands);
		if((result == null) || (result > startTimeWindow.getUpperBound())) {
			return -1;
		}
		return result.intValue();
	}

	@Override
	public void schedule(int activity, int startTime) {
		int duration = instance.getProcessingTime(activity);
		int[] resourceDemands = instance.getNonRenewableResourceConsumption(activity);
		wrapped.bindResources(startTime, duration, resourceDemands);
		
	}

	@Override
	public void unschedule(Set<Integer> activities, int[] startTimes) {
		for(int activity : activities) {
			unschedule(activity, startTimes[activity]);
		}
	}

	private void unschedule(int activity, int startTime) {
		int duration = instance.getProcessingTime(activity);
		int[] resourceDemands = instance.getNonRenewableResourceConsumption(activity);
		wrapped.freeResources(startTime, duration, resourceDemands);
	}

}
