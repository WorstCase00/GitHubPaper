package com.uc4.scheduling.mrcpspmax.instance.loader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

public abstract class BaseInstanceFileTest {

	private static final String THESIS_FILE = "src/test/resources/thesisExample.sch";

	protected List<String> readFromThesisFile() throws IOException,
			FileNotFoundException {
				List<String> instanceLines = IOUtils.readLines(new FileInputStream(THESIS_FILE));
				return instanceLines;
			}

}
