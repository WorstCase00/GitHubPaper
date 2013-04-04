package edu.bocmst.scheduling.mrcpspmax.ga.recombination;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public abstract class MrcpspMaxRecombinationFactory {

	public static EvolutionaryOperator<IMrcpspMaxCandidate> create(
			MrcpspMaxRecombinationConfiguration configuration, IMrcpspMaxInstance instance) {
		return new MrcpspMaxListOrderRecombination(instance);
	}

}
