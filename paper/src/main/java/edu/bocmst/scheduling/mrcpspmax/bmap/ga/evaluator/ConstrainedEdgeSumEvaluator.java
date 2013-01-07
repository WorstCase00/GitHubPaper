package edu.bocmst.scheduling.mrcpspmax.bmap.ga.evaluator;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import edu.bocmst.scheduling.mrcpspmax.bmap.solution.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.INetworkEdge;
import edu.bocmst.scheduling.mrcpspmax.instance.loader.IAonNetwork;

public class ConstrainedEdgeSumEvaluator implements
		FitnessEvaluator<IModeAssignment> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConstrainedEdgeSumEvaluator.class);
	private static final double RESOURCE_PENALTY = Double.MAX_VALUE;
	private static final double TIME_PENALTY = Double.MAX_VALUE / 2;
	private final IMrcpspMaxInstance problem;

	public ConstrainedEdgeSumEvaluator(IMrcpspMaxInstance problem) {
		this.problem = problem;
	}

	public double getFitness(IModeAssignment candidate,
			List<? extends IModeAssignment> population) {
		if(!candidate.isResourceFeasible()) {
			LOGGER.debug("candidate not resource feasible - fitness value: {}", RESOURCE_PENALTY);
			return RESOURCE_PENALTY;
		} else if(!candidate.isResourceFeasible()) {
			LOGGER.debug("candidate not resource feasible - fitness value: {}", RESOURCE_PENALTY);
			return TIME_PENALTY;
		}
		double edgeWeightSum = calculateTimeLagSum(candidate);
		return edgeWeightSum;
	}

	private double calculateTimeLagSum(IModeAssignment candidate) {
		IAonNetwork aonNetwork = problem.getAonNetwork();
		Set<INetworkEdge> edges = aonNetwork.getEdges();
		int[] modes = candidate.getModeArray();
		double edgeWeightSum = 0;
		for(INetworkEdge edge : edges) {
			int source = edge.getSourceActivity();
			int sourceMode = modes[source];
			int target = edge.getTargetActivity();
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
