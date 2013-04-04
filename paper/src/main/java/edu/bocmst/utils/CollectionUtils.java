package edu.bocmst.utils;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public abstract class CollectionUtils {

	private CollectionUtils() {}
	
	public static List<Integer> createInitializedList(int length, int initValue) {
		List<Integer> list = Lists.newArrayListWithCapacity(length);
		for (int i = 0; i < length; i++) {
			list.add(initValue);
		}
		return list;
	}

	public static List<Integer> createOrderedListOfIntegers(int count) {
		List<Integer> orderedIntegers = Lists.newArrayList();
		for (int i = 0; i < count; i++) {
			orderedIntegers.add(i);
		}
		return orderedIntegers;
	}
	
	public static Set<Integer> createSetOfIntegers(int count) {
		Set<Integer> set = Sets.newHashSetWithExpectedSize(count);
		for (int i = 0; i < count; i++) {
			set.add(i);
		}
		return set;
	}
}
