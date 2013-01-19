package edu.bocmst.scheduling.mrcpspmax.bmap;

import java.util.List;

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public interface IBmapSolver {
	
	List<IModeAssignment> getRankedModeAssignments(IMrcpspMaxInstance instance);
}
