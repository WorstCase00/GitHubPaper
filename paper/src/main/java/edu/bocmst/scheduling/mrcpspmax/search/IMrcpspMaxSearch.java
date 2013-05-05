package edu.bocmst.scheduling.mrcpspmax.search;

import edu.bocmst.scheduling.mrcpspmax.MrcpspMaxSolution;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;

public interface IMrcpspMaxSearch {
	 MrcpspMaxSolution search(
				IModeAssignment modeAssignment,
				Schedule schedule);
}
