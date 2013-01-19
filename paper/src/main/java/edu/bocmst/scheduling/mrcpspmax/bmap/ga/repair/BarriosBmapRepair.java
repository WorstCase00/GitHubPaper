package edu.bocmst.scheduling.mrcpspmax.bmap.ga.repair;

import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public class BarriosBmapRepair implements EvolutionaryOperator<IModeAssignment> {

	private final EvolutionPipeline<IModeAssignment> repairs;
	
	protected BarriosBmapRepair(EvolutionPipeline<IModeAssignment> repairs) {
		super();
		this.repairs = repairs;
	}

	@Override
	public List<IModeAssignment> apply(
			List<IModeAssignment> selectedCandidates, 
			Random rng) {
		return repairs.apply(selectedCandidates, rng);
	}

	public static BarriosBmapRepair createInstance(IMrcpspMaxInstance instance) {
		EvolutionaryOperator<IModeAssignment> resourceRepair = new BarriosBmapResourceRepair(instance);
		EvolutionaryOperator<IModeAssignment> cycleRepair = new BarriosBmapCycleRepair(instance);
		@SuppressWarnings("unchecked")
		List<EvolutionaryOperator<IModeAssignment>> operators = Lists.newArrayList(
				resourceRepair,
				cycleRepair);
		EvolutionPipeline<IModeAssignment> pipeline = new EvolutionPipeline<IModeAssignment>(operators);
		BarriosBmapRepair operator = new BarriosBmapRepair(pipeline);
		return operator;
	}

}
