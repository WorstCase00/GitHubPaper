package edu.bocmst.scheduling.mrcpspmax.bmap.ga;

import java.util.List;

import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.swing.evolutionmonitor.EvolutionMonitor;

import com.google.common.collect.Lists;

import edu.bocmst.scheduling.mrcpspmax.bmap.IBmapSolver;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public class GaBmapSolver implements IBmapSolver{

	private final EvolutionEngine<IModeAssignment> engine;
	private final int populationSize;
	private final int eliteCount;
	private final TerminationCondition terminationCondition;
	
	public GaBmapSolver(EvolutionEngine<IModeAssignment> engine,
			int populationSize, int eliteCount,
			TerminationCondition terminationCondition) {
		this.engine = engine;
		this.populationSize = populationSize;
		this.eliteCount = eliteCount;
		this.terminationCondition = terminationCondition;
	}

	public List<IModeAssignment> getRankedModeAssignments(IMrcpspMaxInstance instance) {
		EvolutionMonitor<IModeAssignment> monitor = new EvolutionMonitor<IModeAssignment>();
		monitor.showInFrame("monitor", true);
		engine.addEvolutionObserver(monitor);
		List<EvaluatedCandidate<IModeAssignment>> solutions = engine.evolvePopulation(
				populationSize, 
				eliteCount, 
				terminationCondition);
		List<IModeAssignment> modeAssignments = extractAssignments(solutions);
		return modeAssignments;
	}
	
	private List<IModeAssignment> extractAssignments(
			List<EvaluatedCandidate<IModeAssignment>> solutions) {
		List<IModeAssignment> assignments = Lists.newArrayList();
		for(EvaluatedCandidate<IModeAssignment> solution : solutions) {
			IModeAssignment assignment = solution.getCandidate();
			assignments.add(assignment);
		}
		return assignments;
	}
}
