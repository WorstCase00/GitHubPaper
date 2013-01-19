package edu.bocmst.scheduling.mrcpspmax.bmap.ga.repair;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.bocmst.scheduling.mrcpspmax.TestConstants;
import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.ModeAssignment;

public class BarriosBmapCycleRepairTest {

	@Test
	public void testRepair() {
		BarriosBmapCycleRepair testee = new BarriosBmapCycleRepair(TestConstants.THESIS_INSTANCE);
		int[] modes = new int[] {1,2,2,3,3,1,3,1};
		IModeAssignment assignment = ModeAssignment.createInstance(modes, TestConstants.THESIS_INSTANCE);
		IModeAssignment result = testee.operate(assignment);
		
		assertTrue(result.isTimeFeasible());
		assertTrue(result.isResourceFeasible());
	}
}
