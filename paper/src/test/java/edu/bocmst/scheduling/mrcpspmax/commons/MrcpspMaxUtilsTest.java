package edu.bocmst.scheduling.mrcpspmax.commons;

import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

public class MrcpspMaxUtilsTest {
	
	@Test
	public void testOrderActivitiesWithUniqueStarts() throws Exception {
		int[] startTimes = new int[] {1,3,5,4,2};
		Integer[] expected = new Integer[] {2,3,1,4,0};
		
		List<Integer> result = MrcpspMaxUtils.getActivitiesOrderedByStartTimeDecreasing(startTimes);
		
		assertArrayEquals(expected, result.toArray());
	}
	
	@Test
	public void testOrderActivitiesWithDuplicateStarts() throws Exception {
		int[] startTimes = new int[] {1,1,0};
		
		List<Integer> result = MrcpspMaxUtils.getActivitiesOrderedByStartTimeDecreasing(startTimes);
		
		assertEquals(startTimes.length, result.size());
		assertEquals(2, result.get(2).intValue());
	}
}
