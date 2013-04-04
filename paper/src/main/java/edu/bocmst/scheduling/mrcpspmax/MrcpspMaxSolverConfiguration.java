package edu.bocmst.scheduling.mrcpspmax;

import org.apache.commons.configuration.PropertiesConfiguration;

import edu.bocmst.scheduling.mrcpspmax.ga.GaMrcpspMaxSolverConfiguration;
import edu.bocmst.utils.AbstractPropertyFileConfiguration;

public class MrcpspMaxSolverConfiguration extends AbstractPropertyFileConfiguration {

	private final GaMrcpspMaxSolverConfiguration gaConfiguration;


	protected MrcpspMaxSolverConfiguration(
			PropertiesConfiguration configuration,
			GaMrcpspMaxSolverConfiguration gaConfiguration) {
		super(configuration);
		this.gaConfiguration = gaConfiguration;
	}

	static final class Keys {

		public static final String SOLVER_TYPE = "solverType";

	}

	static final class Defaults {

		public static final String SOLVER_TYPE = "GeneticAlgorithm";

	}

	public MrcpspMaxSolverType getSolverType() {
		String typeString = configuration.getString(
				Keys.SOLVER_TYPE,
				Defaults.SOLVER_TYPE);
		return MrcpspMaxSolverType.valueOf(typeString);
	}

	public GaMrcpspMaxSolverConfiguration getGaConfiguration() {
		return this.gaConfiguration;
	}

	public static MrcpspMaxSolverConfiguration createDefault() {
		PropertiesConfiguration config = new PropertiesConfiguration();
		GaMrcpspMaxSolverConfiguration gaConfig = GaMrcpspMaxSolverConfiguration.createDefault();
		MrcpspMaxSolverConfiguration instance = new MrcpspMaxSolverConfiguration(config, gaConfig);
		return instance;
	}
}