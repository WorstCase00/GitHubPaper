package edu.bocmst.scheduling.mrcpspmax.ga.evaluation;

import java.util.List;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import edu.bocmst.metaheuristic.IGeneratedSolutionsCounter;
import edu.bocmst.scheduling.mrcpspmax.MrcpspMaxSolution;
import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.IPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.RandomKeysPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;
import edu.bocmst.scheduling.mrcpspmax.commons.SolutionValidator;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.scheduler.IRcpspMaxScheduler;
import edu.bocmst.scheduling.mrcpspmax.search.IMrcpspMaxSearch;
import edu.bocmst.scheduling.mrcpspmax.search.RandomizedMmdjMaxSearch;
import edu.bocmst.scheduling.mrcpspmax.search.RandomizedMmdjSearch;

public class MrcpspMaxFitnessEvaluator implements FitnessEvaluator<IMrcpspMaxCandidate> {

	private IMrcpspMaxInstance instance;
	public MrcpspMaxFitnessEvaluator(
			IGeneratedSolutionsCounter solutionsCounter, IMrcpspMaxInstance problem) {
		this.solutionsCounter = solutionsCounter;
		this.instance = problem;
	}

	private final IGeneratedSolutionsCounter solutionsCounter;
	@Override
	public double getFitness(
			IMrcpspMaxCandidate candidate,
			List<? extends IMrcpspMaxCandidate> population) {
		
		IModeAssignment modeAssignment = candidate.getModeAssignment();
		if(modeAssignment.isResourceFeasible() && modeAssignment.isTimeFeasible()) {
			IPriorityRule priorityRule = candidate.getPriorityRule();
			IRcpspMaxScheduler scheduler = candidate.getScheduler();
			Schedule schedule = scheduler.createSchedule(modeAssignment, priorityRule);
			if((schedule != null) && SolutionValidator.isTimeValid(schedule.getStartTimes(), modeAssignment.getModeArray(), instance)) {
				solutionsCounter.increment();
				

				IMrcpspMaxSearch search = new RandomizedMmdjSearch(instance);
				MrcpspMaxSolution searchResult = search.search(candidate.getModeAssignment(), schedule);
				
//				return searchResult.getSchedule().getMakespan();
				
				schedule = searchResult.getSchedule();
				candidate.setPriorityRule(RandomKeysPriorityRule.toPriorityRule(schedule.getStartTimes()));
				candidate.setModeAssignment(ModeAssignmentFactory.createInstance(searchResult.getModes(), instance));
				return schedule.getMakespan();
			}
		} 
		return Double.MAX_VALUE/2;
	}

	@Override
	public boolean isNatural() {
		return false;
	}

}
