package edu.bocmst.scheduling.mrcpspmax.ga.factory;

import org.uncommons.watchmaker.framework.CandidateFactory;

import edu.bocmst.scheduling.mrcpspmax.bmap.ga.factory.MrcpspMaxCandidateFactoryConfiguration;
import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public abstract class MrcpspMaxFactoryFactory {

	private MrcpspMaxFactoryFactory() {}

	public static CandidateFactory<IMrcpspMaxCandidate> createCandidateFactory(
			IMrcpspMaxInstance problem,
			MrcpspMaxCandidateFactoryConfiguration configuration) {
		// TODO Auto-generated method stub
		return BmapBasedRandomKeysCandidateFactory.createInstance(problem, configuration);
	}
	
	
}
