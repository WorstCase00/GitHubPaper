package edu.bocmst.scheduling.mrcpspmax.bmap.metric;

import edu.bocmst.scheduling.mrcpspmax.candidate.modeassignment.IModeAssignment;

public class RelativeEditDistance implements ISimilarityMetric {

	@Override
	public double getSimilarityMetric(
			IModeAssignment candidate1,
			IModeAssignment candidate2) {
		double editDistance = getRelativeEditDistance(candidate1, candidate2);
		return editDistance;
	}

	private static double getRelativeEditDistance(
			IModeAssignment candidate1,
			IModeAssignment candidate2) {
		int[] modes1 = candidate1.getModeArray();
		int[] modes2 = candidate2.getModeArray();
		double relativeEditDistance = getRelativeEditDistance(modes1, modes2);
		return relativeEditDistance;
	}

	private static double getRelativeEditDistance(int[] modes1, int[] modes2) {
		int editDistance = 0;
		for(int i = 0; i < modes1.length; i++) {
			if(modes1[i] != modes2[i]) {
				editDistance ++;
			}
		}
		double relativeEditDistance = ((double) editDistance) / modes1.length;
		return relativeEditDistance;
	}
}
