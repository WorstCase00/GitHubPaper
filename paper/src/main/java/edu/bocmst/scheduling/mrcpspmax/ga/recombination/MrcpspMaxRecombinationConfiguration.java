package edu.bocmst.scheduling.mrcpspmax.ga.recombination;

import org.apache.commons.configuration.PropertiesConfiguration;

import edu.bocmst.utils.AbstractPropertyFileConfiguration;

public class MrcpspMaxRecombinationConfiguration extends AbstractPropertyFileConfiguration{

	protected MrcpspMaxRecombinationConfiguration(
			PropertiesConfiguration configuration) {
		super(configuration);
	}
	
	public MrcpspMaxRecombinationConfiguration createDefault() {
		PropertiesConfiguration empty = new PropertiesConfiguration();
		MrcpspMaxRecombinationConfiguration instance = new MrcpspMaxRecombinationConfiguration(empty);
		return instance;
	}

	public int getCrossoverPoints() {
		// TODO Auto-generated method stub
		return 1;
	}

}
