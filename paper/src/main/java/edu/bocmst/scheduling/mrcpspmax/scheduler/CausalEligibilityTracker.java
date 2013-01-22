package edu.bocmst.scheduling.mrcpspmax.scheduler;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IRcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.commons.GraphUtils;

class CausalEligibilityTracker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CausalEligibilityTracker.class);
	
	private final List<Set<Integer>> openCausalPredecessors;
	private final List<Set<Integer>> causalSuccessors;
	
	protected CausalEligibilityTracker(
			List<Set<Integer>> openCausalPredecessors,
			List<Set<Integer>> causalSuccessors) {
		this.openCausalPredecessors = openCausalPredecessors;
		this.causalSuccessors = causalSuccessors;
	}

	public Set<Integer> getEligibleActivities() {
		Set<Integer> eligible = Sets.newHashSet();
		for(int activity = 0; activity < openCausalPredecessors.size(); activity ++) {
			if(openCausalPredecessors.get(activity).size() == 0) {
				LOGGER.debug("found activity {} to be eligible", activity);
				eligible.add(activity);
			}
		}
		return eligible;
	}

	public void unschedule(Set<Integer> unschedule) {
		for(Integer activity : unschedule) {
			unschedule(activity);
		}
	}

	private void unschedule(Integer activity) {
		Set<Integer> successors = causalSuccessors.get(activity);
		for(Integer successor : successors) {
			Set<Integer> open = openCausalPredecessors.get(successor.intValue());
			open.add(activity);
		}
	}

	public void schedule(int activity) {
		Set<Integer> successors = causalSuccessors.get(activity);
		for(Integer successor : successors) {
			Set<Integer> open = openCausalPredecessors.get(successor.intValue());
			open.remove(activity);
		}
	}
	
	public static CausalEligibilityTracker createInstance(IModeAssignment candidate) {
		int[] modes = candidate.getModeArray();
		IRcpspMaxInstance rcpspMaxInstance = candidate.getInstance();
		List<Set<Integer>> causalSuccs = GraphUtils.getPositivePredecessors(modes, rcpspMaxInstance);
		List<Set<Integer>> open = deepCopy(causalSuccs);
		CausalEligibilityTracker instance = new CausalEligibilityTracker(open, causalSuccs);
		return instance;
	}

	private static List<Set<Integer>> deepCopy(List<Set<Integer>> list) {
		List<Set<Integer>> copy = Lists.newArrayListWithCapacity(list.size());
		for(Set<Integer> activities : list) {
			copy.add(Sets.newHashSet(activities));
		}
		return copy;
	}

}
