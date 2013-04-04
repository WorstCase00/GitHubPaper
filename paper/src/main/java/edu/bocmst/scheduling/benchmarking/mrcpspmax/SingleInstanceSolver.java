package edu.bocmst.scheduling.benchmarking.mrcpspmax;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bocmst.metaheuristic.IGeneratedSolutionsCounter;
import edu.bocmst.scheduling.mrcpspmax.IMrcpspMaxSolver;
import edu.bocmst.scheduling.mrcpspmax.MrcpspMaxSolution;
import edu.bocmst.scheduling.mrcpspmax.MrcpspMaxSolverConfiguration;
import edu.bocmst.scheduling.mrcpspmax.MrcpspMaxSolverFactory;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;
import edu.bocmst.scheduling.mrcpspmax.ga.GaMrcpspMaxSolverConfiguration;
import edu.bocmst.scheduling.mrcpspmax.ga.GaMrcpspMaxSolverFactory;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.loader.InstanceLoader;

public class SingleInstanceSolver {

	private static final Logger LOGGER = LoggerFactory
	.getLogger(SingleInstanceSolver.class);

	private static final String INSTANCE = "instances/100/psp216.sch";

	public static void main(String[] args) throws IOException {
		IMrcpspMaxInstance problem = InstanceLoader.loadInstance(INSTANCE);
		MrcpspMaxSolverConfiguration configuration = MrcpspMaxSolverConfiguration.createDefault();
		IMrcpspMaxSolver solver = MrcpspMaxSolverFactory.create(problem, configuration);
		MrcpspMaxSolution solution = solver.solve(problem);
		if(!MrcpspMaxUtils.isScheduleTimeValid(solution.getSchedule().getStartTimes(), solution.getModes(), problem)) {
			LOGGER.error("generated illegal schedule - check causal constraints tracker regarding 0 - weight edges");
			throw new RuntimeException("illegal solution");
		}
		System.out.println(solution);
	}
}
