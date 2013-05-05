package edu.bocmst.scheduling.mrcpspmax.search;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import edu.bocmst.scheduling.mrcpspmax.MrcpspMaxSolution;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.utils.IntInterval;

public class MmdjULeftShift implements IMrcpspMaxSearch {
	private static final Logger LOGGER = LoggerFactory.getLogger(RandomizedMmdjMaxSearch.class);
	private static final int MODE_INDEX = 0;
	private static final int START_INDEX = 1;

	private final IMrcpspMaxInstance instance;

	public MmdjULeftShift(IMrcpspMaxInstance instance) {
		this.instance = instance;
	}

	@Override
	public MrcpspMaxSolution search(
			IModeAssignment modeAssignment,
			Schedule schedule) {
		int[] oldModes = modeAssignment.getModeArray();
		int[] oldStartTimes = schedule.getStartTimes();
		int bound = oldStartTimes[oldStartTimes.length - 1];
		MmdjUTemporalConstraintsTracker temporalConstraintsTracker = new MmdjUTemporalConstraintsTracker(
				bound,
				instance);
		IMultiModeResourceProfile resourceProfile = new WrappedLegacyMultiModeResourceProfile(instance);
		int[] remainingResources = modeAssignment.getResourceRemainingVector();
		NonRenewableResourceTracker nonRenewableResourceTracker = new NonRenewableResourceTracker(remainingResources, instance);

		List<Integer> orderedActivities = MrcpspMaxUtils.getActivitiesOrderedByStartTimeIncreasing(oldStartTimes);
		Set<Integer> considered = Sets.newHashSet();

		int[] newStartTimes = new int[oldStartTimes.length];
		int[] newModes = new int[oldModes.length];
		for(int activity : orderedActivities) {
			int oldMode = oldModes[activity];
			LOGGER.debug("unschedule activity {} with mode {}", activity, oldMode);
			nonRenewableResourceTracker.freeResourcesForActivity(activity, oldMode);

			int[] newModeAndStart = getEarliestPossibleModeAndStart(
					activity,
					temporalConstraintsTracker,
					resourceProfile,
					nonRenewableResourceTracker);
			if(newModeAndStart == null) {
				LOGGER.debug("unschedule step is necessary");
				Set<Integer> toUnschedule = getUnscheduleActivities(activity, considered);
				resourceProfile = new WrappedLegacyMultiModeResourceProfile(instance);
				for(int unschedule : toUnschedule) {
					temporalConstraintsTracker.update(unschedule, oldModes[unschedule], oldStartTimes[unschedule]);

					resourceProfile.schedule(unschedule, oldModes[unschedule], oldStartTimes[unschedule]);

					nonRenewableResourceTracker.freeResourcesForActivity(unschedule, newModes[unschedule]);
					nonRenewableResourceTracker.update(unschedule, oldModes[unschedule], oldStartTimes[unschedule]);
				
					newModes[unschedule] = oldModes[unschedule];
					newStartTimes[unschedule] = oldStartTimes[unschedule];
				}
				temporalConstraintsTracker.update(activity, oldModes[activity], oldStartTimes[activity]);
				resourceProfile.schedule(activity, oldModes[activity], oldStartTimes[activity]);
				nonRenewableResourceTracker.update(activity, oldModes[activity], oldStartTimes[activity]);
				newModes[activity] = oldModes[activity];
				newStartTimes[activity] = oldStartTimes[activity];
				
				considered.add(activity);
				
			} else {
				int newMode = newModeAndStart[MODE_INDEX];
				int newStart = newModeAndStart[START_INDEX];
				LOGGER.debug("found earliest possible start time {} with mode {}", 
						newStart, newMode);

				newModes[activity] = newMode;
				newStartTimes[activity] = newStart;
				if((oldMode == newMode) && (oldStartTimes[activity] == newStart)) {
					LOGGER.debug("no changes possible for activity {}", activity);
				} else {
					LOGGER.debug("change to new values");
				}
				temporalConstraintsTracker.update(activity, newMode, newStart);
				resourceProfile.schedule(activity, newMode, newStart);
				nonRenewableResourceTracker.update(activity, newMode, newStart);
				considered.add(activity);
			}
		}

		Schedule newSchedule = new Schedule(newStartTimes, new WrappedResourceProfile(resourceProfile, newModes));
		MrcpspMaxSolution solution = new MrcpspMaxSolution(newSchedule, newModes);
		LOGGER.debug("search algorithm delivered solution {}", solution);
		return solution;
	}

	private Set<Integer> getUnscheduleActivities(int activity,
			Set<Integer> considered) {
		Set<Integer> toUnschedule = Sets.newHashSet(considered);
		return toUnschedule;
	}

	private int[] getEarliestPossibleModeAndStart(
			int activity,
			MmdjUTemporalConstraintsTracker temporalConstraintsTracker,
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
			LOGGER.debug("mode {} is feasible with respect to budget constraints", mode);
			IntInterval temporalWindow = temporalConstraintsTracker.getStartTimeWindow(activity, mode);
			if(!temporalWindow.isValidTimeWindow()) {
				LOGGER.debug("start time window not valid: {}", temporalWindow);
				continue;
			}
			LOGGER.debug("mode {} has temporal time window {}", mode, temporalWindow);
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
		if(earliestMode == -1) {
			return null;
		}
		int[] modeAndStart = new int[] {earliestMode, earliestStart};
		return modeAndStart;
	}
}
