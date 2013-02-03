package edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment;

import com.google.common.primitives.Ints;

import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public abstract class ModeAssignmentFactory {

	private ModeAssignmentFactory() {}
	
	public static IModeAssignment createInstance(
			int[] modes,
			IMrcpspMaxInstance instance) {
		int[] remaining = MrcpspMaxUtils.calculateResourceRemainingVector(modes, instance);
		boolean resourceFeasible = (Ints.min(remaining) >= 0);
		
		boolean timeFeasible = MrcpspMaxUtils.isModeAssignmentTimeValid(modes, instance);
		
		IRcpspMaxInstance rcpspsMaxInstance = RcpspMaxInstance.createInstance(modes, instance);
		
		return new ModeAssignment(modes, rcpspsMaxInstance, remaining, resourceFeasible, timeFeasible);
	}

	
}
