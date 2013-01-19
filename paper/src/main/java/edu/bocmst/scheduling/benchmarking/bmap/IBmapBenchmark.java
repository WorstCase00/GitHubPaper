package edu.bocmst.scheduling.benchmarking.bmap;

import java.util.Collection;
import java.util.List;

import edu.bocmst.scheduling.mrcpspmax.bmap.BmapSolverConfiguration;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public interface IBmapBenchmark {

	List<IBenchmarkStatistic> performBenchmarks(
			List<BmapSolverConfiguration> solverConfigurations,
			Collection<IMrcpspMaxInstance> instances);
}
