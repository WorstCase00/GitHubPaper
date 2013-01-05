package com.uc4.scheduling.benchmarking;

import java.io.FileNotFoundException;
import java.util.PriorityQueue;

public class ComparisonScript {

	public static void main(String[] args) throws FileNotFoundException {
		double[] lbs = NumberArrayFileLoader.loadNumberArray(FileNameConstants.LBS_100);
		double[] ballestin = NumberArrayFileLoader.loadNumberArray(FileNameConstants.BALLESTIN_100);
		printAverageDeviation(lbs, ballestin);

		double[] best10k = NumberArrayFileLoader.loadNumberArray(FileNameConstants.BEST_100_10k_SIMILARITY);
		printAverageDeviation(lbs, best10k);

		printSumOfDifferencesInDeviation(ballestin, best10k);
		
		PriorityQueue<DeviationEntry> deviations = createOrderedDeviationsQueue(ballestin, best10k);
		printQueue(deviations);
		
		SignificanceTests.evaluateSignificance(lbs, best10k, ballestin);
	}

	private static void printQueue(PriorityQueue<DeviationEntry> deviations) {
		while(!deviations.isEmpty()) {
			System.out.println(deviations.poll());
		}
	}

	private static PriorityQueue<DeviationEntry> createOrderedDeviationsQueue(
			double[] ballestin,
			double[] best10k) {
		PriorityQueue<DeviationEntry> orderedDeviations = new PriorityQueue<DeviationEntry>(270, new DeviationEntryComparator());
		for(int i = 0; i < 270; i++) {
			DeviationEntry devEntry = new DeviationEntry(i, best10k[i], ballestin[i]);
			orderedDeviations.add(devEntry);
		}
		return orderedDeviations;
	}

	private static void printAverageDeviation(
			double[] array1, 
			double[] array2) {
		double devSum = 0;
		for(int i = 0; i < 270; i++) {
			double deviation = Math.abs(array1[i] - array2[i]) / array1[i] * 100;
			devSum += deviation;
		}
		System.out.println("avgDev: " + devSum / 270);
	}

	private static void printSumOfDifferencesInDeviation(double[] array1, double[] array2) {
		double diffSum = 0;
		for(int i = 0; i < 270; i++) {
			double diff = array1[i] - array2[i];
			diffSum += diff;
		}
		System.out.println("diffSum: " + diffSum);
	}
}
