package edu.bocmst.scheduling.mrcpspmax;

import edu.bocmst.metaheuristic.IGeneratedSolutionsCounter;
import edu.bocmst.scheduling.mrcpspmax.ga.GaMrcpspMaxSolverConfiguration;
import edu.bocmst.scheduling.mrcpspmax.ga.GaMrcpspMaxSolverFactory;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.utils.AtomicCounterCounter;

public abstract class MrcpspMaxSolverFactory {
	
	private MrcpspMaxSolverFactory() {}
	
	public static IMrcpspMaxSolver create(
			IMrcpspMaxInstance instance,
			MrcpspMaxSolverConfiguration configuration) {
		IGeneratedSolutionsCounter solutionsCounter = new AtomicCounterCounter();
		GaMrcpspMaxSolverConfiguration gaConfiguration = configuration.getGaConfiguration();
		return GaMrcpspMaxSolverFactory.create(instance, gaConfiguration, solutionsCounter);
	}
}
