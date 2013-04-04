package edu.bocmst.scheduling.mrcpspmax.candidate.priority;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.scheduler.CausalEligibilityTracker;
import edu.bocmst.utils.CollectionUtils;
import edu.bocmst.utils.RandomUtils;

public class RandomRandomKeysPriorityRuleCreator implements IPriorityRuleCreator{

	private static final Logger LOGGER = LoggerFactory
		.getLogger(RandomRandomKeysPriorityRuleCreator.class);

	private final IMrcpspMaxInstance problem;
	
	public RandomRandomKeysPriorityRuleCreator(IMrcpspMaxInstance problem) {
		this.problem = problem;
	}

	@Override
	public IPriorityRule createPriorityRuleForModeAssignment(
			IModeAssignment modeAssignment) {
		List<Integer> priorities = createPriorities(modeAssignment);
		IPriorityRule rule = new RandomKeysPriorityRule(priorities);
		return rule;
	}


	private List<Integer> createPriorities(
			IModeAssignment modeAssignment) {
		Set<Integer> openActivities = CollectionUtils.createSetOfIntegers(modeAssignment.getInstance().getActivityCount());
		List<Integer> priorities = CollectionUtils.createInitializedList(problem.getActivityCount(), -1);
		CausalEligibilityTracker causalTracker = CausalEligibilityTracker.createInstance(modeAssignment);
		for (int i = 0; i < problem.getActivityCount(); i++) {
			int nextActivity = getNextActivity(modeAssignment, causalTracker, openActivities);
			LOGGER.debug("chosen as next activity: {}", nextActivity);
			priorities.set(nextActivity, i);
			causalTracker.schedule(nextActivity);
			openActivities.remove(nextActivity);
		}
		return priorities;
	}

	private int getNextActivity(IModeAssignment modeAssignment,
			CausalEligibilityTracker causalTracker, Set<Integer> openActivities) {
		Set<Integer> eligibleSet = causalTracker.getEligibleActivities();
		if(eligibleSet.isEmpty()) {
//			if(modeAssignment.isTimeFeasible()) {
//				throw new RuntimeException("no eligible activity despite time feasibility");
//			}
			LOGGER.debug("no eligible activity - choose one randomly");
			eligibleSet = openActivities;
		}
		LOGGER.debug("eligible for iteration {}: {}", Arrays.toString(eligibleSet.toArray()));
		int nextActivity = RandomUtils.getRandomFromSet(eligibleSet);
		return nextActivity;
	}

}
