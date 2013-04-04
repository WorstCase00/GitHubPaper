package edu.bocmst.scheduling.mrcpspmax.search;

import java.util.Arrays;
import java.util.Set;

import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.utils.IntInterval;

@NotThreadSafe
public class MultiModeTemporalConstraintsTracker {

	private static final Logger LOGGER = LoggerFactory
		.getLogger(MultiModeTemporalConstraintsTracker.class);

	private final IMrcpspMaxInstance instance;
	
	// state
	private int[] modes;
	private int[] startTimes;
	
	public MultiModeTemporalConstraintsTracker(
			int[] modes,
			int[] startTimes,
			IMrcpspMaxInstance instance) {
		this.instance = instance;
		this.modes = modes;
		this.startTimes = startTimes;
	}

	public void update(
			int activity, 
			int newMode, 
			int newStart) {
		LOGGER.debug("set mode of activity {} to {}", activity, newMode);
		modes[activity] = newMode;
		LOGGER.debug("set start time of activity {} to {}", activity, newStart);
		startTimes[activity] = newStart;
	}

	public IntInterval getStartTimeWindow(int activity, int mode) {
		LOGGER.debug("get start time window of activity {} with mode {}", activity, mode);
		int latest = getLatestStart(activity, mode);
		LOGGER.debug("calculated earliest start of activity {}: {}", activity, latest);
		int earliest = getEarliestStart(activity, mode);
		LOGGER.debug("calculated latest start of activity {}: {}", activity, earliest);
		IntInterval timeWindow = new IntInterval(earliest, latest);
		return timeWindow;
	}

	private int getEarliestStart(int activity, int mode) {
		LOGGER.debug("calculate earliest start of activity {} with mode {}", activity, mode);
		int earliestStart = 0;
		Set<Integer> predecessors = instance.getAonNetwork().getPredecessors(activity);
		LOGGER.debug("considered predecessors of activity {}", Arrays.toString(predecessors.toArray()));
		for(int predecessor : predecessors) {
			int timeLag = instance.getTimeLag(predecessor, modes[predecessor], activity, mode);
			LOGGER.debug("time lag with predecessor {}: {}", predecessor, timeLag);
			int predecessorStart = startTimes[predecessor];
			LOGGER.debug("start time of predecessor {}: {}", predecessor, predecessorStart);
			earliestStart = Math.max(earliestStart, predecessorStart + timeLag);
			LOGGER.debug("earliest start after considering activity {}: {}", activity, earliestStart);
		}
		return earliestStart;
	}

	private int getLatestStart(int activity, int mode) {
		LOGGER.debug("calculate latest start of activity {} with mode {}", activity, mode);
		int latestStart = Integer.MAX_VALUE;
		Set<Integer> successors = instance.getAonNetwork().getSuccessors(activity);
		LOGGER.debug("considered successors of activity {}", Arrays.toString(successors.toArray()));
		for(int successor : successors) {
			int timeLag = instance.getTimeLag(activity, mode, successor, modes[successor]);
			LOGGER.debug("time lag with successor {}: {}", successor, timeLag);
			int successorStart = startTimes[successor];
			LOGGER.debug("start time of successor {}: {}", successor, successorStart);
			latestStart = Math.min(latestStart, successorStart - timeLag);
			LOGGER.debug("latest start after considering activity {}: {}", activity, latestStart);
		}
		return latestStart;
	}
}
