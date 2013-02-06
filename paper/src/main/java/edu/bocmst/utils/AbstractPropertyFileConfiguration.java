package edu.bocmst.utils;

import org.apache.commons.configuration.PropertiesConfiguration;


public abstract class AbstractPropertyFileConfiguration {
	
	protected final PropertiesConfiguration configuration;

	protected AbstractPropertyFileConfiguration(
			PropertiesConfiguration configuration) {
		super();
		this.configuration = configuration;
	}
}
