package edu.bocmst.scheduling.mrcpspmax.search;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;

import edu.bocmst.graph.IDirectedGraph;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.utils.IntInterval;

@RunWith(MockitoJUnitRunner.class)
public class MmdjMaxTemporalConstraintsTrackerTest {

	private static final int BOUND = 100;
	@Mock private IMrcpspMaxInstance instance;
	@Mock private IDirectedGraph network;
	
	@Before
	public void setupFixture() {
		when(instance.getAonNetwork()).thenReturn(network);
		when(network.getPredecessors(1)).thenReturn(Sets.newHashSet(0));
		when(network.getSuccessors(1)).thenReturn(Collections.<Integer>emptySet());
		when(network.getSuccessors(0)).thenReturn(Sets.newHashSet(1));
		when(network.getPredecessors(0)).thenReturn(Collections.<Integer>emptySet());
	}

	@Test
	public void testTimeWindowAfterPositivePredecessorWasScheduled() {
		int[] modes = new int[] {1, 1, 0};
		int predStart = 123;
		int[] startTimes = new int[] {predStart, -1, BOUND};
		int lag = 321;
		when(instance.getTimeLag(0, 1, 1, 1)).thenReturn(lag);
		MmdjMaxTemporalConstraintsTracker testee = new MmdjMaxTemporalConstraintsTracker(modes, startTimes, instance);
		
		IntInterval result = testee.getStartTimeWindow(1, 1);
	
		assertEquals(predStart + lag, result.getLowerBound());
		assertEquals(BOUND, result.getUpperBound());
	}
	
	@Test
	public void testTimeWindowAfterPositiveSuccessorWasScheduled() {
		int[] modes = new int[] {1, 1, 0};
		int succStart = 123;
		int[] startTimes = new int[] {-1, succStart, BOUND};
		int lag = 76;
		when(instance.getTimeLag(0, 1, 1, 1)).thenReturn(lag);
		MmdjMaxTemporalConstraintsTracker testee = new MmdjMaxTemporalConstraintsTracker(modes, startTimes, instance);
		
		IntInterval result = testee.getStartTimeWindow(0, 1);
	
		assertEquals(0, result.getLowerBound());
		assertEquals(succStart - lag, result.getUpperBound());
	}
	
	@Test
	public void testTimeWindowAfterNegativeSuccessorWasScheduled() {
		int[] modes = new int[] {1, 1, 0};
		int succStart = 123;
		int[] startTimes = new int[] {-1, succStart, BOUND};
		int lag = -76;
		when(instance.getTimeLag(0, 1, 1, 1)).thenReturn(lag);
		MmdjMaxTemporalConstraintsTracker testee = new MmdjMaxTemporalConstraintsTracker(modes, startTimes, instance);
		
		IntInterval result = testee.getStartTimeWindow(0, 1);
	
		assertEquals(succStart + lag, result.getLowerBound());
		assertEquals(BOUND, result.getUpperBound());
	}
	
	@Test
	public void testTimeWindowAfterNegativePredecessorWasScheduled() {
		int[] modes = new int[] {1, 1, 0};
		int predStart = 55;
		int[] startTimes = new int[] {predStart, -1, BOUND};
		int lag = -21;
		when(instance.getTimeLag(0, 1, 1, 1)).thenReturn(lag);
		MmdjMaxTemporalConstraintsTracker testee = new MmdjMaxTemporalConstraintsTracker(modes, startTimes, instance);
		
		IntInterval result = testee.getStartTimeWindow(1, 1);
	
		assertEquals(predStart - lag, result.getUpperBound());
		assertEquals(0, result.getLowerBound());
	}
}