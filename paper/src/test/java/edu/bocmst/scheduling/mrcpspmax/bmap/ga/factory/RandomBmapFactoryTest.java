package edu.bocmst.scheduling.mrcpspmax.bmap.ga.factory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxRandomUtils;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
	ModeAssignmentFactory.class,
	MrcpspMaxRandomUtils.class
})
public class RandomBmapFactoryTest {
	
	private static final int[] MODE_COUNTS = new int[] {1, 3, 3};
	private static final int[] RNG_RESPONSES = new int[] {1, 2, 3};
	
	@Mock private IMrcpspMaxInstance instance;
	@Mock private Random rng;
	@Mock private IModeAssignment candidate;

	@Before
	public void setupFixture() {
		PowerMockito.mockStatic(ModeAssignmentFactory.class);
		PowerMockito.mockStatic(MrcpspMaxRandomUtils.class);
	}
	
	@Test
	public void testCreationMethod() {
		when(instance.getActivityCount()).thenReturn(MODE_COUNTS.length);
		for(int i = 0; i < MODE_COUNTS.length; i++) {
			when(instance.getModeCount(i)).thenReturn(MODE_COUNTS[i]);
			PowerMockito.when(MrcpspMaxRandomUtils.getRandomMode(i, instance)).thenReturn(RNG_RESPONSES[i]);
		}
		PowerMockito.when(ModeAssignmentFactory.createInstance(RNG_RESPONSES, instance)).thenReturn(candidate);
		RandomBmapFactory testee = new RandomBmapFactory(instance);
		IModeAssignment result = testee.generateRandomCandidate(rng);
		
		assertEquals(candidate, result);
	}
}
