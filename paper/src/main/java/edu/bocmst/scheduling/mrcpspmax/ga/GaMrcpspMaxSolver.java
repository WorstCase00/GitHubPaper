package edu.bocmst.scheduling.mrcpspmax.ga;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.TerminationCondition;

import edu.bocmst.metaheuristic.AbstractGaSolver;
import edu.bocmst.scheduling.mrcpspmax.IMrcpspMaxSolver;
import edu.bocmst.scheduling.mrcpspmax.MrcpspMaxSolution;
import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;
import edu.bocmst.scheduling.mrcpspmax.scheduler.IRcpspMaxScheduler;
import edu.bocmst.scheduling.mrcpspmax.scheduler.RcpspMaxSchedulerFactory;

public class GaMrcpspMaxSolver extends AbstractGaSolver<IMrcpspMaxCandidate, MrcpspMaxSolution> implements IMrcpspMaxSolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(GaMrcpspMaxSolver.class);

	public GaMrcpspMaxSolver(EvolutionEngine<IMrcpspMaxCandidate> engine,
			int populationSize, int eliteCount,
			TerminationCondition terminationCondition) {
		super(engine, populationSize, eliteCount, terminationCondition);
	}

	@Override
	protected MrcpspMaxSolution extractSolutionFromFinalPopultation(
			List<EvaluatedCandidate<IMrcpspMaxCandidate>> solutions) {
		LOGGER.debug("extract best schedule from final population");

		Schedule bestSchedule = null;
		int[] bestModeAssignment = null;
		for(EvaluatedCandidate<IMrcpspMaxCandidate> evalutatedCandidate : solutions) {
			IRcpspMaxScheduler scheduler = RcpspMaxSchedulerFactory.createInstance();
			IMrcpspMaxCandidate candidate = evalutatedCandidate.getCandidate();
			Schedule schedule = scheduler.createSchedule(candidate.getModeAssignment(), candidate.getPriorityRule());
			if(bestSchedule == null) {
				bestSchedule = schedule;
				bestModeAssignment = candidate.getModeAssignment().getModeArray();
			} else if(
					(schedule != null) &&
					(bestSchedule.getMakespan() > schedule.getMakespan())) {
				bestSchedule = schedule;
				bestModeAssignment = candidate.getModeAssignment().getModeArray();
			}
		}
		
		return new MrcpspMaxSolution(bestSchedule, bestModeAssignment);
	}

}
