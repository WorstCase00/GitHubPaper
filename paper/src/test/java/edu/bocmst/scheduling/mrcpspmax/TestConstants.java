package edu.bocmst.scheduling.mrcpspmax;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.loader.InstanceLoader;

public class TestConstants {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TestConstants.class);
	
	public static final String BARRIOS_PATH = "src/test/resources/barriosExample.sch";

	public static final int[] VALID_MODES_BARRIOS_INSTANCE = new int[] {1,2,1,3,1,1,3,1};
	public static IMrcpspMaxInstance BARRIOS_INSTANCE;
	
	static {
		try {
			BARRIOS_INSTANCE = InstanceLoader.loadInstance(BARRIOS_PATH);
		} catch (IOException e) {
			LOGGER.error("failed to load barrios instance", e);
		}
	}
}
