package edu.bocmst.scheduling.mrcpspmax.scheduler;

import java.util.Set;

import com.google.common.collect.Sets;

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IRcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.candidate.IPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.candidate.IResourceProfile;
import edu.bocmst.scheduling.mrcpspmax.candidate.ResourceProfileListImpl;
import edu.bocmst.scheduling.mrcpspmax.candidate.Schedule;

public class SerialRcpspMaxScheduler implements IRcpspMaxScheduler {

	@Override
	public Schedule createSchedule(IModeAssignment candidate, IPriorityRule priorityRule) {
		
		Set<Integer> scheduledActivities = Sets.newHashSet();
		IRcpspMaxInstance instance = candidate.getInstance();
		int activityCount = instance.getActivityCount();
		int[] startTimes = new int[activityCount];
		IResourceProfile resourceProfile = new ResourceProfileListImpl(instance);
		CausalEligibilityTracker causalConstraintsTracker = CausalEligibilityTracker.createInstance(candidate);
		TemporalConstraintsTracker temporalConstraintsTracker = new TemporalConstraintsTracker(instance);
		
		while(scheduledActivities.size() != activityCount) {
			Set<Integer> eligibleActivities = causalConstraintsTracker.getEligibleActivities();
			int activity = priorityRule.getNextActivity(eligibleActivities);
			StartTimeWindow startTimeWindow = temporalConstraintsTracker.getStartTimeWindow(activity);
			int earliestPossibleStart = resourceProfile.getEarliestPossibleStartInTimeWindow(activity, startTimeWindow);
			if(earliestPossibleStart < 0) {
				int timeSpan = -earliestPossibleStart;
				Set<Integer> unscheduleActivities = temporalConstraintsTracker.calculateUnscheduleSet(activity, timeSpan);
				if(unscheduleActivities.contains(0)) {
					return null;
				}
				
				temporalConstraintsTracker.unschedule(unscheduleActivities);
				resourceProfile.unschedule(unscheduleActivities, startTimes);
				causalConstraintsTracker.unschedule(unscheduleActivities);
				for(int unscheduleActivity : unscheduleActivities) {
					startTimes[unscheduleActivity] = -1;
				}
			} else {
				startTimes[activity] = earliestPossibleStart;
				temporalConstraintsTracker.schedule(activity, earliestPossibleStart);
				resourceProfile.schedule(activity, earliestPossibleStart);
				causalConstraintsTracker.schedule(activity);
			}
		}
		Schedule schedule = new Schedule(startTimes, resourceProfile);
		return schedule;
	}

}
