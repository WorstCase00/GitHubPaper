package com.uc4.scheduling.mrcpspmax.bmap.ga.crossover;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uncommons.watchmaker.framework.operators.IntArrayCrossover;
import static org.junit.Assert.*;
import com.google.common.collect.Lists;
import com.uc4.scheduling.mrcpspmax.bmap.solution.IModeAssignment;
import com.uc4.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BmapArrayCrossoverTest {

	private static final int[] MODES1 = new int[] {1, 2};
	private static final int[] MODES2 = new int[] {3, 4};
	private static final int[] MODES3 = new int[] {5, 6};
	@Mock private IntArrayCrossover intArrayCrossover;
	@Mock private IMrcpspMaxInstance problem;
	@Mock private Random rng;

	@Test
	public void testWithThreeCandidates() {
		BmapArrayCrossover testee = new BmapArrayCrossover(intArrayCrossover, problem);
		List<IModeAssignment> candidates = Lists.newArrayList();
		List<int[]> modeList = Lists.newArrayList(MODES1, MODES2, MODES3);
		when(intArrayCrossover.apply(anyListOf(int[].class), any(Random.class))).thenReturn(modeList);
		
		List<IModeAssignment> result = testee.apply(candidates, rng);
		
		assertArrayEquals(MODES1, result.get(0).getModeArray());
		assertArrayEquals(MODES2, result.get(1).getModeArray());
		assertArrayEquals(MODES3, result.get(2).getModeArray());
	}
	
}
