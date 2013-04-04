package edu.bocmst.scheduling.mrcpspmax.ga.recombination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import edu.bocmst.metaheuristic.ListOrderCrossoverHelper;
import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.candidate.MrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.IPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.RandomKeysPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.utils.RandomUtils;

public class MrcpspMaxListOrderRecombination implements EvolutionaryOperator<IMrcpspMaxCandidate> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MrcpspMaxListOrderRecombination.class);
	
	private final ListOrderCrossoverHelper<Integer> listOrderCrossoverHelper = new ListOrderCrossoverHelper<Integer>();
	private final IMrcpspMaxInstance problem;
	
	public MrcpspMaxListOrderRecombination(IMrcpspMaxInstance problem) {
		this.problem = problem;
	}

	@Override
	public List<IMrcpspMaxCandidate> apply(
			List<IMrcpspMaxCandidate> selectedCandidates, Random rng) {
		LOGGER.debug("recombine selected candidates: {}", Arrays.toString(selectedCandidates.toArray()));
		List<IMrcpspMaxCandidate> newCandidates = Lists.newArrayList();
		for (int i = 0; i < selectedCandidates.size() / 2; i++) {
			IMrcpspMaxCandidate candidate1 = selectedCandidates.get(2* i);
			IMrcpspMaxCandidate candidate2 = selectedCandidates.get(2*i + 1);
			LOGGER.debug("recombine candidate {} and {}", candidate1, candidate2);
			List<IMrcpspMaxCandidate> recombined = recombine(candidate1, candidate2);
			newCandidates.addAll(recombined);
		}
		LOGGER.debug("return new candidate: {}", Arrays.toString(newCandidates.toArray()));
		return newCandidates;
	}

	private List<IMrcpspMaxCandidate> recombine(
			IMrcpspMaxCandidate candidate1,
			IMrcpspMaxCandidate candidate2) {
		List<IMrcpspMaxCandidate> recombined = Lists.newArrayListWithCapacity(2);
		ImmutableList<Integer> priorityList1 = candidate1.getPriorityRule().getIntegerListRepresentation();
		ImmutableList<Integer> priorityList2 = candidate2.getPriorityRule().getIntegerListRepresentation();
		int point1 = RandomUtils.getInstance().nextInt(priorityList1.size());
		int point2 = RandomUtils.getInstance().nextInt(priorityList2.size());
		List<List<Integer>> priorityListRecombined = listOrderCrossoverHelper.mateWithCrossoverPoints(
				priorityList1, 
				priorityList2, 
				point1, 
				point2);
		
		int[] modes1 = candidate1.getModeAssignment().getModeArray();
		int[] modes2 = candidate2.getModeAssignment().getModeArray();
		List<int[]> newModes = arrayRecombination(modes1, modes2, point1, point2);
		
		IPriorityRule newRule1 = new RandomKeysPriorityRule(priorityListRecombined.get(0));
		IModeAssignment newAssignment1 = ModeAssignmentFactory.createInstance(newModes.get(0), problem);
		IMrcpspMaxCandidate recombined1 = new MrcpspMaxCandidate(newRule1, newAssignment1, candidate1.getScheduler());
		recombined.add(recombined1);
		IPriorityRule newRule2 = new RandomKeysPriorityRule(priorityListRecombined.get(1));
		IModeAssignment newAssignment2 = ModeAssignmentFactory.createInstance(newModes.get(1), problem);
		IMrcpspMaxCandidate recombined2 = new MrcpspMaxCandidate( newRule2, newAssignment2, candidate2.getScheduler());
		recombined.add(recombined2);
		
		return recombined;
	}
	
	private ArrayList<int[]> arrayRecombination(int[] parent1,
            int[] parent2, int point1, int point2) {
		int[] offspring1 = new int[parent1.length];
        System.arraycopy(parent1, 0, offspring1, 0, parent1.length);
        int[] offspring2 = new int[parent2.length];
        System.arraycopy(parent2, 0, offspring2, 0, parent2.length);
		int[] temp = new int[parent1.length];
        for (int crossoverIndex : new int[] {point1, point2})
        {
            System.arraycopy(offspring1, 0, temp, 0, crossoverIndex);
            System.arraycopy(offspring2, 0, offspring1, 0, crossoverIndex);
            System.arraycopy(temp, 0, offspring2, 0, crossoverIndex);
        }
        
        return Lists.newArrayList(offspring1, offspring2);
	}

}
