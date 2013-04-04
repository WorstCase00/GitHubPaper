package edu.bocmst.scheduling.mrcpspmax.scheduler;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import edu.bocmst.scheduling.mrcpspmax.TestConstants;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;

@RunWith(MockitoJUnitRunner.class)
public class CausalEligibilityTrackerTest {

	@Test
	public void testInitializationNotOnlyZeroEligible() {
		int[] modes = TestConstants.VALID_MODES_BARRIOS_INSTANCE;
		IModeAssignment candidate = ModeAssignmentFactory.createInstance(modes, TestConstants.BARRIOS_INSTANCE);
		CausalEligibilityTracker testee = CausalEligibilityTracker.createInstance(candidate);
		
		Set<Integer> result = testee.getEligibleActivities();
		
		assertEquals(3, result.size());
		assertTrue(result.contains(0));
		assertTrue(result.contains(1));
		assertTrue(result.contains(2));
	}
}