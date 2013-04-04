package edu.bocmst.utils;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.bocmst.metaheuristic.IGeneratedSolutionsCounter;

public class AtomicCounterCounter implements IGeneratedSolutionsCounter {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AtomicCounterCounter.class);
	
	private final AtomicInteger counter = new AtomicInteger(0);
	
	@Override
	public void increment() {
		int newValue = this.counter.incrementAndGet();
		LOGGER.debug("incremented counter to {}", newValue);
	}

	@Override
	public int getCount() {
		return this.counter.get();
	}

}
