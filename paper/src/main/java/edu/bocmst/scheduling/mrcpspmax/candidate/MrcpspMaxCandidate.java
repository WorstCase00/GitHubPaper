package edu.bocmst.scheduling.mrcpspmax.candidate;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.IPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.scheduler.IRcpspMaxScheduler;

public class MrcpspMaxCandidate implements IMrcpspMaxCandidate {

	private final IPriorityRule priorityRule;
	private final IModeAssignment modeAssignment;
	private final IRcpspMaxScheduler scheduler;
	
	public MrcpspMaxCandidate(
			IPriorityRule priorityRule,
			IModeAssignment modeAssignment, 
			IRcpspMaxScheduler scheduler) {
		this.priorityRule = priorityRule;
		this.modeAssignment = modeAssignment;
		this.scheduler = scheduler;
	}

	@Override
	public IPriorityRule getPriorityRule() {
		return this.priorityRule;
	}

	@Override
	public IModeAssignment getModeAssignment() {
		return this.modeAssignment;
	}

	@Override
	public IRcpspMaxScheduler getScheduler() {
		return this.scheduler;
	}

}
