package edu.bocmst.scheduling.mrcpspmax.bmap.ga.factory;

import edu.bocmst.scheduling.mrcpspmax.bmap.BmapSolverConfiguration;

public class MrcpspMaxCandidateFactoryConfiguration {

	private final BmapSolverConfiguration bmapSolverConfiguration;

	public MrcpspMaxCandidateFactoryConfiguration(
			BmapSolverConfiguration bmapSolverConfiguration) {
		this.bmapSolverConfiguration = bmapSolverConfiguration;
	}

	public BmapSolverConfiguration getBmapSolverConfiguration() {
		return this.bmapSolverConfiguration;
	}

	public static MrcpspMaxCandidateFactoryConfiguration createDefault() {
		return new MrcpspMaxCandidateFactoryConfiguration(BmapSolverConfiguration.createDefault());
	}

}
