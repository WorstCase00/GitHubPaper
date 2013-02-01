package edu.bocmst.metaheuristic;

import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.TerminationCondition;

public class TerminationCountDownCondition implements TerminationCondition {

	private final IGeneratedSolutionsCounter terminationCounter;
	private final int terminationCount;

	public TerminationCountDownCondition(
			IGeneratedSolutionsCounter terminationCounter, 
			int terminationCount) {
		this.terminationCounter = terminationCounter;
		this.terminationCount = terminationCount;
	}

	@Override
	public boolean shouldTerminate(PopulationData<?> populationData) {
		if(terminationCounter.getCount() >= terminationCount) {
			return true;
		}
		return false;
	}

}
