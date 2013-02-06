package edu.bocmst.scheduling.mrcpspmax.bmap.ga.repair;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;
import edu.bocmst.scheduling.mrcpspmax.commons.RandomUtils;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public class BarriosBmapResourceRepair extends AbstractModeAssignmentRepair  {

	private static final Logger LOGGER = LoggerFactory.getLogger(BarriosBmapRepair.class);
	private final IMrcpspMaxInstance instance;
	
	public BarriosBmapResourceRepair(IMrcpspMaxInstance instance) {
		this.instance = instance;
	}

	public IModeAssignment operate(IModeAssignment candidate) {
		if(candidate.isResourceFeasible()) {
			LOGGER.debug("candidate already resource feasible");
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
		int numberOfTries = modes.length;
		LOGGER.debug("number of repair tries: {}", numberOfTries);
		for(int i = 0; i < numberOfTries; i++) {
			List<Integer> reassignActivities = RandomUtils.getRandomActivitySelection(instance);
			int[] newModes = modes.clone();
			for(int activity : reassignActivities) {
				newModes[activity] = RandomUtils.getRandomMode(activity, instance);
			}
			if(MrcpspMaxUtils.isModeAssignmentResourceValid(newModes, instance)) {
				LOGGER.debug("found resource valid mode assignment: {}", Arrays.toString(newModes));
				return newModes;
			}
		}
		return modes;
	}

}
