package edu.bocmst.utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public abstract class RandomUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(RandomUtils.class);

	protected static final Random INSTANCE = new Random(System.currentTimeMillis());

	public static Integer getRandomFromSet(Set<Integer> eligibleSet) {
		if(eligibleSet.isEmpty()) {
			LOGGER.error("empty set");
			throw new IllegalArgumentException("empty set");
		}
		int randomIndex = INSTANCE.nextInt(eligibleSet.size());
		Iterator<Integer> iterator = eligibleSet.iterator();
		for (int i = 0; i < randomIndex; i++) {
			iterator.next();
		}
		return iterator.next();
	}
	
	public static Random getInstance() {
		return INSTANCE;
	}

	public static List<Integer> createRandomPermutation(int count) {
		List<Integer> integers = CollectionUtils.createOrderedListOfIntegers(count);
		Collections.shuffle(integers);
		return integers;
	}

	

}
