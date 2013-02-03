package edu.bocmst.scheduling.mrcpspmax.bmap.ga.evaluation;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import edu.bocmst.graph.IDirectedEdge;
import edu.bocmst.metaheuristic.IGeneratedSolutionsCounter;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public class PenalizingEdgeSumEvaluator implements FitnessEvaluator<IModeAssignment> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PenalizingEdgeSumEvaluator.class);
	private static final double RESOURCE_PENALTY = Double.MAX_VALUE;
	private static final double TIME_PENALTY = Double.MAX_VALUE / 2;

	private final IMrcpspMaxInstance problem;
	private final IGeneratedSolutionsCounter solutionsCounter;

	public PenalizingEdgeSumEvaluator(IMrcpspMaxInstance problem, IGeneratedSolutionsCounter solutionsCounter) {
		this.problem = problem;
		this.solutionsCounter = solutionsCounter;
	}

	public double getFitness(IModeAssignment candidate,
			List<? extends IModeAssignment> population) {
		if(!candidate.isResourceFeasible()) {
			LOGGER.debug("candidate not resource feasible - fitness value: {}", RESOURCE_PENALTY);
			return RESOURCE_PENALTY;
		} else if(!candidate.isTimeFeasible()) {
			LOGGER.debug("candidate not resource feasible - fitness value: {}", RESOURCE_PENALTY);
			return TIME_PENALTY;
		} else {
			solutionsCounter.increment();
			double edgeWeightSum = calculateTimeLagSum(candidate);
			LOGGER.debug("valid mode assignment with edge weight sum: {}", edgeWeightSum);
			return edgeWeightSum;
		}
	}

	private double calculateTimeLagSum(IModeAssignment candidate) {
		Set<IDirectedEdge> edges = problem.getAonNetworkEdges();
		int[] modes = candidate.getModeArray();
		double edgeWeightSum = 0;
		for(IDirectedEdge edge : edges) {
			int source = edge.getSource();
			int sourceMode = modes[source];
			int target = edge.getTarget();
			int targetMode = modes[target];
			int timeLag = problem.getTimeLag(source, sourceMode, target, targetMode);
			edgeWeightSum += timeLag;
		}
		return edgeWeightSum;
	}

	public boolean isNatural() {
		return false;
	}

}
