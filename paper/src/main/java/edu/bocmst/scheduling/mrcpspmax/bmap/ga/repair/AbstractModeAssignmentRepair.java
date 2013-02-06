package edu.bocmst.scheduling.mrcpspmax.bmap.ga.repair;

import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;

abstract class AbstractModeAssignmentRepair implements EvolutionaryOperator<IModeAssignment> {
	
	@Override
	public List<IModeAssignment> apply(
			List<IModeAssignment> selectedCandidates, Random rng) {
		List<IModeAssignment> output = Lists.newArrayList();
		for(IModeAssignment candidate : selectedCandidates) {
			IModeAssignment newCandidate = operate(candidate);
			output.add(newCandidate);
		}
		return output;
	}

	public abstract IModeAssignment operate(IModeAssignment candidate);
}
