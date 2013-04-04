package edu.bocmst.scheduling.mrcpspmax.scheduler;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.TestConstants;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.ActivityListPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.IPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;

@RunWith(MockitoJUnitRunner.class)
public class SerialRcpspMaxSchedulerIntegrationTest {

	private static final int[] EXPECTED_START_TIMES = new int[] {0,0,6,8,10,12,14,18};
	private static final int[] MODES = new int[] {1,2,1,3,1,1,3,1};
	private static final ImmutableList<Integer> LIST = ImmutableList.copyOf(Lists.newArrayList(0,1,3,2,4,5,6,7));
	
	private IModeAssignment candidate;
	private IPriorityRule priorityRule;

	@Test
	public void integrationTestBarriosPaperExample() throws Exception {
		priorityRule = new ActivityListPriorityRule(LIST);
		candidate = ModeAssignmentFactory.createInstance(MODES, TestConstants.BARRIOS_INSTANCE);
		SerialRcpspMaxScheduler testee = new SerialRcpspMaxScheduler();
		Schedule result = testee.createSchedule(candidate, priorityRule);
		int[] startTime = result.getStartTimes();
		
		assertArrayEquals(EXPECTED_START_TIMES, startTime);
	}
}