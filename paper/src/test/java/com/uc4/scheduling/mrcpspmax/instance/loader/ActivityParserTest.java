package com.uc4.scheduling.mrcpspmax.instance.loader;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.NonReadableChannelException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.uc4.scheduling.mrcpspmax.instance.INetworkVertex;
import com.uc4.scheduling.mrcpspmax.instance.NetworkVertex;

public class ActivityParserTest extends BaseInstanceFileTest {

	private static final int NON_RENEWABLE_COUNT = 1;
	private static final Object RENEWABLE_COUNT = 1;
	
	private static final List<int[]> RENEWABLE_CONSUMPTIONS = Lists.newArrayList();
	static {
		RENEWABLE_CONSUMPTIONS.add(new int[] {0});
		RENEWABLE_CONSUMPTIONS.add(new int[] {3, 2, 1});
		RENEWABLE_CONSUMPTIONS.add(new int[] {1, 2, 1});
		RENEWABLE_CONSUMPTIONS.add(new int[] {1, 2, 1});
		RENEWABLE_CONSUMPTIONS.add(new int[] {2, 1, 1});
		RENEWABLE_CONSUMPTIONS.add(new int[] {2, 2, 2});
		RENEWABLE_CONSUMPTIONS.add(new int[] {1, 1, 1});
		RENEWABLE_CONSUMPTIONS.add(new int[] {0});
	}
	
	private static final List<int[]> NON_RENEWABLE_CONSUMPTIONS = Lists.newArrayList();
	static {
		NON_RENEWABLE_CONSUMPTIONS.add(new int[] {0});
		NON_RENEWABLE_CONSUMPTIONS.add(new int[] {2, 1, 1});
		NON_RENEWABLE_CONSUMPTIONS.add(new int[] {1, 1, 2});
		NON_RENEWABLE_CONSUMPTIONS.add(new int[] {4, 2, 1});
		NON_RENEWABLE_CONSUMPTIONS.add(new int[] {1, 2, 1});
		NON_RENEWABLE_CONSUMPTIONS.add(new int[] {1, 1, 3});
		NON_RENEWABLE_CONSUMPTIONS.add(new int[] {2, 1, 2});
		NON_RENEWABLE_CONSUMPTIONS.add(new int[] {0});
	}

	private static final List<int[]> PROCESSING_TIMES = Lists.newArrayList();
	static {
		PROCESSING_TIMES.add(new int[] {0});
		PROCESSING_TIMES.add(new int[] {2, 6, 3});
		PROCESSING_TIMES.add(new int[] {2, 1, 1});
		PROCESSING_TIMES.add(new int[] {1, 2, 2});
		PROCESSING_TIMES.add(new int[] {2, 1, 1});
		PROCESSING_TIMES.add(new int[] {2, 2, 1});
		PROCESSING_TIMES.add(new int[] {1, 1, 4});
		PROCESSING_TIMES.add(new int[] {0});
	}
	
	private int ACTIVITY_COUNT = 8;

	@Test
	public void testProcessingTimesLoadFromFile() throws FileNotFoundException, IOException {
		List<String> instanceLines = readFromThesisFile();
	
		Map<INetworkVertex, List<Integer>> result = 
			ActivityParser.parseProcessingTimes(instanceLines);
		
		assertEquals(ACTIVITY_COUNT, result.size());
		for(int i = 0; i < ACTIVITY_COUNT; i++) {
			List<Integer> procTimes = result.get(NetworkVertex.createInstance(i));
			assertArrayEquals(PROCESSING_TIMES.get(i), Ints.toArray(procTimes));
		}
	}
	
	@Test
	public void testNonRenewableresourcesLoadFromFile() throws FileNotFoundException, IOException {
		List<String> instanceLines = readFromThesisFile();
	
		Map<INetworkVertex, List<List<Integer>>> result = 
			ActivityParser.parseNonRenewableResourceConsumptions(instanceLines);
		
		assertEquals(ACTIVITY_COUNT, result.size());
		for(int i = 0; i < ACTIVITY_COUNT; i++) {
			List<List<Integer>> allConsumptions = result.get(NetworkVertex.createInstance(i));
			assertEquals(NON_RENEWABLE_COUNT, allConsumptions.size());
			for (int j = 0; j < allConsumptions.size(); j++) {
				List<Integer> consumptions = allConsumptions.get(j);
				assertArrayEquals(NON_RENEWABLE_CONSUMPTIONS.get(i), Ints.toArray(consumptions));
			}
		}
	}
	
	@Test
	public void testRenewableresourcesLoadFromFile() throws FileNotFoundException, IOException {
		List<String> instanceLines = readFromThesisFile();
	
		Map<INetworkVertex, List<List<Integer>>> result = 
			ActivityParser.parseRenewableResourceConsumptions(instanceLines);
		
		assertEquals(ACTIVITY_COUNT, result.size());
		for(int i = 0; i < ACTIVITY_COUNT; i++) {
			List<List<Integer>> allConsumptions = result.get(NetworkVertex.createInstance(i));
			assertEquals(RENEWABLE_COUNT, allConsumptions.size());
			for (int j = 0; j < allConsumptions.size(); j++) {
				List<Integer> consumptions = allConsumptions.get(j);
				int[] expected = RENEWABLE_CONSUMPTIONS.get(i);
				assertArrayEquals(expected, Ints.toArray(consumptions));
			}
		}
	}
}
