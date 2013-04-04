package edu.bocmst.scheduling.mrcpspmax;

import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public interface IMrcpspMaxSolver {

	MrcpspMaxSolution solve(IMrcpspMaxInstance instance);
}
