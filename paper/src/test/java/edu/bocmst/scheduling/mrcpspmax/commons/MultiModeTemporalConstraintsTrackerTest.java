package edu.bocmst.scheduling.mrcpspmax.commons;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.bocmst.scheduling.mrcpspmax.TestConstants;
import edu.bocmst.scheduling.mrcpspmax.search.MmdjMaxTemporalConstraintsTracker;
import edu.bocmst.utils.IntInterval;

public class MultiModeTemporalConstraintsTrackerTest {

	@Test
	public void testExampleWithPositiveEdges() {
		int[] modes = new int[] {1,1,1,1,1,1,1,1,1};
		int[] startTimes = new int[] {0,0,0,3,4,4,4,6};
		
		MmdjMaxTemporalConstraintsTracker testee = new MmdjMaxTemporalConstraintsTracker(modes, startTimes, TestConstants.BARRIOS_INSTANCE);
		
		IntInterval result = testee.getStartTimeWindow(1, 1);
		assertEquals(0, result.getLowerBound());
		assertEquals(0, result.getUpperBound());
		
		result = testee.getStartTimeWindow(1, 2);
		assertEquals(0, result.getLowerBound());
		assertEquals(-3, result.getUpperBound());
		
		result = testee.getStartTimeWindow(1, 3);
		assertEquals(0, result.getLowerBound());
		assertEquals(2, result.getUpperBound());
	}
	
	@Test
	public void testExampleWithNegativeTimeLagsToPredecessor() {
		int[] modes = new int[] {1,1,1,1,1,1,1,1,1};
		int[] startTimes = new int[] {0,0,0,3,4,4,4,6};
		
		MmdjMaxTemporalConstraintsTracker testee = new MmdjMaxTemporalConstraintsTracker(modes, startTimes, TestConstants.BARRIOS_INSTANCE);
		
		IntInterval result = testee.getStartTimeWindow(3, 1);
		assertEquals(3, result.getLowerBound());
		assertEquals(3, result.getUpperBound());
	}
	
	@Test
	public void testExampleWithNegativeTimeLagsToSuccessor() {
		int[] modes = new int[] {1,1,1,1,1,1,1,1,1};
		int[] startTimes = new int[] {0,0,0,3,4,4,4,6};
		
		MmdjMaxTemporalConstraintsTracker testee = new MmdjMaxTemporalConstraintsTracker(modes, startTimes, TestConstants.BARRIOS_INSTANCE);
		
		IntInterval result = testee.getStartTimeWindow(5, 1);
		assertEquals(4, result.getLowerBound());
		assertEquals(4, result.getUpperBound());
	}
}
