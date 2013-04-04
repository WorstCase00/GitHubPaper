package edu.bocmst.scheduling.mrcpspmax.bmap;

import edu.bocmst.metaheuristic.IGeneratedSolutionsCounter;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.GaBmapSolverConfiguration;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.GaBmapSolverFactory;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.utils.AtomicCounterCounter;

public abstract class BmapSolverFactory {

	private BmapSolverFactory() {}
	
	public static IBmapSolver createInstanceForInstance(
			IMrcpspMaxInstance instance,
			BmapSolverConfiguration configuration) {
		IGeneratedSolutionsCounter solutionsCounter = new AtomicCounterCounter();
		BmapSolverType type = configuration.getSolverType();
		switch(type) {
		case GeneticAlgorithm: 
			GaBmapSolverConfiguration gaConfiguration = configuration.getGaConfiguration();
			return GaBmapSolverFactory.createInstance(
					instance, 
					gaConfiguration,
					solutionsCounter);
		}
		throw new IllegalArgumentException(type.name());
	}
}
