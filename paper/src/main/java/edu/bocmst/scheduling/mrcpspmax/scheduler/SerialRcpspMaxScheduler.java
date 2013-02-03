package edu.bocmst.scheduling.mrcpspmax.scheduler;

import java.util.Arrays;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IRcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.IPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.IResourceProfile;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.ResourceProfileListImpl;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;
import edu.bocmst.utils.IntInterval;

class SerialRcpspMaxScheduler implements IRcpspMaxScheduler {

	private static final Logger LOGGER = LoggerFactory
		.getLogger(SerialRcpspMaxScheduler.class);
	
	private static final int UNSCHEDULE_LIMIT = 100;
	private final int unscheduleLimit = UNSCHEDULE_LIMIT; // might be parameterized

	private int unscheduleCount = 0;
	
	@Override
	public Schedule createSchedule(IModeAssignment candidate, IPriorityRule priorityRule) {
		LOGGER.debug("create schedule for candidate {} with priority rule {}", candidate, priorityRule);
		Set<Integer> scheduledActivities = Sets.newHashSet();
		IRcpspMaxInstance instance = candidate.getInstance();
		int activityCount = instance.getActivityCount();
		int[] startTimes = new int[activityCount];
		IResourceProfile resourceProfile = new ResourceProfileListImpl(instance);
		CausalEligibilityTracker causalConstraintsTracker = CausalEligibilityTracker.createInstance(candidate);
		TemporalConstraintsTracker temporalConstraintsTracker = TemporalConstraintsTracker.createInstance(candidate);
		
		while(scheduledActivities.size() != activityCount) {
			Set<Integer> eligibleActivities = causalConstraintsTracker.getEligibleActivities();
			LOGGER.debug("eligibile activities in this iteration: {}", Arrays.toString(eligibleActivities.toArray()));
			int activity = priorityRule.getNextActivity(eligibleActivities);
			LOGGER.debug("next activity to be schedule: {}", activity);
			IntInterval startTimeWindow = temporalConstraintsTracker.getStartTimeWindow(activity);
			LOGGER.debug("temporally valid start time window: {}", startTimeWindow);
			int earliestStartOrMissingTime = resourceProfile.getEarliestPossibleStartInTimeWindowOrNegativeMissingTimeSpan(activity, startTimeWindow);
			if(earliestStartOrMissingTime < 0) {
				LOGGER.debug("unscheduling procedure must be called because of activity: {} with missing time {}", activity, earliestStartOrMissingTime);
				unscheduleCount ++;
				if(unscheduleCount > unscheduleLimit) {
					LOGGER.debug("unschedule limit has been reached - procedure is cancelled");
					return null;
				}
				int timeSpan = -earliestStartOrMissingTime;
				LOGGER.debug("temporal bounds must be adjusted by timespan: {}", timeSpan);
				Set<Integer> unscheduleActivities = temporalConstraintsTracker.unschedule(activity, timeSpan, startTimes);
				LOGGER.debug("activities determined for unscheduling: {}", Arrays.toString(unscheduleActivities.toArray()));
				if(unscheduleActivities.contains(0)) {
					LOGGER.debug("procedure cancelled because activity 0 contained in unscheduling");
					return null;
				}
				LOGGER.debug("free resources for activities: {}", Arrays.toString(unscheduleActivities.toArray()));
				resourceProfile.unschedule(unscheduleActivities, startTimes);
				LOGGER.debug("causal constraints are adapted");
				causalConstraintsTracker.unschedule(unscheduleActivities);
				LOGGER.debug("invalidate start times for unscheduled activities");
				for(int unscheduleActivity : unscheduleActivities) {
					startTimes[unscheduleActivity] = -1;
				}
			} else {
				LOGGER.debug("activity {} is schedule at time slot {}", activity, earliestStartOrMissingTime);
				startTimes[activity] = earliestStartOrMissingTime;
				temporalConstraintsTracker.schedule(activity, earliestStartOrMissingTime);
				resourceProfile.schedule(activity, earliestStartOrMissingTime);
				causalConstraintsTracker.schedule(activity);
				scheduledActivities.add(activity);
			}
		}
		Schedule schedule = new Schedule(startTimes, resourceProfile);
		return schedule;
	}

}
