package edu.bocmst.scheduling.mrcpspmax.search;

import java.util.Arrays;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.utils.IntInterval;

public class MmdjUTemporalConstraintsTracker {


	private static final Logger LOGGER = LoggerFactory.getLogger(MmdjUTemporalConstraintsTracker.class);

	private final IMrcpspMaxInstance instance;
	private final int upperBound;
	
	// state
	private Set<Integer> considered;
	private int[] startTimes;
	private int[] modes;

	public MmdjUTemporalConstraintsTracker(
			int bound,
			IMrcpspMaxInstance instance) {
		this.instance = instance;
		this.upperBound = bound;
		this.considered = Sets.newHashSet();
		this.startTimes = new int[instance.getActivityCount()];
		this.modes = new int[instance.getActivityCount()];
	}

	public IntInterval getStartTimeWindow(int activity, int mode) {
		int startTime = getEarliestStartTime(activity, mode);
		int endTime = getLatestStartTime(activity, mode);
		IntInterval startTimeWindow = new IntInterval(startTime, endTime);
		return startTimeWindow;
	}

	private int getLatestStartTime(int activity, int mode) {
		LOGGER.debug("calculate latest start of activity {} with mode {}", activity, mode);
		int latestStart = upperBound;
		Set<Integer> successors = instance.getAonNetwork().getSuccessors(activity);
		LOGGER.debug("considered successors of activity {}", Arrays.toString(successors.toArray()));
		for(int successor : successors) {
			if(!considered.contains(successor)) {
				LOGGER.debug("activity {} is not considered", successor);
				continue;
			}
			int timeLag = instance.getTimeLag(activity, mode, successor, modes[successor]);
			LOGGER.debug("time lag with successor {}: {}", successor, timeLag);
			if(timeLag < 0) {
				LOGGER.debug("skipped because of negative time lag");
				continue;
			}
			int successorStart = startTimes[successor];
			LOGGER.debug("start time of successor {}: {}", successor, successorStart);
			latestStart = Math.min(latestStart, successorStart - timeLag);
			LOGGER.debug("latest start after considering activity {}: {}", activity, latestStart);
		}
		
		Set<Integer> predecessors = instance.getAonNetwork().getPredecessors(activity);
		LOGGER.debug("considered successors of activity {}", Arrays.toString(successors.toArray()));
		for(int predecessor : predecessors) {
			if(!considered.contains(predecessor)) {
				LOGGER.debug("activity {} is not considered", predecessor);
				continue;
			}
			int timeLag = instance.getTimeLag(predecessor, modes[predecessor], activity, mode);
			LOGGER.debug("time lag with successor {}: {}", predecessor, timeLag);
			if(timeLag >= 0) {
				LOGGER.debug("skipped because of positive time lag");
				continue;
			}
			int predecessorStart = startTimes[predecessor];
			LOGGER.debug("start time of predecessor {}: {}", predecessor, predecessorStart);
			latestStart = Math.min(latestStart, predecessorStart - timeLag);
			LOGGER.debug("latest start after considering activity {}: {}", activity, latestStart);
		}
		return latestStart;
	}

	private int getEarliestStartTime(int activity, int mode) {
		LOGGER.debug("calculate earliest start of activity {} with mode {}", activity, mode);
		int earliestStart = 0;
		Set<Integer> predecessors = instance.getAonNetwork().getPredecessors(activity);
		LOGGER.debug("considered predecessors of activity {}", Arrays.toString(predecessors.toArray()));
		for(int predecessor : predecessors) {
			if(!considered.contains(predecessor)) {
				LOGGER.debug("activity {} is not considered", predecessor);
				continue;
			}
			int timeLag = instance.getTimeLag(predecessor, modes[predecessor], activity, mode);
			LOGGER.debug("time lag with predecessor {}: {}", predecessor, timeLag);
			if(timeLag < 0) {
				LOGGER.debug("skipped because of negative timelag");
				continue;
			}
			int predecessorStart = startTimes[predecessor];
			LOGGER.debug("start time of predecessor {}: {}", predecessor, predecessorStart);
			earliestStart = Math.max(earliestStart, predecessorStart + timeLag);
			LOGGER.debug("earliest start after considering activity {}: {}", activity, earliestStart);
		}
		
		Set<Integer> successors = instance.getAonNetwork().getSuccessors(activity);
		LOGGER.debug("considered successors of activity {}", Arrays.toString(successors.toArray()));
		for(int successor : successors) {
			if(!considered.contains(successor)) {
				LOGGER.debug("activity {} is not considered", successor);
				continue;
			}
			int timeLag = instance.getTimeLag(activity, mode, successor, modes[successor]);
			LOGGER.debug("time lag with successor {}: {}", successor, timeLag);
			if(timeLag >= 0) {
				LOGGER.debug("skipped because of positive time lag");
				continue;
			}
			int successorStart = startTimes[successor];
			LOGGER.debug("start time of predecessor {}: {}", successor, successorStart);
			earliestStart = Math.max(earliestStart, successorStart + timeLag);
			LOGGER.debug("earliest start after considering activity {}: {}", activity, earliestStart);
		}
		return earliestStart;
	}

	public void update(int activity, int newMode, int newStart) {
		considered.add(activity);
		modes[activity] = newMode;
		startTimes[activity] = newStart;
	}

}
