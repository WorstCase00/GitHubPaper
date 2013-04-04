package edu.bocmst.scheduling.mrcpspmax.commons;

import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

import org.uncommons.watchmaker.framework.EvaluatedCandidate;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import edu.bocmst.graph.IDirectedEdge;
import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.INonRenewableResource;

public abstract class MrcpspMaxUtils {

	private MrcpspMaxUtils() {}
	
	public static boolean isModeAssignmentResourceValid(
			int[] modes, 
			IMrcpspMaxInstance instance) {
		int[] remainingResourcesVector = calculateResourceRemainingVector(modes, instance);
		if(Ints.min(remainingResourcesVector) >= 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isModeAssignmentTimeValid(
			int[] modes, 
			IMrcpspMaxInstance instance) {
		int[][] adjacencyMatrix = GraphUtils.getAdjacencyMatrix(modes, instance);
		int[][] floydMatrix = GraphUtils.floydWarshallLongestPathWithoutPositiveCycleDetection(adjacencyMatrix);
		for(int i = 0; i < floydMatrix.length; i++) {
			if(floydMatrix[i][i] > 0) {
				return false;
			}
		}
		return true;
	}
	
	public static int[] calculateResourceRemainingVector(
			int[] modes,
			IMrcpspMaxInstance instance) {
		List<INonRenewableResource> resources = instance.getNonRenewableResourceList();
		int[] remainingResources = new int[resources.size()];
		for(int resourceIndex = 0; resourceIndex < remainingResources.length; resourceIndex++) {
			INonRenewableResource resource = resources.get(resourceIndex);
			int consumptionsSum = 0;
			for(int activity = 0; activity < modes.length; activity ++) {
				int consumption = instance.getNonRenewableResourceConsumption(
						activity,
						modes[activity],
						resourceIndex);
				consumptionsSum += consumption;
			}
			int supply = resource.getSupply();
			remainingResources[resourceIndex] = supply - consumptionsSum;
		}
		return remainingResources;
	}

	public static int[] getNonRenewableConsumptionVector(
			int activity, 
			int mode,
			IMrcpspMaxInstance instance) {
		int[] actualConsumption = new int[instance.getNonRenewableResourceCount()];
		for(int resource = 0; resource < actualConsumption.length; resource ++) {
			int consumption = instance.getNonRenewableResourceConsumption(activity, mode, resource);
			actualConsumption[resource] = consumption;
		}
		return actualConsumption;
	}
	
	// might be extracted to some kind of candidate util class
	public static List<IModeAssignment> extractModeAssignments(
			List<IMrcpspMaxCandidate> selectedCandidates) {
		List<IModeAssignment> modeAssignments = Lists.newArrayListWithCapacity(selectedCandidates.size());
		for(IMrcpspMaxCandidate candidate : selectedCandidates) {
			modeAssignments.add(candidate.getModeAssignment());
		}
		return modeAssignments;
	}

	public static Collection<EvaluatedCandidate<IMrcpspMaxCandidate>> getValidCandidates(
			Collection<EvaluatedCandidate<IMrcpspMaxCandidate>> population) {
		Collection<EvaluatedCandidate<IMrcpspMaxCandidate>> validCandidates = Lists.newArrayList();
		for(EvaluatedCandidate<IMrcpspMaxCandidate> evaluatedCandidate : population) {
			if(evaluatedCandidate.getFitness() < 10000) {  // TODO: disgusting: generate schedule too expensive?
				validCandidates.add(evaluatedCandidate);
			}
		}
		return validCandidates;
	}

	public static List<EvaluatedCandidate<IModeAssignment>> getValidCandidates(
			List<EvaluatedCandidate<IModeAssignment>> population) {
		List<EvaluatedCandidate<IModeAssignment>> validCandidates = Lists.newArrayList();
		for(EvaluatedCandidate<IModeAssignment> evaluatedCandidate : population) {
			if(evaluatedCandidate.getCandidate().isResourceFeasible() && evaluatedCandidate.getCandidate().isTimeFeasible()) {
				validCandidates.add(evaluatedCandidate);
			}
		}
		return validCandidates;
	}

	public static List<int[]> extractModeArrays(
			List<EvaluatedCandidate<IModeAssignment>> validCandidates) {
		List<int[]> modeAssignments = Lists.newArrayList();
		for(EvaluatedCandidate<IModeAssignment> evaluatedCandidate : validCandidates) {
			modeAssignments.add(evaluatedCandidate.getCandidate().getModeArray());
		}
		return modeAssignments;
	}

	public static boolean isScheduleTimeValid(int[] startTimes, int[] modes,
			IMrcpspMaxInstance instance) {
		for(IDirectedEdge edge : instance.getAonNetworkEdges()) {
			int source = edge.getSource();
			int target = edge.getTarget();
			int weight = instance.getTimeLag(source, modes[source], target, modes[target]);
			if(startTimes[target] - startTimes[source] < weight) {
				return false;
			}
		}
		return true;
	}

	public static List<Integer> getActivitiesOrderedByStartTimeDecreasing(
			int[] startTimes) {
		PriorityQueue<ActivityToStartTimeEntry> orderedEntries = 
			new PriorityQueue<ActivityToStartTimeEntry>(startTimes.length, new DecreasingStartTimeComparator());
		for (int activity = 0; activity < startTimes.length; activity++) {
			ActivityToStartTimeEntry entry = new ActivityToStartTimeEntry(activity, startTimes[activity]);
			orderedEntries.add(entry);
		}
		List<Integer> orderedList = extractOrderedActivities(orderedEntries);
		return orderedList;
	}

	private static List<Integer> extractOrderedActivities(
			PriorityQueue<ActivityToStartTimeEntry> orderedEntries) {
		List<Integer> orderedActivities = Lists.newArrayList();
		while(!orderedEntries.isEmpty()) {
			orderedActivities.add(orderedEntries.poll().getActivity());
		}
		return orderedActivities;
	}
}
