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
import edu.bocmst.utils.RandomUtils;

public class RandomizedMmdjUSearch implements IMrcpspMaxSearch {

	private static final Logger LOGGER = LoggerFactory.getLogger(RandomizedMmdjUSearch.class);
	private final IMrcpspMaxInstance instance;

	public RandomizedMmdjUSearch(IMrcpspMaxInstance instance) {
		this.instance = instance;
	}

	@Override
	public MrcpspMaxSolution search(IModeAssignment modeAssignment, Schedule schedule) {
		LOGGER.debug("start search for mode assignment {} and schedule {}", modeAssignment, schedule);
		List<IMrcpspMaxSearch> subSearch = Lists.newArrayList();
		if(RandomUtils.getInstance().nextBoolean()) {
			subSearch.add(new MmdjULeftShift(instance));
			subSearch.add(new MmdjURightShift(instance));
		} else {
			subSearch.add(new MmdjURightShift(instance));
			subSearch.add(new MmdjULeftShift(instance));
		}
		for(IMrcpspMaxSearch search : subSearch) {
			MrcpspMaxSolution solution = search.search(modeAssignment, schedule);
			schedule = solution.getSchedule();
			modeAssignment = ModeAssignmentFactory.createInstance(solution.getModes(), instance);
		}
		MrcpspMaxSolution finalSolution = new MrcpspMaxSolution(schedule, modeAssignment.getModeArray());
		return finalSolution;
	}
}
