package edu.bocmst.scheduling.mrcpspmax.bmap.ga.repair;

import java.util.Arrays;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.primitives.Ints;

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.ModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.AbstractModeAssignmentOperator;
import edu.bocmst.scheduling.mrcpspmax.commons.GraphUtils;
import edu.bocmst.scheduling.mrcpspmax.commons.IntArrays;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxHelper;
import edu.bocmst.scheduling.mrcpspmax.commons.RandomUtils;
import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetworkEdge;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public class BarriosBmapCycleRepair extends AbstractModeAssignmentOperator {

	private static final Logger LOGGER = LoggerFactory.getLogger(BarriosBmapCycleRepair.class);

	private static final int REPAIR_TRIES = 100;
	
	private final IMrcpspMaxInstance instance;
	
	public BarriosBmapCycleRepair(IMrcpspMaxInstance instance) {
		this.instance = instance;
	}
	
	@Override
	public IModeAssignment operate(IModeAssignment candidate) {
		if(candidate.isTimeFeasible()) {
			LOGGER.debug("candidate already time feasible");
			return candidate;
		}
		int[] modes = candidate.getModeArray();
		int[] newModes = createNewModes(modes);
		if(Arrays.equals(modes, newModes)) {
			LOGGER.debug("no repair modifications found");
			return candidate;
		}
		IModeAssignment newCandidate = ModeAssignment.createInstance(newModes, instance);
		return newCandidate;
	}

	private int[] createNewModes(int[] modes) {
		Set<Set<IAonNetworkEdge>> positiveCycles = GraphUtils.getPositiveCycles(modes, instance);
		boolean[] fixed = new boolean[modes.length];
		int[] newModes = null;
		for(Set<IAonNetworkEdge> positiveCycle : positiveCycles) {
			newModes = repairCycleStructure(modes, positiveCycle, fixed);
			if(GraphUtils.isWeightSumPositive(positiveCycle, newModes, instance)) {
				LOGGER.debug("cycle repair failed");
				return modes;
			}
			fixed = updateFixed(fixed, positiveCycle);
		}
		return newModes;
	}

	private boolean[] updateFixed(
			boolean[] fixed,
			Set<IAonNetworkEdge> edgeSet) {
		for(IAonNetworkEdge edge : edgeSet) {
			fixed[edge.getSource()] = true;
			fixed[edge.getTarget()] = true;
		}
		return fixed;
	}

	private int[] repairCycleStructure(
			int[] modes, 
			Set<IAonNetworkEdge> positiveCycle, 
			boolean[] fixed) {
		int[] newModes = Arrays.copyOf(modes, modes.length);
		for(int i = 0; i < REPAIR_TRIES; i++) {
			newModes = randomizeUnfixedModesOfEdgeSet(modes, positiveCycle, fixed);
			if(GraphUtils.isWeightSumPositive(positiveCycle, newModes, instance)) {
				continue;
			}
			int[] remaining = MrcpspMaxHelper.calculateResourceRemainingVector(
					newModes, 
					instance);
			if(Ints.min(remaining) >= 0) {
				return newModes;
			}
			newModes = repairSinglePass(newModes, remaining, fixed);
			remaining = MrcpspMaxHelper.calculateResourceRemainingVector(
					newModes, 
					instance);
			if(Ints.min(remaining) >= 0) {
				return newModes;
			}
		}
		return newModes;
	}

	private int[] repairSinglePass(
			int[] modes, 
			int[] remaining, 
			boolean[] fixed) {
		for(int activity = 0; activity < modes.length; activity ++) {
			if(fixed[activity]) {
				continue;
			}
			int newMode = getMostEfficientModeAndUpdateRemaining(activity, modes[activity], remaining);
			modes[activity] = newMode;
		}
		return modes;
	}

	private int getMostEfficientModeAndUpdateRemaining(
			int activity,
			int actualMode,
			int[] remaining) {
		int modeCount = instance.getModeCount(activity);
		int chosenMode = 0;
		int highScore = Integer.MIN_VALUE;
		int[] actualConsumption = MrcpspMaxHelper.getNonRenewableConsumptionVector(activity, actualMode, instance);
		remaining = IntArrays.plus(remaining, actualConsumption);
		for(int mode = 1; mode <= modeCount; mode ++) {
			int[] alternativeConsumption = MrcpspMaxHelper.getNonRenewableConsumptionVector(activity, mode, instance);
			int score = calculateScore(alternativeConsumption, remaining);
			if(score > highScore) {
				actualConsumption = alternativeConsumption;
				highScore = score;
				chosenMode = mode;
			}
		}
		remaining = IntArrays.minus(remaining, actualConsumption);
		return chosenMode;
	}

	private int calculateScore(
			int[] actualConsumption,
			int[] remaining) {
		int[] nextRemaining = IntArrays.minus(remaining, actualConsumption);
		int negativRemainingSum = 0;
		for(int r : nextRemaining) {
			if(r < 0) {
				negativRemainingSum += r;
			}
		}
		return negativRemainingSum;
	}

	private int[] randomizeUnfixedModesOfEdgeSet(
			int[] modes, 
			Set<IAonNetworkEdge> positiveCycle, 
			boolean[] fixed) {
		int[] newModes = modes.clone();
		for(IAonNetworkEdge edge : positiveCycle) {
			int activity = edge.getSource();
			if(!fixed[activity]) {
				int randomMode = RandomUtils.getRandomMode(activity, instance);
				modes[activity] = randomMode;
			}
		}
		return newModes;
	}
}
