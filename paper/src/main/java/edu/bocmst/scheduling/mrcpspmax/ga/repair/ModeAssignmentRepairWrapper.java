package edu.bocmst.scheduling.mrcpspmax.ga.repair;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.bmap.ga.repair.BarriosModeAssignmentRepair;
import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.candidate.MrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.priority.IPriorityRule;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.scheduler.IRcpspMaxScheduler;

public class ModeAssignmentRepairWrapper implements EvolutionaryOperator<IMrcpspMaxCandidate> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ModeAssignmentRepairWrapper.class);
	
	private final BarriosModeAssignmentRepair modeAssignmentRepair;
	
	protected ModeAssignmentRepairWrapper(
			BarriosModeAssignmentRepair modeAssignmentRepair) {
		this.modeAssignmentRepair = modeAssignmentRepair;
	}
	
	@Override
	public List<IMrcpspMaxCandidate> apply(
			List<IMrcpspMaxCandidate> selectedCandidates, Random rng) {
		LOGGER.debug("apply mode assignment repair to candidates: {}", Arrays.toString(selectedCandidates.toArray()));
		List<IModeAssignment> modeAssignments = MrcpspMaxUtils.extractModeAssignments(selectedCandidates);
		List<IModeAssignment> repairedModeAssignments = modeAssignmentRepair.apply(modeAssignments, rng);
		LOGGER.debug("list of repaired mode assignments: {}", Arrays.toString(repairedModeAssignments.toArray()));
		List<IMrcpspMaxCandidate> newCandidates = combineToNewCandidates(selectedCandidates, repairedModeAssignments);
		return newCandidates;
	}
	
	private List<IMrcpspMaxCandidate> combineToNewCandidates(
			List<IMrcpspMaxCandidate> selectedCandidates,
			List<IModeAssignment> repairedModeAssignments) {
		List<IMrcpspMaxCandidate> newCandidates = Lists.newArrayListWithCapacity(selectedCandidates.size());
		for (int i = 0; i < selectedCandidates.size(); i++) {
			IPriorityRule priorityRule = selectedCandidates.get(i).getPriorityRule();
			IModeAssignment modeAssignment = repairedModeAssignments.get(i);
			IRcpspMaxScheduler scheduler = selectedCandidates.get(i).getScheduler();
			IMrcpspMaxCandidate newCandidate = new MrcpspMaxCandidate(
					priorityRule, 
					modeAssignment,
					scheduler);
			newCandidates.add(newCandidate);
		}
		return newCandidates;
	}

	public static ModeAssignmentRepairWrapper createInstance(IMrcpspMaxInstance problem) {
		BarriosModeAssignmentRepair repair = BarriosModeAssignmentRepair.createInstance(problem);
		ModeAssignmentRepairWrapper instance = new ModeAssignmentRepairWrapper(repair);
		return instance;
	}

}
