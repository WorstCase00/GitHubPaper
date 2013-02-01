package edu.bocmst.metaheuristic;

import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.termination.ElapsedTime;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

public class TerminationConditionCreator {

	public TerminationCondition createCondition(
			TerminationConditionConfiguration terminationConfiguration,
			IGeneratedSolutionsCounter terminationCounter) {
		TerminationConditionType type = terminationConfiguration.getType();
		switch(type) {
		case ElapsedTime: 
			long maxDurationMs = terminationConfiguration.getMaxDurationMs();
			return new ElapsedTime(maxDurationMs);
		case GenerationCount:
			int generationCount = terminationConfiguration.getGenerationCount();
			return new GenerationCount(generationCount);
		case TerminationCounter:
			int terminationCount = terminationConfiguration.getTerminationCount();
			return new TerminationCountDownCondition(terminationCounter, terminationCount);
		}
		throw new IllegalArgumentException();
	}

}
