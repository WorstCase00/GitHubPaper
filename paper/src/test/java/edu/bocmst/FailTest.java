package edu.bocmst;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import edu.bocmst.graph.IDirectedEdge;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.IPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;
import edu.bocmst.scheduling.mrcpspmax.commons.GraphUtils;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.loader.InstanceLoader;
import edu.bocmst.scheduling.mrcpspmax.scheduler.CausalEligibilityTracker;
import edu.bocmst.scheduling.mrcpspmax.scheduler.SerialRcpspMaxScheduler;


public class FailTest {

	private static final int[] modes = new int[] {
		1, 5, 4, 3, 5, 3, 1, 1, 1, 3, 1, 4, 1, 4, 5, 1, 3, 3, 4, 2, 4, 3, 4, 3, 2, 2, 4, 5, 3, 3, 5, 5, 2, 4, 3, 2, 4, 4, 3, 4, 3, 2, 3, 2, 2, 2, 4, 5, 1, 3, 4, 3, 5, 3, 1, 1, 4, 5, 4, 2, 2, 4, 2, 1, 5, 3, 4, 4, 2, 1, 2, 4, 2, 4, 1, 2, 4, 1, 4, 1, 4, 4, 3, 4, 5, 4, 3, 5, 3, 4, 2, 4, 2, 1, 4, 3, 3, 1, 1, 2, 1, 1};

	@Test
	public void test() throws IOException {
		IMrcpspMaxInstance instance = InstanceLoader.loadInstance("instances/100/psp216.sch");
		IModeAssignment candidate = ModeAssignmentFactory.createInstance(modes, instance);
		assertTrue(candidate.isTimeFeasible());
		assertTrue(candidate.isResourceFeasible());
		
		Set<Set<IDirectedEdge>> posCycles = GraphUtils.getPositiveCycles(modes, instance);
		assertTrue(posCycles.isEmpty());
		CausalEligibilityTracker causalEligibilityTracker =CausalEligibilityTracker.createInstance(candidate);
		for (int i = 0; i < instance.getActivityCount(); i++) {
			Set<Integer> eligible = causalEligibilityTracker.getEligibleActivities();
			if(eligible.isEmpty()) {
				fail();
			}
			assertFalse(eligible.isEmpty());
			causalEligibilityTracker.schedule(eligible.iterator().next());
		}
		SerialRcpspMaxScheduler scheduler = new SerialRcpspMaxScheduler();
		Schedule schedule = scheduler.createSchedule(candidate, new IPriorityRule() {
			
			@Override
			public int getNextActivityFromEligibleSet(Set<Integer> eligibleActivities) {
				return eligibleActivities.iterator().next();
			}
			
			@Override
			public ImmutableList<Integer> getIntegerListRepresentation() {
				return null;
			}

			@Override
			public int compare(int activity1, int activity2) {
				return 0;
			}
		});
		MrcpspMaxUtils.isScheduleTimeValid(schedule.getStartTimes(), modes, instance);
	}
}
