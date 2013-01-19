package edu.bocmst.scheduling.mrcpspmax.bmap;

import edu.bocmst.scheduling.mrcpspmax.bmap.ga.GaBmapSolverConfiguration;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.GaBmapSolverFactory;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public abstract class BmapSolverFactory {

	private BmapSolverFactory() {}
	
	public static IBmapSolver createInstanceForInstance(
			IMrcpspMaxInstance instance,
			BmapSolverConfiguration configuration) {
		BmapSolverType type = configuration.getSolverType();
		switch(type) {
		case GeneticAlgorithm: 
			GaBmapSolverConfiguration gaConfiguration = configuration.getGaConfiguration();
			return GaBmapSolverFactory.createInstance(instance, gaConfiguration);
		}
		throw new IllegalArgumentException(type.name());
	}
}
