package edu.bocmst.scheduling.mrcpspmax.bmap.ga.recombination;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.uncommons.watchmaker.framework.operators.IntArrayCrossover;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ModeAssignmentFactory.class)
public class BmapArrayCrossoverTest {

	private static final int[] MODES1 = new int[] {1, 2};
	private static final int[] MODES2 = new int[] {3, 4};
	private static final int[] MODES3 = new int[] {5, 6};
	
	@Mock private IntArrayCrossover intArrayCrossover;
	@Mock private IMrcpspMaxInstance problem;
	@Mock private Random rng;
	@Mock private IModeAssignment candidate1;
	@Mock private IModeAssignment candidate2;
	@Mock private IModeAssignment candidate3;

	@Before
	public void setupFixture() {
		PowerMockito.mockStatic(ModeAssignmentFactory.class);
	}
	
	@Test
	public void testWithThreeCandidates() {
		BmapArrayRecombination testee = new BmapArrayRecombination(intArrayCrossover, problem);
		List<IModeAssignment> candidates = Lists.newArrayList();
		List<int[]> modeList = Lists.newArrayList(MODES1, MODES2, MODES3);
		when(intArrayCrossover.apply(anyListOf(int[].class), any(Random.class))).thenReturn(modeList);
	
		PowerMockito.when(ModeAssignmentFactory.createInstance(MODES1, problem)).thenReturn(candidate1);
		PowerMockito.when(ModeAssignmentFactory.createInstance(MODES2, problem)).thenReturn(candidate2);
		PowerMockito.when(ModeAssignmentFactory.createInstance(MODES3, problem)).thenReturn(candidate3);
		when(candidate1.getModeArray()).thenReturn(MODES1);
		when(candidate2.getModeArray()).thenReturn(MODES2);
		when(candidate3.getModeArray()).thenReturn(MODES3);
		
		List<IModeAssignment> result = testee.apply(candidates, rng);
		
		assertArrayEquals(MODES1, result.get(0).getModeArray());
		assertArrayEquals(MODES2, result.get(1).getModeArray());
		assertArrayEquals(MODES3, result.get(2).getModeArray());
	}
	
}
