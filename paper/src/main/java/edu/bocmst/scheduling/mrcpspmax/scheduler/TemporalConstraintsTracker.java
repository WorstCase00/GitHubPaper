package edu.bocmst.scheduling.mrcpspmax.scheduler;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IRcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.commons.GraphUtils;
import edu.bocmst.scheduling.mrcpspmax.commons.IntArrays;
import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetwork;

public class TemporalConstraintsTracker {

	

	private static final Logger LOGGER = LoggerFactory.getLogger(TemporalConstraintsTracker.class);
	
	private final int[][] pathMatrix;
	private final IAonNetwork aonNetwork;
	private final int[] lowerBounds;
	private final int[] upperBounds;
	private final Set<Integer> scheduled;

	TemporalConstraintsTracker(int[][] pathMatrix,
			IAonNetwork aonNetwork, int[] lowerBounds, int[] upperBounds,
			IRcpspMaxInstance instance, Set<Integer> scheduled) {
		this.pathMatrix = pathMatrix;
		this.aonNetwork = aonNetwork;
		this.lowerBounds = lowerBounds;
		this.upperBounds = upperBounds;
		this.scheduled = scheduled;
	}

	public StartTimeWindow getStartTimeWindow(int activity) {
		int earliestStart = lowerBounds[activity];
		Integer latestStart = upperBounds[activity];
		StartTimeWindow timeWindow = new StartTimeWindow(earliestStart, latestStart);
		return timeWindow;
	}

	public void schedule(int activity, int start) {
		Set<Integer> successors = aonNetwork.getSuccessors(activity);
		scheduled.add(activity);
		for(int successor : successors) {
			int actualLowerBound = lowerBounds[successor];
			int timeLag = pathMatrix[activity][successor];
			int newLowerBound = start + timeLag;
			if(newLowerBound > actualLowerBound) {
				lowerBounds[successor] = newLowerBound;
			}
		}
		for(int predecessor : aonNetwork.getPredecessors(activity)) {
			int actualUpperBound = upperBounds[predecessor];
			int timeLag = pathMatrix[predecessor][activity];
			int newUpperBound = start - timeLag;
			if(newUpperBound < actualUpperBound) {
				upperBounds[predecessor] = newUpperBound;
			}
		}
	}

	public Set<Integer> unschedule(int activity, int timeSpan, int[] startTimes) {
		Set<Integer> directUnschedule = Sets.newHashSet();
		for(int scheduledActivity : scheduled) {
			int startTime = startTimes[scheduledActivity];
			int timeLag = pathMatrix[activity][scheduledActivity];
			if((startTime - timeLag) == upperBounds[activity]) {
				directUnschedule.add(scheduledActivity);
			}
		}
		for(int unscheduleActivity : directUnschedule) {
			lowerBounds[unscheduleActivity] = startTimes[unscheduleActivity] + timeSpan;
		}
		scheduled.removeAll(directUnschedule);
		
		Set<Integer> indirectUnschedule = Sets.newHashSet();
		int minStartTimeInUnscheduled = getMinStartTime(directUnschedule, startTimes);
		for(int scheduledActivity : scheduled) {
			if(startTimes[scheduledActivity] > minStartTimeInUnscheduled) {
				indirectUnschedule.add(scheduledActivity);
			}
		}
		scheduled.removeAll(indirectUnschedule);
		
		for(int openActivity = 0; openActivity < lowerBounds.length; openActivity++) {
			if(scheduled.contains(openActivity)) {
				continue;
			}
			int maxUnscheduledDistance = getMaxDistance(openActivity, directUnschedule);
			lowerBounds[openActivity] = Math.max(pathMatrix[0][openActivity], maxUnscheduledDistance);
			if(pathMatrix[openActivity][0] != GraphUtils.NO_EDGE) {
				upperBounds[openActivity] = -pathMatrix[openActivity][0];
			} else {
				upperBounds[openActivity] = Integer.MAX_VALUE;
			}
			for(int scheduledActivity : scheduled) {

				if(pathMatrix[scheduledActivity][openActivity] != GraphUtils.NO_EDGE) {
				lowerBounds[openActivity] = Math.max(
						lowerBounds[openActivity], 
						startTimes[scheduledActivity] + pathMatrix[scheduledActivity][openActivity]);
				}
				if(pathMatrix[openActivity][scheduledActivity] != GraphUtils.NO_EDGE) {
					upperBounds[openActivity] = Math.min(
							upperBounds[openActivity], 
							startTimes[scheduledActivity] - pathMatrix[openActivity][scheduledActivity]);
				}
			
			}
			LOGGER.debug("modefied time window for activity {}: {}", 
					openActivity, new StartTimeWindow(lowerBounds[openActivity], upperBounds[openActivity]));
		}
		
		Set<Integer> allUnscheduled = Sets.union(directUnschedule, indirectUnschedule);
		return allUnscheduled;
	}

	private int getMaxDistance(int openActivity, Set<Integer> directUnschedule) {
		
		int maxDistance = Integer.MIN_VALUE;
		for(int unscheduleActivity : directUnschedule) {
			int distance = lowerBounds[unscheduleActivity] + pathMatrix[unscheduleActivity][openActivity];
			if(distance > maxDistance) {
				maxDistance = distance;
			}
		}
		return maxDistance;
	}

	private int getMinStartTime(Set<Integer> toUnschedule, int[] startTimes) {
		int minStartTime = Integer.MAX_VALUE;
		for(int activity : toUnschedule) {
			if(startTimes[activity] < minStartTime) {
				minStartTime = startTimes[activity];
			}
		}
		return minStartTime;
	}

	public static TemporalConstraintsTracker createInstance(IModeAssignment candidate) {
		int[][] paths = candidate.getInstance().getPathMatrix();
		IAonNetwork network = candidate.getInstance().getAonNetwork();
		int[] lbs = paths[0].clone();
		lbs[0] = 0;
		int[] ubs = initUpperBounds(candidate.getInstance().getPathMatrix());
		IRcpspMaxInstance problem = candidate.getInstance();
		Set<Integer> done = Sets.newHashSet();
		TemporalConstraintsTracker tracker = new TemporalConstraintsTracker(paths, network, lbs, ubs, problem, done);
		return tracker;
	}

	private static int[] initUpperBounds(int[][] paths) {
		int[] distancesToEnd = IntArrays.getColumn(paths.length - 1, paths);
//		int makespan = paths[0][paths.length - 1];
		int[] upperBounds = new int[distancesToEnd.length];
		for (int i = 0; i < distancesToEnd.length; i++) {
//			upperBounds[i] = makespan - distancesToEnd[i];
			upperBounds[i] = Integer.MAX_VALUE;//makespan - distancesToEnd[i];
		}
//		upperBounds[upperBounds.length -1] = makespan;
		return upperBounds;
	}

}
