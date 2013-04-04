package edu.bocmst.scheduling.mrcpspmax.search;

import org.junit.Assert;
import org.junit.Test;

import edu.bocmst.scheduling.mrcpspmax.MrcpspMaxSolution;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IRcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.RcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.RcpspMaxInstanceTest;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.IResourceProfile;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.ResourceProfileListImpl;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.loader.InstanceLoader;

public class MmdjMaxSearchTest {
	@Test
	public void testBarriosInstanceShift() throws Exception {
//		MrcpspMaxInstance mi = InstanceHandler.loadInstance(new File("instances/mmdjmaxTest"));
//
//		MMDJmax testee = MMDJmax.createRightLeftInstance();
//		int[] modes = new int[] {1,1,1,1,1,1};
//		ModeAssignment modeAssignmentCandidate = new ModeAssignment(modes, mi);
//
//		AbstractMrcpspMaxCandidate input = new MrcpspMaxActivityListCandidate(modeAssignmentCandidate, null, null, null);
//		Schedule inputSchedule = new Schedule(6, new int[] {3});
//		inputSchedule.startTimes = new int[] {0,0,0,2,5,7};
//		inputSchedule.bindResources(0, 2, new int[] {2});
//		inputSchedule.bindResources(2, 3, new int[] {2});
//		inputSchedule.bindResources(5, 2, new int[] {2});
//		input.setSchedule(inputSchedule );
//		LocalSearchResult result = testee.executeLocalSearch(modes, inputSchedule, mi);
//
//		Assert.assertArrayEquals(new int[] {0,0,0,1,3,5}, result.getSchedule().startTimes);
	}
	
	@Test
	public void testLeftshift() throws Exception{
		// NOTE: fragile test breaks, if behavior for breaking ties between execution modes is changed (right now: take last examined)
		IMrcpspMaxInstance instance = InstanceLoader.loadInstance("src/test/resources/mmdjmaxLeftTest.sch");

		MmdjMaxSearch testee = new MmdjMaxSearch(instance);
		int[] modes = new int[] {1,1,1,1,1,1};
		IModeAssignment modeAssignmentCandidate = ModeAssignmentFactory.createInstance(modes, instance);

		int[] startTimes = new int[] {0,0,0,2,5,7};
		IRcpspMaxInstance rcpsp = RcpspMaxInstance.createInstance(modes, instance);
		IResourceProfile resourceProfile = new ResourceProfileListImpl(rcpsp);
		Schedule schedule = new Schedule(startTimes, resourceProfile);
		
		MrcpspMaxSolution result = testee.search(modeAssignmentCandidate, schedule);

		int[] resultSchedule = result.getSchedule().getStartTimes();
		Assert.assertArrayEquals(new int[] {0,0,0,1,3,5}, resultSchedule); 
	}
}
