package com.uc4.scheduling.mrcpspmax.bmap;

import java.util.List;

import com.uc4.scheduling.mrcpspmax.bmap.solution.IModeAssignment;
import com.uc4.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public interface IBmapSolver {
	
	List<IModeAssignment> getRankedModeAssignments(IMrcpspMaxInstance instance);
}
