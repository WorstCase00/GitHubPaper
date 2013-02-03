package edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment;

import org.junit.Test;

import edu.bocmst.scheduling.mrcpspmax.TestConstants;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IRcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.RcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import static org.junit.Assert.*;

public class RcpspMaxInstanceTest {

	private static final int[] PROC_TIMES = new int[] {0,6,1,2,1,2,4,0};
	private static final int[][] RENEW_RESOURCES = new int[][] {{0},{2},{2},{1},{1},{2},{1},{0}};
	
	@Test
	public void testInstanceCreation() {
		int[] modes = new int[] {1,2,2,3,3,1,3,1};
		IMrcpspMaxInstance instance = TestConstants.THESIS_INSTANCE;
		IRcpspMaxInstance result = RcpspMaxInstance.createInstance(modes, instance);
		
		for(int activity = 0; activity < modes.length; activity ++) {
			assertEquals(PROC_TIMES[activity], result.getProcessingTime(activity));
			assertArrayEquals(RENEW_RESOURCES[activity], result.getRenewableResourceConsumption(activity));
		}
	}
}
