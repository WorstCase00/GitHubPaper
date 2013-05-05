package edu.bocmst.scheduling.mrcpspmax.ga.recombination;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.operators.ListCrossover;

public class XPointCrossover<T> extends ListCrossover<T>{

	private final List<Integer> crossOverPoints;

	public XPointCrossover(List<Integer> crossOverPoints) {
		this.crossOverPoints = crossOverPoints;
	}

	@Override
	protected List<List<T>> mate(List<T> parent1,
			List<T> parent2,
			int numberOfCrossoverPoints,
			Random rng)
			{
		List<T> offspring1 = new ArrayList<T>(parent1); // Use a random-access list for performance.
		List<T> offspring2 = new ArrayList<T>(parent2);
		// Apply as many cross-overs as required.
		for (int crossoverIndex : crossOverPoints)
		{
			// Cross-over index is always greater than zero and less than
			// the length of the parent so that we always pick a point that
			// will result in a meaningful cross-over.
			int max = Math.min(parent1.size(), parent2.size());
			if (max > 1) // Don't perform cross-over if there aren't at least 2 elements in each list.
			{
				for (int j = 0; j < crossoverIndex; j++)
				{
					T temp = offspring1.get(j);
					offspring1.set(j, offspring2.get(j));
					offspring2.set(j, temp);
				}
			}
		}
		List<List<T>> result = new ArrayList<List<T>>(2);
		result.add(offspring1);
		result.add(offspring2);
		return result;
	}

}
