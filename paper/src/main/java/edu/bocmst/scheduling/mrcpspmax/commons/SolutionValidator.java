package edu.bocmst.scheduling.mrcpspmax.commons;

import edu.bocmst.graph.IDirectedEdge;
import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IRcpspMaxInstance;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public abstract class SolutionValidator {

	public static boolean isTimeValid(int[] startTimes, int[] modes, IMrcpspMaxInstance instance) {
		for(IDirectedEdge edge : instance.getAonNetworkEdges()) {
			int source = edge.getSource();
			int sourceMode = modes[source];
			int target = edge.getTarget();
			int targetMode = modes[target];
			int timeLag = instance.getTimeLag(source, sourceMode, target, targetMode);
			if(startTimes[target] - startTimes[source] < timeLag) {
				return false;
			}
		}
		return true;
	}

	public static boolean isTimeValid(
			int[] startTimes,
			IRcpspMaxInstance instance) {
		for(IDirectedEdge edge : instance.getAonNetwork().getEdges()) {
			int source = edge.getSource();
			int target = edge.getTarget();
			int timeLag = instance.getAdjacencyMatrix()[source][target];
			if(startTimes[target] - startTimes[source] < timeLag) {
				return false;
			}
		}
		return true;
	}
}
