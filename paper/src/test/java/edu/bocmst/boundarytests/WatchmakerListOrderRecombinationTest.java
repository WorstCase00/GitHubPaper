package edu.bocmst.boundarytests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.uncommons.watchmaker.framework.operators.ListOrderCrossover;

import com.google.common.collect.Lists;

import edu.bocmst.utils.CollectionUtils;
import edu.bocmst.utils.RandomUtils;

public class WatchmakerListOrderRecombinationTest {

	private static final int CANDIDATE_COUNT = 100;
	private static final int ALLEL_COUNT = 100;

	@Test
	public void testCrossoverDeliversLegalActivityLists() throws Exception {
		ListOrderCrossover<Integer> testee = new ListOrderCrossover<Integer>();
		List<List<Integer>> selectedCandidates = Lists.newArrayList();
		for (int i = 0; i < CANDIDATE_COUNT; i++) {
			List<Integer> randomCandidate = RandomUtils.createRandomPermutation(ALLEL_COUNT);
			selectedCandidates.add(randomCandidate);
		}
		List<List<Integer>> result = testee.apply(selectedCandidates, RandomUtils.getInstance());

		assertEquals(CANDIDATE_COUNT, result.size());
		Set<Integer> activities = CollectionUtils.createSetOfIntegers(ALLEL_COUNT);
		for(List<Integer> list : result) {
			assertEquals(ALLEL_COUNT, list.size());
			assertTrue(list.containsAll(activities));
		}
	}
	
}
