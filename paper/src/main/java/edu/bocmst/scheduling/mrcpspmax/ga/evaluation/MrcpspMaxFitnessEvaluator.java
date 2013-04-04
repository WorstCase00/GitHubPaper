package edu.bocmst.scheduling.mrcpspmax.ga.evaluation;

import java.util.List;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import edu.bocmst.metaheuristic.IGeneratedSolutionsCounter;
import edu.bocmst.scheduling.mrcpspmax.MrcpspMaxSolution;
import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.IPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.scheduler.IRcpspMaxScheduler;
import edu.bocmst.scheduling.mrcpspmax.search.MmdjMaxSearch;

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
			if(schedule != null) {
				solutionsCounter.increment();
				

				MmdjMaxSearch search = new MmdjMaxSearch(instance);
				MrcpspMaxSolution searchResult = search.search(candidate.getModeAssignment(), schedule);
				schedule = searchResult.getSchedule();
				
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
