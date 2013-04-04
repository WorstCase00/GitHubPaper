package edu.bocmst.scheduling.mrcpspmax.ga.evaluation;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import edu.bocmst.metaheuristic.IGeneratedSolutionsCounter;
import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public class MrcpspMaxFitnessEvaluatorFactory {

	public static FitnessEvaluator<? super IMrcpspMaxCandidate> createInstance(
			IMrcpspMaxInstance problem,
			MrcpspMaxFitnessEvaluatorConfiguration evaluatorConfiguration, IGeneratedSolutionsCounter solutionsCounter) {
		// TODO Auto-generated method stub
		return new MrcpspMaxFitnessEvaluator(solutionsCounter, problem);
	}

}
