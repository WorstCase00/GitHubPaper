package edu.bocmst.scheduling.mrcpspmax.ga.factory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.CandidateFactory;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.bmap.BmapSolverConfiguration;
import edu.bocmst.scheduling.mrcpspmax.bmap.BmapSolverFactory;
import edu.bocmst.scheduling.mrcpspmax.bmap.IBmapSolver;
import edu.bocmst.scheduling.mrcpspmax.bmap.ga.factory.MrcpspMaxCandidateFactoryConfiguration;
import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.candidate.MrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.IPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.IPriorityRuleCreator;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.RandomRandomKeysPriorityRuleCreator;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.scheduler.RcpspMaxSchedulerFactory;

public class BmapBasedRandomKeysCandidateFactory implements CandidateFactory<IMrcpspMaxCandidate> {

	private static final Logger LOGGER = LoggerFactory
	.getLogger(BmapBasedRandomKeysCandidateFactory.class);

	private final IMrcpspMaxInstance problem;
	private final IBmapSolver bmapSolver;
	private final IPriorityRuleCreator priorityRuleCreator;

	protected BmapBasedRandomKeysCandidateFactory(
			IMrcpspMaxInstance problem,
			IBmapSolver bmapSolver, 
			IPriorityRuleCreator priorityRuleCreator) {
		this.problem = problem;
		this.bmapSolver = bmapSolver;
		this.priorityRuleCreator = priorityRuleCreator;
	}

	public static CandidateFactory<IMrcpspMaxCandidate> createInstance(
			IMrcpspMaxInstance problem,
			MrcpspMaxCandidateFactoryConfiguration configuration) {
		BmapSolverConfiguration bmapSolverConfiguration = configuration.getBmapSolverConfiguration();
		IBmapSolver bmapSolve = BmapSolverFactory.createInstanceForInstance(problem, bmapSolverConfiguration);
		IPriorityRuleCreator ruleGenerator = new RandomRandomKeysPriorityRuleCreator(problem);
		BmapBasedRandomKeysCandidateFactory instance = new BmapBasedRandomKeysCandidateFactory(
				problem,
				bmapSolve,
				ruleGenerator);
		return instance;
	}

	@Override
	public List<IMrcpspMaxCandidate> generateInitialPopulation(
			int populationSize, Random rng) {
		List<IMrcpspMaxCandidate> population = Lists.newArrayList();
		List<IModeAssignment> bmapSolutions = bmapSolver.solve(problem);
		LOGGER.debug("bmap solutions: {}", Arrays.toString(bmapSolutions.toArray()));
		for (int i = 0; i < populationSize; i++) {
			int bmapIndex = i % bmapSolutions.size();
			IModeAssignment modeAssignment = bmapSolutions.get(bmapIndex);
			IMrcpspMaxCandidate candidate = createCandidate(modeAssignment);
			population.add(candidate);
		}
		return population;
	}

	private IMrcpspMaxCandidate createCandidate(IModeAssignment modeAssignment) {
		IPriorityRule priorityRule = priorityRuleCreator.createPriorityRuleForModeAssignment(modeAssignment);
		IMrcpspMaxCandidate candidate = new MrcpspMaxCandidate(priorityRule, modeAssignment, RcpspMaxSchedulerFactory.createInstance());
		return candidate;
	}

	@Override
	public List<IMrcpspMaxCandidate> generateInitialPopulation(
			int populationSize, Collection<IMrcpspMaxCandidate> seedCandidates,
			Random rng) {
		return generateInitialPopulation(populationSize, rng);
	}

	@Override
	public IMrcpspMaxCandidate generateRandomCandidate(Random rng) {
		throw new UnsupportedOperationException();
	}

}
