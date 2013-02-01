package edu.bocmst.utils;

import org.junit.Test;

import edu.bocmst.utils.IntArrays;
import static org.junit.Assert.*;


public class IntArraysTest {

	private static final int[] ARRAY1 	= new int[] {1, 2, 3};
	private static final int[] ARRAY2 	= new int[] {4, 5, 6};
	private static final int[] SUM		= new int[] {5, 7, 9};
	private static final int[] DIFF		= new int[] {-3, -3, -3};
	
	@Test
	public void testSummationOk() {
		int[] result = IntArrays.plus(ARRAY1, ARRAY2);
		
		assertArrayEquals(SUM, result);
	}
	
	@Test
	public void testDifferenceOk() {
		int[] result = IntArrays.minus(ARRAY1, ARRAY2);
		
		assertArrayEquals(DIFF, result);
	}
	
	@Test(expected = IllegalArgumentException.class) 
	public void testExceptionSummation() {
		IntArrays.plus(ARRAY1, new int[] {1});
	}
	
	@Test(expected = IllegalArgumentException.class) 
	public void testExceptionMinus() {
		IntArrays.minus(ARRAY1, new int[] {1});
	}
}
