package edu.bocmst.utils;

import java.util.Arrays;
import java.util.List;


public abstract class IntArrays {

	private IntArrays() {}
	
	public static int[] plus(int[] sumand1, int[] sumand2) {
		assertSameLengths(sumand1, sumand2);
		int[] sums = new int[sumand1.length];
		for(int i = 0; i < sums.length; i++) {
			sums[i] = sumand1[i] + sumand2[i];
		}
		return sums;
	}

	public static int[] minus(int[] minuend, int[] subtrahend) {
		assertSameLengths(minuend, subtrahend);
		int[] differences = new int[minuend.length];
		for(int i = 0; i < differences.length; i++) {
			differences[i] = minuend[i] - subtrahend[i];
		}
		return differences;
	}

	private static void assertSameLengths(int[] array1, int[] array2) {
		if(array1.length != array2.length) {
			throw new IllegalArgumentException("different array lengths");
		}
	}

	public static int[] getColumn(int index, int[][] matrix) {
		int[] column = new int[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			column[i] = matrix[i][index];
		}
		return column;
	}

	public static String toOneLineString(int[][] matrix) {
		StringBuilder string = new StringBuilder("[");
		for (int row = 0; row < matrix.length; row++) {
			string.append(Arrays.toString(matrix[row])).append(", ");
		}
		string.append("]");
		return string.toString();
	}

	public static int countUnique(List<int[]> arrays) {
		if(arrays.isEmpty()) {
			return 0;
		}
		int count = 1;
		for (int i = 0; i < arrays.size() - 1; i++) {
			int[] first = arrays.get(i);
			boolean foundMatch = false;
			for (int j = i + 1; j < arrays.size(); j++) {
				int[] second = arrays.get(j);
				if(Arrays.equals(first, second)) {
					foundMatch = true;
					break;
				}
			}
			if(!foundMatch) {
				count ++;
			}
		}
		return count;
	}

	public static boolean isValidTimeWindow(IntInterval temporalWindow) {
		if(
				(temporalWindow.getLowerBound() < 0) || 
				(temporalWindow.getUpperBound() < 0) || 
				(temporalWindow.getLowerBound() > temporalWindow.getUpperBound())) {
			return false;
		}
		return true;
	}
}
