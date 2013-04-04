package edu.bocmst.scheduling.mrcpspmax.scheduler;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IRcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.commons.GraphUtils;

public class CausalEligibilityTracker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CausalEligibilityTracker.class);
	
	private final List<Set<Integer>> openCausalPredecessors;
	private final List<Set<Integer>> causalSuccessors;
	private final Set<Integer> scheduled;
	
	protected CausalEligibilityTracker(
			List<Set<Integer>> openCausalPredecessors,
			List<Set<Integer>> causalSuccessors) {
		this.openCausalPredecessors = openCausalPredecessors;
		this.causalSuccessors = causalSuccessors;
		scheduled = Sets.newHashSet();
	}

	public Set<Integer> getEligibleActivities() {
		Set<Integer> eligible = Sets.newHashSet();
		for(int activity = 0; activity < openCausalPredecessors.size(); activity ++) {
			if(scheduled.contains(activity)) {
				continue;
			}
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
		scheduled.remove(activity);
	}

	public void schedule(int activity) {
		Set<Integer> successors = causalSuccessors.get(activity);
		for(Integer successor : successors) {
			Set<Integer> open = openCausalPredecessors.get(successor.intValue());
			open.remove(activity);
		}
		scheduled.add(activity);
	}
	
	public static CausalEligibilityTracker createInstance(IModeAssignment candidate) {
		int[] modes = candidate.getModeArray();
		IRcpspMaxInstance rcpspMaxInstance = candidate.getInstance();
		List<Set<Integer>> openCausalPredecessors = GraphUtils.getStrictlyPositivePredecessors(modes, rcpspMaxInstance);
		List<Set<Integer>> causalSuccs = GraphUtils.getStrictlyPositiveSuccessors(modes, rcpspMaxInstance);
		CausalEligibilityTracker instance = new CausalEligibilityTracker(openCausalPredecessors, causalSuccs);
		return instance;
	}

}
