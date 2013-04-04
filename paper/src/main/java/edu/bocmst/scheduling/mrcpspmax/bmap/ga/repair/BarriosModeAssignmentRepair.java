package edu.bocmst.scheduling.mrcpspmax.bmap.ga.repair;

import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public class BarriosModeAssignmentRepair implements EvolutionaryOperator<IModeAssignment> {

	private final EvolutionPipeline<IModeAssignment> repairs;
	
	protected BarriosModeAssignmentRepair(EvolutionPipeline<IModeAssignment> repairs) {
		super();
		this.repairs = repairs;
	}

	@Override
	public List<IModeAssignment> apply(
			List<IModeAssignment> selectedCandidates, 
			Random rng) {
		return repairs.apply(selectedCandidates, rng);
	}

	public static BarriosModeAssignmentRepair createInstance(IMrcpspMaxInstance instance) {
		EvolutionaryOperator<IModeAssignment> resourceRepair = new BarriosModeAssignmentResourceRepair(instance);
		EvolutionaryOperator<IModeAssignment> cycleRepair = new BarriosModeAssignmentCycleRepair(instance);
		@SuppressWarnings("unchecked")
		List<EvolutionaryOperator<IModeAssignment>> operators = Lists.newArrayList(
				resourceRepair,
				cycleRepair);
		EvolutionPipeline<IModeAssignment> pipeline = new EvolutionPipeline<IModeAssignment>(operators);
		BarriosModeAssignmentRepair operator = new BarriosModeAssignmentRepair(pipeline);
		return operator;
	}

}
