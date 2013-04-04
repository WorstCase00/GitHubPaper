package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import edu.bocmst.scheduling.mrcpspmax.TestConstants;

public abstract class BaseInstanceFileTest {

	protected List<String> readFromBarriosExampleFile() throws IOException,
			FileNotFoundException {
				List<String> instanceLines = IOUtils.readLines(new FileInputStream(TestConstants.BARRIOS_PATH));
				return instanceLines;
			}

}
