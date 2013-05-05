package edu.bocmst.scheduling.mrcpspmax.search;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.MrcpspMaxSolution;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.ModeAssignmentFactory;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public class RandomizedMmdjSearch implements IMrcpspMaxSearch {

	private static final Logger LOGGER = LoggerFactory.getLogger(RandomizedMmdjSearch.class);
	
	private final IMrcpspMaxInstance instance;

	public RandomizedMmdjSearch(IMrcpspMaxInstance instance) {
		this.instance = instance;
	}

	@Override
	public MrcpspMaxSolution search(IModeAssignment modeAssignment, Schedule schedule) {
		LOGGER.debug("start search for mode assignment {} and schedule {}", modeAssignment, schedule);
		List<IMrcpspMaxSearch> subSearch = Lists.newArrayList();
		subSearch.add(new RandomizedMmdjMaxSearch(instance));
		subSearch.add(new RandomizedMmdjUSearch(instance));
		for(IMrcpspMaxSearch search : subSearch) {
			MrcpspMaxSolution solution = search.search(modeAssignment, schedule);
			schedule = solution.getSchedule();
			modeAssignment = ModeAssignmentFactory.createInstance(solution.getModes(), instance);
		}
		MrcpspMaxSolution finalSolution = new MrcpspMaxSolution(schedule, modeAssignment.getModeArray());
		return finalSolution;
	}

}
