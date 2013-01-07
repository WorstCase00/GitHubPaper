package edu.bocmst.scheduling.mrcpspmax.methaheuristics;

import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.termination.ElapsedTime;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

public class TerminationConditionCreator {

	public TerminationCondition createCondition(
			TerminationConditionConfiguration terminationConfiguration) {
		TerminationConditionType type = terminationConfiguration.getType();
		switch(type) {
		case ElapsedTime: 
			long maxDurationMs = terminationConfiguration.getMaxDurationMs();
			return new ElapsedTime(maxDurationMs);
		case GenerationCount:
			int generationCount = terminationConfiguration.getGenerationCount();
			return new GenerationCount(generationCount);
		// TODO some solution counter must be included here
		}
		throw new IllegalArgumentException();
	}

}
