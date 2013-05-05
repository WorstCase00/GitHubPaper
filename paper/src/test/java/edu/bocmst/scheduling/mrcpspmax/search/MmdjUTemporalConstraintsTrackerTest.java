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
public class MmdjUTemporalConstraintsTrackerTest {

	private static final int BOUND = 1000;
	@Mock private IMrcpspMaxInstance instance;
	@Mock private IDirectedGraph network;
	
	@Before
	public void setupFixture() {
		when(instance.getAonNetwork()).thenReturn(network);
		when(instance.getActivityCount()).thenReturn(2);
		when(instance.getAonNetwork()).thenReturn(network);
		when(network.getPredecessors(1)).thenReturn(Sets.newHashSet(0));
		when(network.getSuccessors(1)).thenReturn(Collections.<Integer>emptySet());
		when(network.getSuccessors(0)).thenReturn(Sets.newHashSet(1));
		when(network.getPredecessors(0)).thenReturn(Collections.<Integer>emptySet());
	}
	
	@Test
	public void testOnePositivePredecessor() {
		int predStart = 123;
		int lag = 321;
		when(instance.getTimeLag(0, 1, 1, 1)).thenReturn(lag);
		MmdjUTemporalConstraintsTracker testee = new MmdjUTemporalConstraintsTracker(BOUND, instance);
		testee.update(0, 1, predStart);
		
		IntInterval result = testee.getStartTimeWindow(1, 1);
	
		assertEquals(predStart + lag, result.getLowerBound());
		assertEquals(BOUND, result.getUpperBound());
	}
	
	@Test
	public void testOnePositiveSuccessor() {
		int succStart = 123;
		int lag = 76;
		when(instance.getTimeLag(0, 1, 1, 1)).thenReturn(lag);
		MmdjUTemporalConstraintsTracker testee = new MmdjUTemporalConstraintsTracker(BOUND, instance);
		testee.update(1, 1, succStart);
		
		IntInterval result = testee.getStartTimeWindow(0, 1);
	
		assertEquals(0, result.getLowerBound());
		assertEquals(succStart - lag, result.getUpperBound());
	}
	
	@Test
	public void testOneNegativeSuccessor() {
		int succStart = 123;
		int lag = -76;
		when(instance.getTimeLag(0, 1, 1, 1)).thenReturn(lag);
		MmdjUTemporalConstraintsTracker testee = new MmdjUTemporalConstraintsTracker(BOUND, instance);
		testee.update(1, 1, succStart);
		
		IntInterval result = testee.getStartTimeWindow(0, 1);
	
		assertEquals(succStart + lag, result.getLowerBound());
		assertEquals(BOUND, result.getUpperBound());
	}
	
	@Test
	public void testOneNegativePredecessor() {
		int predStart = 123;
		int lag = -321;
		when(instance.getTimeLag(0, 1, 1, 1)).thenReturn(lag);
		MmdjUTemporalConstraintsTracker testee = new MmdjUTemporalConstraintsTracker(BOUND, instance);
		testee.update(0, 1, predStart);
		
		IntInterval result = testee.getStartTimeWindow(1, 1);
	
		assertEquals(predStart - lag, result.getUpperBound());
		assertEquals(0, result.getLowerBound());
	}
	
	@Test
	public void testAfterInitWithOnePredecessor() {
		when(network.getPredecessors(1)).thenReturn(Sets.newHashSet(0));
		when(network.getSuccessors(1)).thenReturn(Collections.<Integer>emptySet());
		MmdjUTemporalConstraintsTracker testee = new MmdjUTemporalConstraintsTracker(BOUND, instance);
	
		IntInterval result = testee.getStartTimeWindow(1, 1);
		
		assertEquals(new IntInterval(0, BOUND), result);
	}
	
	@Test
	public void testAfterInitWithOneSuccessor() {
		when(network.getPredecessors(1)).thenReturn(Collections.<Integer>emptySet());
		when(network.getSuccessors(1)).thenReturn(Sets.newHashSet(0));
		MmdjUTemporalConstraintsTracker testee = new MmdjUTemporalConstraintsTracker(BOUND, instance);
	
		IntInterval result = testee.getStartTimeWindow(1, 1);
		
		assertEquals(new IntInterval(0, BOUND), result);
	}
}