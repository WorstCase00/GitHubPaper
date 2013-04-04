package edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.primitives.Ints;

import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public abstract class ModeAssignmentFactory {

	private static final Logger LOGGER = LoggerFactory
		.getLogger(ModeAssignmentFactory.class);

	private ModeAssignmentFactory() {}
	
	public static IModeAssignment createInstance(
			int[] modes,
			IMrcpspMaxInstance instance) {
		LOGGER.debug("create mode assignment for mode array: {}", Arrays.toString(modes));
		int[] remaining = MrcpspMaxUtils.calculateResourceRemainingVector(modes, instance);
		LOGGER.debug("remaining resources vector for mode array {}: {}",
				Arrays.toString(modes),
				Arrays.toString(remaining));
		boolean resourceFeasible = (instance.getNonRenewableResourceCount() == 0) || (Ints.min(remaining) >= 0);
		LOGGER.debug("modes {} resource feasible: {}", Arrays.toString(modes), resourceFeasible);
		boolean timeFeasible = MrcpspMaxUtils.isModeAssignmentTimeValid(modes, instance);
		LOGGER.debug("modes {} time feasible: {}", Arrays.toString(modes), timeFeasible);
		
		IRcpspMaxInstance rcpspsMaxInstance = RcpspMaxInstance.createInstance(modes, instance);
		LOGGER.debug("corresponding rcpsp/max instance: {}", rcpspsMaxInstance);
		return new ModeAssignment(modes, rcpspsMaxInstance, remaining, resourceFeasible, timeFeasible);
	}

	
}
