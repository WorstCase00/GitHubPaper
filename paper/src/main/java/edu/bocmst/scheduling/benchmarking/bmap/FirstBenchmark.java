package edu.bocmst.scheduling.benchmarking.bmap;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.bocmst.scheduling.mrcpspmax.bmap.BmapSolverConfiguration;
import edu.bocmst.scheduling.mrcpspmax.bmap.BmapSolverFactory;
import edu.bocmst.scheduling.mrcpspmax.bmap.IBmapSolver;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.ActivityListPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.IPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxRandomUtils;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.loader.InstanceLoader;
import edu.bocmst.scheduling.mrcpspmax.scheduler.IRcpspMaxScheduler;
import edu.bocmst.scheduling.mrcpspmax.scheduler.RcpspMaxSchedulerFactory;

public class FirstBenchmark implements IBmapBenchmark {


	private static final Logger LOGGER = LoggerFactory.getLogger(FirstBenchmark.class);
	private static final String PATH = "instances/100/psp%d.sch";

	@Override
	public List<IBenchmarkStatistic> performBenchmarks(
			List<BmapSolverConfiguration> solverConfigurations,
			Collection<IMrcpspMaxInstance> instances) {
		for(BmapSolverConfiguration configuration : solverConfigurations) {
			for(IMrcpspMaxInstance instance : instances) {
				IBmapSolver solver = BmapSolverFactory.createInstanceForInstance(instance, configuration);
				List<IModeAssignment> modeAssignments = solver.solve(instance);
				LOGGER.info("resource valid mode assignments: {} / {}", countResourceValid(modeAssignments), modeAssignments.size());
				LOGGER.info("time valid mode assignments: {} / {}", countTimeValid(modeAssignments), modeAssignments.size());
				Set<Integer> modesHashes = Sets.newHashSet();
				for(IModeAssignment assignment : modeAssignments) {
					modesHashes.add(Arrays.hashCode(assignment.getModeArray()));
					IRcpspMaxScheduler scheduler = RcpspMaxSchedulerFactory.createInstance();
					List<Integer> randomList = MrcpspMaxRandomUtils.getRandomActivityPermutation(instance);
					ImmutableList<Integer> activityList = ImmutableList.copyOf(randomList);
					IPriorityRule priorityRule = new ActivityListPriorityRule(activityList);
					LOGGER.info("schedule: {}", scheduler.createSchedule(assignment, priorityRule));
				}
				LOGGER.info("# different assignments: {}", modesHashes.size());
			}
		}
		return null;
	}

	private int countTimeValid(List<IModeAssignment> modeAssignments) {
		int count = 0;
		for(IModeAssignment candidate : modeAssignments) {
			if(candidate.isTimeFeasible()) {
				count ++;
			}
		}
		return count;
	}

	private int countResourceValid(List<IModeAssignment> modeAssignments) {
		int count = 0;
		for(IModeAssignment candidate : modeAssignments) {
			if(candidate.isResourceFeasible()) {
				count ++;
			}
		}
		return count;
	}

	public static void main(String[] args) throws IOException {
		FirstBenchmark benchmark = new FirstBenchmark();
		List<BmapSolverConfiguration> solverConfigurations = Lists.newArrayList(BmapSolverConfiguration.createDefault());
		for(int i = 1; i < 270; i++) {
			Collection<IMrcpspMaxInstance> instances = Lists.newArrayList(
					InstanceLoader.loadInstance(String.format(PATH, i)));
			List<IBenchmarkStatistic> result = benchmark.performBenchmarks(solverConfigurations, instances);
			LOGGER.info("result: {}", Arrays.toString(result.toArray()));
		}
	}

}
