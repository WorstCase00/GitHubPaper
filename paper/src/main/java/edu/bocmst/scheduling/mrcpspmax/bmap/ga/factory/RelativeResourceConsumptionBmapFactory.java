package edu.bocmst.scheduling.mrcpspmax.bmap.ga.factory;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.ModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.INonRenewableResource;

public class RelativeResourceConsumptionBmapFactory extends AbstractCandidateFactory<IModeAssignment> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RelativeResourceConsumptionBmapFactory.class);
	
	private final IMrcpspMaxInstance instance;

	public RelativeResourceConsumptionBmapFactory(IMrcpspMaxInstance instance) {
		this.instance = instance;
	}

	@Override
	public IModeAssignment generateRandomCandidate(Random rng) {
		LOGGER.debug("create relative resource consumption minimizing candidate");
		int[] modes = createModes(rng);
		IModeAssignment candidate = ModeAssignment.createInstance(modes, instance);
		return candidate;
	}

	private int[] createModes(Random rng) {
		int activityCount = instance.getActivityCount();
		List<Integer> activityList = Lists.newArrayListWithCapacity(activityCount);
		for(int activity = 0; activity < activityCount; activity++) {
			activityList.add(activity);
		}
		Collections.shuffle(activityList, rng);
		int[] modes = new int[activityCount];
		int[] partialRemainResources = initPartialRemainResources();
		for(int activity : activityList) {
			double[] relativeConsumptions = calculateAvgRelativeConsumptionsPerModes(
					activity, 
					partialRemainResources);
			List<Integer> minModes = getMinimumIndices(relativeConsumptions);
			int chosenMode = chooseMode(activity, minModes, rng);
			partialRemainResources = updatePartialRemainResources(
					partialRemainResources, 
					activity, 
					chosenMode);
			modes[activity] = chosenMode;
		}
		return modes;
	}

	private int[] updatePartialRemainResources(
			int[] partialRemainResources,
			int activity, 
			int mode) {
		for(int resource = 0; resource < partialRemainResources.length; resource ++) {
			int consumption = instance.getNonRenewableResourceConsumption(activity, mode, resource);
			partialRemainResources[resource] -= consumption;
		}
		return partialRemainResources;
	}

	private List<Integer> getMinimumIndices(double[] values) {
		List<Integer> minIndices = Lists.newArrayList();
		double minimum = Double.MAX_VALUE;
		for(int mode = 1; mode <= values.length; mode ++) {
			double value = values[mode - 1];
			if(minimum > value) {
				minimum = value;
				minIndices.clear();
				minIndices.add(mode);
			} else if(minimum == value) {
				minIndices.add(mode);
			}
		}
		return minIndices;
	}

	private double[] calculateAvgRelativeConsumptionsPerModes(
			int activity,
			int[] partialRemainResources) {
		int modeCount = instance.getModeCount(activity);
		double[] relativeAvgConsumptionsPerMode = new double[modeCount];
		for(int mode = 1; mode <= modeCount; mode ++) {
			double relativeAvgConsumption = calculateAvgRelativeConsumption(
					activity, 
					mode, 
					partialRemainResources);
			relativeAvgConsumptionsPerMode[mode - 1] = relativeAvgConsumption;
		}
		return relativeAvgConsumptionsPerMode;
	}

	private double calculateAvgRelativeConsumption(
			int activity, 
			int mode,
			int[] partialRemainResources) {
		double avgRelativeConsumption = 0;
		for(int resource = 0; resource < partialRemainResources.length; resource ++) {
			int consumption = instance.getNonRenewableResourceConsumption(activity, mode, resource);
			double relativeConsumption = (double) consumption / partialRemainResources[resource];
			avgRelativeConsumption += relativeConsumption;
		}
		return avgRelativeConsumption;
	}

	private int chooseMode(
			int activity,
			List<Integer> minModes, 
			Random rng) {
		int chosenMode;
		if(minModes.size() == 1) {
			chosenMode = minModes.get(0);
		} else {
			chosenMode = getModeWithMinimumProcessingTimeOrRandom(activity, minModes, rng);
		}
		return chosenMode;
	}

	private int getModeWithMinimumProcessingTimeOrRandom(
			int activity,
			List<Integer> modes, 
			Random rng) {
		int min = Integer.MAX_VALUE;
		List<Integer> minModes = Lists.newArrayList();
		for(int mode : modes) {
			int processingTime = instance.getProcessingTime(activity, mode);
			if(min > processingTime) {
				minModes.clear();
				minModes.add(mode);
			} else if(min == processingTime) {
				minModes.add(mode);
			}
		}
		if(minModes.size() == 1) {
			return minModes.get(0);
		}
		int randomIndex = rng.nextInt(minModes.size());
		return minModes.get(randomIndex);
	}

	private int[] initPartialRemainResources() {
		List<INonRenewableResource> resourcesList = instance.getNonRenewableResourceList();
		int[] resources = new int[resourcesList.size()];
		for(int resource = 0; resource < resources.length; resource ++) {
			resources[resource] = resourcesList.get(resource).getSupply();
		}
		return resources;
	}

}
