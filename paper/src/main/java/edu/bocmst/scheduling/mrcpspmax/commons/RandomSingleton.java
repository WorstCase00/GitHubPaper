package edu.bocmst.scheduling.mrcpspmax.commons;

import java.util.Random;

import org.uncommons.maths.random.MersenneTwisterRNG;

public abstract class RandomSingleton {
	
	private RandomSingleton() {}
	
	private static final Random INSTANCE = new MersenneTwisterRNG();

	public static Random getInstance() {
		return INSTANCE;
	}
}
