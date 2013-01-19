package edu.bocmst.scheduling.mrcpspmax;

import java.io.IOException;

import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.loader.InstanceLoader;

public class TestConstants {
	public static final String THESIS_PATH = "src/test/resources/thesisExample.sch";
	public static IMrcpspMaxInstance THESIS_INSTANCE; // TODO hard code thesis instance
	static {
		try {
			THESIS_INSTANCE = InstanceLoader.loadInstance(THESIS_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
