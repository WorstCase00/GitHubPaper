package edu.bocmst.scheduling.mrcpspmax.bmap.ga.recombination;

import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.operators.IntArrayCrossover;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.ModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public class BmapArrayCrossover implements
		EvolutionaryOperator<IModeAssignment> {

	private final IntArrayCrossover intArrayCrossover;
	private final IMrcpspMaxInstance problem;
	
	public BmapArrayCrossover(IntArrayCrossover intArrayCrossover, IMrcpspMaxInstance problem) {
		this.intArrayCrossover = intArrayCrossover;
		this.problem = problem;
	}
	
	public List<IModeAssignment> apply(List<IModeAssignment> candidates, Random rng) {
		List<int[]> modeArrays = extractModeArrays(candidates);
		List<int[]> newModes = intArrayCrossover.apply(modeArrays, rng);
		List<IModeAssignment> newCandidates = createCandidates(newModes);
		return newCandidates;
	}

	private List<IModeAssignment> createCandidates(List<int[]> modesList) {
		List<IModeAssignment> candidates = Lists.newArrayList();
		for(int[] modes : modesList) {
			IModeAssignment candidate = ModeAssignment.createInstance(modes, problem);
			candidates.add(candidate);
		}
		return candidates;
	}

	private List<int[]> extractModeArrays(List<IModeAssignment> candidates) {
		List<int[]> modeArrays = Lists.newArrayList();
		for(IModeAssignment candidate : candidates) {
			int[] array = candidate.getModeArray();
			modeArrays.add(array);
		}
		return modeArrays;
	}

	public static EvolutionaryOperator<IModeAssignment> createInstance(
			int crossoverPoints,
			IMrcpspMaxInstance problem) {
		IntArrayCrossover crossover = new IntArrayCrossover(crossoverPoints);
		EvolutionaryOperator<IModeAssignment> instance = new BmapArrayCrossover(crossover, problem);
		return instance;
	}
}
