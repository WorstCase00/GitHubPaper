package edu.bocmst.scheduling.mrcpspmax.scheduler;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;

import edu.bocmst.graph.IDirectedGraph;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IRcpspMaxInstance;
import edu.bocmst.utils.IntInterval;

@RunWith(MockitoJUnitRunner.class)
public class TemporalConstraintsTrackerTest {

	private int[][] pathMatrix = new int[2][2];
	private int[] lowerBounds = new int[2];
	private int[] upperBounds = new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE};
	
	@Mock private IDirectedGraph aonNetwork;
	@Mock private IRcpspMaxInstance instance;
	
	
	@Before
	public void setupFixture() {
		when(instance.getAonNetwork()).thenReturn(aonNetwork);
		when(aonNetwork.getPredecessors(0)).thenReturn(Collections.<Integer>emptySet());
		when(aonNetwork.getSuccessors(0)).thenReturn(Sets.newHashSet(1));
		when(aonNetwork.getPredecessors(1)).thenReturn(Sets.newHashSet(0));
		when(aonNetwork.getSuccessors(1)).thenReturn(Collections.<Integer>emptySet());
	}

	@Test
	public void testStartTimeWindowAfterPositivePredecessorWasScheduled() throws Exception {
		Set<Integer> scheduled = Sets.newHashSet();
		
		int lag = 123;
		pathMatrix[0][1] = lag;
		RigidTemporalConstraintsTracker testee = new RigidTemporalConstraintsTracker(
				pathMatrix, 
				aonNetwork, 
				lowerBounds, 
				upperBounds, 
				instance, 
				scheduled);
		int start = 234;
		testee.schedule(0, start);
		IntInterval result = testee.getStartTimeWindow(1);
		
		assertEquals(start + lag, result.getLowerBound());
		assertEquals(Integer.MAX_VALUE, result.getUpperBound());
	}
	
	@Test
	public void testStartTimeWindowAfterPositiveSuccessorWasScheduled() throws Exception {
		Set<Integer> scheduled = Sets.newHashSet();
		
		int lag = 123;
		pathMatrix[0][1] = lag;
		RigidTemporalConstraintsTracker testee = new RigidTemporalConstraintsTracker(
				pathMatrix, 
				aonNetwork, 
				lowerBounds, 
				upperBounds, 
				instance, 
				scheduled);
		int start = 234;
		testee.schedule(1, start);
		IntInterval result = testee.getStartTimeWindow(0);
		
		assertEquals(0, result.getLowerBound());
		assertEquals(start - lag, result.getUpperBound());
	}
	
	@Test 
	public void testTimeWindowAfterNegativePredecessorWasScheduled() throws Exception {
		Set<Integer> scheduled = Sets.newHashSet();
		
		int lag = -123;
		pathMatrix[0][1] = lag;
		RigidTemporalConstraintsTracker testee = new RigidTemporalConstraintsTracker(
				pathMatrix, 
				aonNetwork, 
				lowerBounds, 
				upperBounds, 
				instance, 
				scheduled);
		int start = 234;
		testee.schedule(0, start);
		IntInterval result = testee.getStartTimeWindow(1);
		
		assertEquals(start + lag, result.getLowerBound());
		assertEquals(Integer.MAX_VALUE, result.getUpperBound());
	}
	
	@Test
	public void testStartTimewindowAfterNegativeSuccessorWasScheduled() throws Exception {
		Set<Integer> scheduled = Sets.newHashSet();
		
		int lag = -123;
		pathMatrix[0][1] = lag;
		RigidTemporalConstraintsTracker testee = new RigidTemporalConstraintsTracker(
				pathMatrix, 
				aonNetwork, 
				lowerBounds, 
				upperBounds, 
				instance, 
				scheduled);
		int start = 234;
		testee.schedule(1, start);
		IntInterval result = testee.getStartTimeWindow(0);
		
		assertEquals(0, result.getLowerBound());
		assertEquals(start - lag, result.getUpperBound());
	}
}