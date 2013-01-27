package edu.bocmst.scheduling.mrcpspmax.commons;

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
}
