package edu.bocmst.scheduling.mrcpspmax.commons;

import java.io.IOException;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.bocmst.scheduling.mrcpspmax.TestConstants;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.loader.InstanceLoader;

public class SolutionValidatorTest {
	
	@Test
	public void testTimeFeasibilityCheckFail() throws IOException {
		
		int[] modes = new int[] {1, 2, 2, 3, 3, 1, 3, 1};
		IMrcpspMaxInstance instance = InstanceLoader.loadInstance(TestConstants.BARRIOS_PATH);
		boolean result = MrcpspMaxUtils.isModeAssignmentTimeValid(modes, instance);
		
		assertFalse(result);
	}
	
	@Test
	public void testTimeFeasibilityCheckPass() throws IOException {
		
		int[] modes = new int[] {1,2,1,1,1,3,3,1};
		IMrcpspMaxInstance instance = InstanceLoader.loadInstance(TestConstants.BARRIOS_PATH);
		boolean result = MrcpspMaxUtils.isModeAssignmentTimeValid(modes, instance);
		
		assertTrue(result);
	}
	
	@Test
	public void testResourceFeasibilityCheckFail() throws IOException {
		
		int[] modes = new int[] {1,2,1,1,1,3,3,1};
		IMrcpspMaxInstance instance = InstanceLoader.loadInstance(TestConstants.BARRIOS_PATH);
		boolean result = MrcpspMaxUtils.isModeAssignmentResourceValid(modes, instance);
		
		assertFalse(result);
	}
	
	@Test
	public void testResourceFeasibilityCheckOk() throws IOException {
		
		int[] modes = new int[] {1, 2, 2, 3, 3, 1, 3, 1};
		IMrcpspMaxInstance instance = InstanceLoader.loadInstance(TestConstants.BARRIOS_PATH);
		boolean result = MrcpspMaxUtils.isModeAssignmentResourceValid(modes, instance);
		
		assertTrue(result);
	}
}
