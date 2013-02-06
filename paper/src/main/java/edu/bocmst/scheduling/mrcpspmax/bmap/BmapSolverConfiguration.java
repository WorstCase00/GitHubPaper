package edu.bocmst.scheduling.mrcpspmax.bmap;

import org.apache.commons.configuration.PropertiesConfiguration;

import edu.bocmst.scheduling.mrcpspmax.bmap.ga.GaBmapSolverConfiguration;
import edu.bocmst.utils.AbstractPropertyFileConfiguration;

public class BmapSolverConfiguration extends AbstractPropertyFileConfiguration {

	protected BmapSolverConfiguration(
			PropertiesConfiguration configuration,
			GaBmapSolverConfiguration gaConfiguration) {
		super(configuration);
		this.gaConfiguration = gaConfiguration;
	}

	static final class Keys {

		public static final String SOLVER_TYPE = "solverType";
		
	}
	
	static final class Defaults {

		public static final String SOLVER_TYPE = "GeneticAlgorithm";
		
	}

	private final GaBmapSolverConfiguration gaConfiguration;
	
	public BmapSolverType getSolverType() {
		String typeString = configuration.getString(
				Keys.SOLVER_TYPE,
				Defaults.SOLVER_TYPE);
		return BmapSolverType.valueOf(typeString);
	}

	public GaBmapSolverConfiguration getGaConfiguration() {
		return this.gaConfiguration;
	}

	public static BmapSolverConfiguration createDefault() {
		PropertiesConfiguration config = new PropertiesConfiguration();
		GaBmapSolverConfiguration gaConfig = GaBmapSolverConfiguration.createDefault();
		BmapSolverConfiguration instance = new BmapSolverConfiguration(config, gaConfig);
		return instance;
	}

}
