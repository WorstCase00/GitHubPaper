package edu.bocmst.scheduling.mrcpspmax.bmap.ga.mutation;

import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.utils.RandomUtils;

public class RandomModeMutation implements EvolutionaryOperator<IModeAssignment> {

	private static final double MUTATION_PROBABILITY = 0.03;
	
	private final IMrcpspMaxInstance instance;

	public RandomModeMutation(IMrcpspMaxInstance instance) {
		this.instance = instance;
	}

	@Override
	public List<IModeAssignment> apply(
			List<IModeAssignment> selectedCandidates, Random rng) {
		List<IModeAssignment> mutated = Lists.newArrayListWithCapacity(selectedCandidates.size());
		for(IModeAssignment candidate : selectedCandidates) {
			IModeAssignment mutatedCandidate = mutate(candidate);
			mutated.add(mutatedCandidate);
		}
		return mutated;
	}

	private IModeAssignment mutate(IModeAssignment candidate) {
		int[] newModes = candidate.getModeArray();
		for (int i = 0; i < newModes.length; i++) {
			double randomDouble = RandomUtils.getInstance().nextDouble();
			if(randomDouble < MUTATION_PROBABILITY) {
				int randomMode = RandomUtils.getInstance().nextInt(instance.getModeCounts()[i]) + 1;
				newModes[i] = randomMode;
			}
		}
		IModeAssignment mutated = ModeAssignmentFactory.createInstance(newModes, instance);
		return mutated;
	}

}
