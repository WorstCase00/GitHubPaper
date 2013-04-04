package edu.bocmst.scheduling.mrcpspmax.candidate.priority;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;

public interface IPriorityRuleCreator {

	IPriorityRule createPriorityRuleForModeAssignment(
			IModeAssignment modeAssignment);

}
