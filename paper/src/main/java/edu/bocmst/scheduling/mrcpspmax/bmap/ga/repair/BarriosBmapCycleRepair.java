package edu.bocmst.scheduling.mrcpspmax.bmap.ga.repair;

import java.util.Arrays;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.primitives.Ints;

import edu.bocmst.graph.IDirectedEdge;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;
import edu.bocmst.scheduling.mrcpspmax.commons.GraphUtils;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;
import edu.bocmst.scheduling.mrcpspmax.commons.RandomUtils;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.utils.IntArrays;

public class BarriosBmapCycleRepair extends AbstractModeAssignmentRepair {

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
		IModeAssignment newCandidate = ModeAssignmentFactory.createInstance(newModes, instance);
		return newCandidate;
	}

	private int[] createNewModes(int[] modes) {
		Set<Set<IDirectedEdge>> positiveCycles = GraphUtils.getPositiveCycles(modes, instance);
		LOGGER.debug("found {} positive cycle structures for mode assignment {}",
				positiveCycles.size(), Arrays.toString(modes));
		boolean[] fixed = new boolean[modes.length];
		int[] newModes = null;
		for(Set<IDirectedEdge> positiveCycle : positiveCycles) {
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
			Set<IDirectedEdge> edgeSet) {
		for(IDirectedEdge edge : edgeSet) {
			fixed[edge.getSource()] = true;
			fixed[edge.getTarget()] = true;
		}
		return fixed;
	}

	private int[] repairCycleStructure(
			int[] modes, 
			Set<IDirectedEdge> positiveCycle, 
			boolean[] fixed) {
		LOGGER.debug("start cycle repair for cycle structure {}", Arrays.toString(positiveCycle.toArray()));
		int[] newModes = Arrays.copyOf(modes, modes.length);
		for(int i = 0; i < REPAIR_TRIES; i++) {
			newModes = randomizeUnfixedModesOfEdgeSet(modes, positiveCycle, fixed);
			LOGGER.debug("new modes guessed for repair try {}: {}", i, Arrays.toString(newModes));
			if(GraphUtils.isWeightSumPositive(positiveCycle, newModes, instance)) {
				LOGGER.debug("cycle weight sum still positive for modes {}", Arrays.toString(newModes));
				continue;
			}
			int[] remaining = MrcpspMaxUtils.calculateResourceRemainingVector(
					newModes, 
					instance);
			LOGGER.debug("remaining resources vector is {} for mode assignment {}",
					Arrays.toString(remaining),
					Arrays.toString(newModes));
			if(Ints.min(remaining) >= 0) {
				LOGGER.debug("guessed mode vector is resource feasible");
				return newModes;
			}
			LOGGER.debug("guessed mode vector not resource feasible - execute repair");
			newModes = repairSinglePass(newModes, remaining, fixed);
			remaining = MrcpspMaxUtils.calculateResourceRemainingVector(
					newModes, 
					instance);
			LOGGER.debug("remaining resources vector is {} for mode assignment {}",
					Arrays.toString(remaining),
					Arrays.toString(newModes));
			if(Ints.min(remaining) >= 0) {
				LOGGER.debug("guessed mode vector is resource feasible");
				return newModes;
			}
			LOGGER.debug("guessed mode vector not resource feasible - retry");
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
			int oldMode = modes[activity];
			int newMode = getMostEfficientModeAndUpdateRemaining(activity, oldMode, remaining);
			LOGGER.debug("switch mode of activity {}: {} -> {}",
					new Object[] {activity, oldMode, newMode});
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
		int[] actualConsumption = MrcpspMaxUtils.getNonRenewableConsumptionVector(activity, actualMode, instance);
		remaining = IntArrays.plus(remaining, actualConsumption);
		for(int mode = 1; mode <= modeCount; mode ++) {
			int[] alternativeConsumption = MrcpspMaxUtils.getNonRenewableConsumptionVector(activity, mode, instance);
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
			Set<IDirectedEdge> positiveCycle, 
			boolean[] fixed) {
		int[] newModes = modes.clone();
		for(IDirectedEdge edge : positiveCycle) {
			int activity = edge.getSource();
			if(!fixed[activity]) {
				int randomMode = RandomUtils.getRandomMode(activity, instance);
				LOGGER.debug("mode of activity {} is set to random mode: {} -> {}",
						new Object[] {activity, modes[activity], randomMode});
				newModes[activity] = randomMode;
			}
		}
		return newModes;
	}
}
