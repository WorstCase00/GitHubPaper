package edu.bocmst.scheduling.mrcpspmax.scheduler;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.TestConstants;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;
import edu.bocmst.utils.IntInterval;

public class TemporalConstraintsTrackerBarriosTest {

	private static final int[] MODES = new int[] {1,2,1,3,1,1,3,1};
	
	@Test
	public void testBoundInitializationCorrect() throws Exception {
		IModeAssignment candidate = ModeAssignmentFactory.createInstance(MODES, TestConstants.THESIS_INSTANCE);
		TemporalConstraintsTracker testee = TemporalConstraintsTracker.createInstance(candidate);
		
//		assertEquals(new StartTimeWindow(0, 0), testee.getStartTimeWindow(0));
//		assertEquals(new StartTimeWindow(0, 2), testee.getStartTimeWindow(1));
//		assertEquals(new StartTimeWindow(0, 0), testee.getStartTimeWindow(2));
//		assertEquals(new StartTimeWindow(1, 3), testee.getStartTimeWindow(3));
//		assertEquals(new StartTimeWindow(4, 4), testee.getStartTimeWindow(4));
//		assertEquals(new StartTimeWindow(5, 7), testee.getStartTimeWindow(5));
//		assertEquals(new StartTimeWindow(5, 5), testee.getStartTimeWindow(6));
//		assertEquals(new StartTimeWindow(9, 9), testee.getStartTimeWindow(7));
		
		assertEquals(new IntInterval(0, Integer.MAX_VALUE), testee.getStartTimeWindow(0));
		assertEquals(new IntInterval(0, Integer.MAX_VALUE), testee.getStartTimeWindow(1));
		assertEquals(new IntInterval(0, Integer.MAX_VALUE), testee.getStartTimeWindow(2));
		assertEquals(new IntInterval(1, Integer.MAX_VALUE), testee.getStartTimeWindow(3));
		assertEquals(new IntInterval(4, Integer.MAX_VALUE), testee.getStartTimeWindow(4));
		assertEquals(new IntInterval(5, Integer.MAX_VALUE), testee.getStartTimeWindow(5));
		assertEquals(new IntInterval(5, Integer.MAX_VALUE), testee.getStartTimeWindow(6));
		assertEquals(new IntInterval(9, Integer.MAX_VALUE), testee.getStartTimeWindow(7));
	}
	
	@Test
	public void testBarriosReschedulingExample() throws Exception {
		IModeAssignment candidate = ModeAssignmentFactory.createInstance(MODES, TestConstants.THESIS_INSTANCE);
		TemporalConstraintsTracker testee = TemporalConstraintsTracker.createInstance(candidate);
		testee.schedule(0, 0);
		testee.schedule(1, 0);
		testee.schedule(3, 6);
		assertEquals(new IntInterval(10, 10), testee.getStartTimeWindow(5));
		testee.schedule(2, 6);
		assertEquals(new IntInterval(10, 10), testee.getStartTimeWindow(4));
		testee.schedule(4, 10);
		int[] startTimes = new int[] {0, 0, 6, 6, 10, -1, -1 -1};
		Set<Integer> unschedule = testee.unschedule(5, 2, startTimes);
		assertEquals(2, unschedule.size());
		assertTrue(unschedule.containsAll(Lists.newArrayList(4,3)));
	}
}
