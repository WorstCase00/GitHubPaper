package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import edu.bocmst.scheduling.mrcpspmax.instance.INonRenewableResource;
import edu.bocmst.scheduling.mrcpspmax.instance.IRenewableResource;
import edu.bocmst.scheduling.mrcpspmax.instance.NonRenewableResource;
import edu.bocmst.scheduling.mrcpspmax.instance.RenewableResource;

public abstract class ResourceParser extends BaseParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceParser.class);
	private static final int MIXED_RENEWABLE_COUNT_INDEX = 3;
	
	private ResourceParser() {}

	public static void verifyNoMixedResources(String string) {
		int mixedResourcesCount = getIntegerParsedIndexWord(
				string, 
				MIXED_RENEWABLE_COUNT_INDEX);
		if(mixedResourcesCount != 0) {
			throw new RuntimeException("mixed resources specified");
		}
	}

	public static List<IRenewableResource> parseRenewableResourcesList(
			List<String> instanceLines) {
		
		int renewableCount = getRenewableResourcesCount(instanceLines);
		LOGGER.debug("renewable resource count: {}", renewableCount);
		String footerString = instanceLines.get(instanceLines.size() - 1);
		String[] footerWords = footerString.split(InstanceFileConstants.DELIMITER);
		List<IRenewableResource> renewableResourceList = Lists.newArrayList();
		
		for(int i = 0; i < renewableCount; i++) {
			int limit = Integer.parseInt(footerWords[i]);
			LOGGER.debug("create nonrenewable resource with limit: {}", limit);
			IRenewableResource resource = new RenewableResource(limit);
			renewableResourceList.add(resource);
		}
		return renewableResourceList;
	}

	public static List<INonRenewableResource> parseNonRenewableResourceList(
			List<String> instanceLines) {
		int nonRenewableCount = getNonRenewableResourcesCount(instanceLines);
		LOGGER.debug("non-renewable resource count: {}", nonRenewableCount);
		
		String footerString = instanceLines.get(instanceLines.size() - 1);
		String[] footerWords = footerString.split("\t");
		List<INonRenewableResource> nonRenewableResourceList = Lists.newArrayList();
		
		int renewableCountForOffset = getRenewableResourcesCount(instanceLines);
		for(int i = 0; i < nonRenewableCount; i++) {
			int limit = Integer.parseInt(footerWords[i + renewableCountForOffset]);
			INonRenewableResource resource = new NonRenewableResource(limit);
			LOGGER.debug("create non-renewable resource with limit: {}", limit);
			nonRenewableResourceList.add(resource);
		}
		return nonRenewableResourceList;
	}
}