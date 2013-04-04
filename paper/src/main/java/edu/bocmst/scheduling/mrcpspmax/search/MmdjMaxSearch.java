package edu.bocmst.scheduling.mrcpspmax.search;

import java.util.List;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bocmst.scheduling.mrcpspmax.MrcpspMaxSolution;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.IResourceProfile;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;
import edu.bocmst.scheduling.mrcpspmax.ga.repair.ModeAssignmentRepairWrapper;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.scheduler.CausalEligibilityTracker;
import edu.bocmst.utils.IntArrays;
import edu.bocmst.utils.IntInterval;

public class MmdjMaxSearch {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MmdjMaxSearch.class);
	private static final int MODE_INDEX = 0;
	private static final int START_INDEX = 1;
	
	private final IMrcpspMaxInstance instance;
	
	public MmdjMaxSearch(IMrcpspMaxInstance instance) {
		this.instance = instance;
	}

	public MrcpspMaxSolution search(IModeAssignment modeAssignment, Schedule schedule) {
		int[] oldModes = modeAssignment.getModeArray();
		int[] oldStartTimes = schedule.getStartTimes();
		MultiModeTemporalConstraintsTracker temporalConstraintsTracker = new MultiModeTemporalConstraintsTracker(
				oldModes, 
				oldStartTimes, 
				instance);
		IMultiModeResourceProfile resourceProfile = new WrappedLegacyMultiModeResourceProfile(instance);
		int[] remainingResources = modeAssignment.getResourceRemainingVector();
		NonRenewableResourceTracker nonRenewableResourceTracker = new NonRenewableResourceTracker(remainingResources, instance);
		List<Integer> orderedActivities = MrcpspMaxUtils.getActivitiesOrderedByStartTimeDecreasing(oldStartTimes);
		
		int[] newStartTimes = new int[oldStartTimes.length];
		int[] newModes = new int[oldModes.length];
		for(int activity : orderedActivities) {
			resourceProfile.unschedule(activity, oldModes[activity], oldStartTimes[activity]);
			nonRenewableResourceTracker.freeResourcesForActivity(activity, oldModes[activity]);
			
			int[] newModeAndStart = getEarliestPossibleModeAndStart(
					activity,
					temporalConstraintsTracker,
					resourceProfile,
					nonRenewableResourceTracker);
			int newMode = newModeAndStart[MODE_INDEX];
			int newStart = newModeAndStart[START_INDEX];
			newModes[activity] = newMode;
			newStartTimes[activity] = newStart;
			if((oldModes[activity] == newMode) && (oldStartTimes[activity] == newStart)) {
				LOGGER.debug("no changes possible for activity {}", activity);
			} else {
				LOGGER.debug("change");
				temporalConstraintsTracker.update(activity, newMode, newStart);
				resourceProfile.schedule(activity, newMode, newStart);
				nonRenewableResourceTracker.update(activity, newMode, newStart);
			}
		}
		
		Schedule newSchedule = new Schedule(newStartTimes, new WrappedResourceProfile(resourceProfile, newModes));
		MrcpspMaxSolution solution = new MrcpspMaxSolution(newSchedule, newModes);
		return solution;
	}

	private int[] getEarliestPossibleModeAndStart(
			int activity,
			MultiModeTemporalConstraintsTracker temporalConstraintsTracker,
			IMultiModeResourceProfile renewableResourceTracker,
			NonRenewableResourceTracker nonRenewableResourceTracker) {
		LOGGER.debug("get earliest mode and start for activity {}", activity);
		int earliestMode = -1;
		int earliestStart = Integer.MAX_VALUE;
		for(int mode = 1; mode <= instance.getModeCount(activity); mode ++) {
			LOGGER.debug("check mode {}", mode);
			if(!nonRenewableResourceTracker.isFeasibleActivityWithMode(activity, mode)) {
				LOGGER.debug("mode {} is not feasible for activity {}", mode, activity);
				continue;
			}
			IntInterval temporalWindow = temporalConstraintsTracker.getStartTimeWindow(activity, mode);
			if(!IntArrays.isValidTimeWindow(temporalWindow)) {
				LOGGER.debug("start time window not valid: {}", temporalWindow);
				continue;
			}
			int start = renewableResourceTracker.getEarliestPossibleStartInTimeWindowOrNegativeMissingTimeSpan(activity, mode, temporalWindow);
			if(start < 0) {
				LOGGER.debug("found no suitable start because of resource constraints in temporal window {}", temporalWindow);
				continue;
			}
			LOGGER.debug("found start in this iteration: {}", start);
			if(start < earliestStart) {
				earliestStart = start;
				earliestMode = mode;
			}
		}
		int[] modeAndStart = new int[] {earliestMode, earliestStart};
		return modeAndStart;
	}

}
