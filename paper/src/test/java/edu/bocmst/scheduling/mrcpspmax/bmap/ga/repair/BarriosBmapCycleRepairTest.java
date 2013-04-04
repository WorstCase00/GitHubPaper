package edu.bocmst.scheduling.mrcpspmax.bmap.ga.repair;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.bocmst.scheduling.mrcpspmax.TestConstants;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;

public class BarriosBmapCycleRepairTest {

	@Test
	public void testRepair() {
		BarriosModeAssignmentCycleRepair testee = new BarriosModeAssignmentCycleRepair(TestConstants.BARRIOS_INSTANCE);
		int[] modes = new int[] {1,2,2,3,3,1,3,1};
		IModeAssignment assignment = ModeAssignmentFactory.createInstance(modes, TestConstants.BARRIOS_INSTANCE);
		IModeAssignment result = testee.operate(assignment);
		
		assertTrue(result.isTimeFeasible());
		assertTrue(result.isResourceFeasible());
	}
}
