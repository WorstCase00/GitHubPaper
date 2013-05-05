package edu.bocmst.scheduling.mrcpspmax.metric;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.uncommons.watchmaker.framework.EvaluatedCandidate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.bocmst.scheduling.mrcpspmax.candidate.IMrcpspMaxCandidate;
import edu.bocmst.scheduling.mrcpspmax.candidate.schedule.Schedule;
import edu.bocmst.scheduling.mrcpspmax.commons.MrcpspMaxUtils;

public class UniqueStartTimeArraysSumMetric implements IMrcpspMaxPopulationMetric {

	private static final String NAME = "validStartTimeArrayHashSum";
	private Set<Integer> startTimeHashes = Sets.newHashSet();
	
	@Override
	public Metric calculateMetric(
			List<EvaluatedCandidate<IMrcpspMaxCandidate>> population) {
		List<EvaluatedCandidate<IMrcpspMaxCandidate>> validCandidates = Lists.newArrayList(MrcpspMaxUtils.getValidCandidates(population));
		for(EvaluatedCandidate<IMrcpspMaxCandidate> candidate : validCandidates) {
			Schedule schedule = candidate.getCandidate().getScheduler().createSchedule(candidate.getCandidate().getModeAssignment(), candidate.getCandidate().getPriorityRule());
			if(schedule == null) {
				continue;
			}
			int[] startTimes = schedule.getStartTimes();
			startTimeHashes.add(Arrays.hashCode(startTimes));
		}
		Metric metric = new Metric(NAME, (double) startTimeHashes.size());
		return metric;
	}

}
