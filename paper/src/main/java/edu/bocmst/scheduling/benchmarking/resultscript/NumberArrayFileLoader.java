package edu.bocmst.scheduling.benchmarking.resultscript;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class NumberArrayFileLoader {

	public static double[] loadNumberArray(String filePath) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(filePath));
		double[] array = new double[270];
		for(int i = 0; i < 270; i++) {
			array[i] = scanner.nextDouble();
		}
		return array;
	}
}
