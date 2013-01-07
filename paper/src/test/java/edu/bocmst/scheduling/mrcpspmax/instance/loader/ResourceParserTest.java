package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import edu.bocmst.scheduling.mrcpspmax.instance.INonRenewableResource;
import edu.bocmst.scheduling.mrcpspmax.instance.IRenewableResource;
import edu.bocmst.scheduling.mrcpspmax.instance.loader.ResourceParser;

public class ResourceParserTest extends BaseInstanceFileTest {

	private static final int NON_RENEWABLE_COUNT = 1;
	private static final int NON_RENEWABLE_RESOURCE_LIMIT = 10;
	private static final int RENEWABLE_COUNT = 1;
	private static final int RENEWABLE_RESOURCE_LIMIT = 4;
	
	@Test
	public void testNonRenewableResourcesExampleFromFile() throws FileNotFoundException, IOException {
		List<String> instanceLines = readFromThesisFile();
		
		List<INonRenewableResource> result = ResourceParser.parseNonRenewableResourceList(instanceLines);
	
		assertEquals(NON_RENEWABLE_COUNT, result.size());
		assertEquals(NON_RENEWABLE_RESOURCE_LIMIT, result.get(0).getSupply());
	}

	@Test
	public void tesRenewableResourcesExampleFromFile() throws FileNotFoundException, IOException {
		List<String> instanceLines = readFromThesisFile();
		
		List<IRenewableResource> result = ResourceParser.parseRenewableResourcesList(instanceLines);
	
		assertEquals(RENEWABLE_COUNT, result.size());
		assertEquals(RENEWABLE_RESOURCE_LIMIT, result.get(0).getSupply());
	}
}
