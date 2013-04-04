package edu.bocmst.scheduling.mrcpspmax.commons;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.utils.RandomUtils;

public abstract class MrcpspMaxRandomUtils extends RandomUtils {
	
	private MrcpspMaxRandomUtils() {}
	
	public static Random getInstance() {
		return INSTANCE;
	}
	
	public static List<Integer> getRandomActivityPermutation(IMrcpspMaxInstance instance) {
		List<Integer> permutation = createList(instance.getActivityCount());
		Collections.shuffle(permutation, INSTANCE);
		return permutation;
	}
	
	public static List<Integer> getRandomActivitySelection(IMrcpspMaxInstance instance) {
		List<Integer> permutation = getRandomActivityPermutation(instance);
		int activityCount = instance.getActivityCount();
		int sublistSize = INSTANCE.nextInt(activityCount-1) + 1;
		List<Integer> suList = permutation.subList(0, sublistSize);
		return suList;
	}
	
	public static int getRandomMode(int activity, IMrcpspMaxInstance instance) {
		int modeCount = instance.getModeCount(activity);
		if(modeCount == 1) {
			return 1;
		}
		int randomMode = INSTANCE.nextInt(modeCount - 1) + 1;
		return randomMode;
	}

	private static List<Integer> createList(int n) {
		List<Integer> list = Lists.newArrayListWithCapacity(n);
		for(int i = 0; i < n; i++) {
			list.add(i);
		}
		return list;
	}
}
