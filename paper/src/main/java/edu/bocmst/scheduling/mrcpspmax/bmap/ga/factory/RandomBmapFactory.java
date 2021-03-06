package edu.bocmst.scheduling.mrcpspmax.bmap.ga.factory;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxRandomUtils;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public class RandomBmapFactory extends AbstractCandidateFactory<IModeAssignment> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RandomBmapFactory.class);
	
	private final IMrcpspMaxInstance instance;

	public RandomBmapFactory(IMrcpspMaxInstance instance) {
		this.instance = instance;
	}

	@Override
	public IModeAssignment generateRandomCandidate(Random rng) {
		LOGGER.debug("create random mode assignment");
		int activityCount = instance.getActivityCount();
		int[] modes = new int[activityCount];
		for(int activity = 0; activity < modes.length; activity++) {
			int randomMode = MrcpspMaxRandomUtils.getRandomMode(activity, instance);
			modes[activity] = randomMode;
		}
		IModeAssignment candidate = ModeAssignmentFactory.createInstance(modes, instance);
		return candidate;
	}

	
}
