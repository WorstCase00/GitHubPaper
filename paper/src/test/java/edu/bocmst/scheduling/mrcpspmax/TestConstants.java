package edu.bocmst.scheduling.mrcpspmax;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.loader.InstanceLoader;

public class TestConstants {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TestConstants.class);
	
	public static final String THESIS_PATH = "src/test/resources/thesisExample.sch";
	public static IMrcpspMaxInstance THESIS_INSTANCE;
	
	static {
		try {
			THESIS_INSTANCE = InstanceLoader.loadInstance(THESIS_PATH);
		} catch (IOException e) {
			LOGGER.error("failed to load thesis instance", e);
		}
	}
}
