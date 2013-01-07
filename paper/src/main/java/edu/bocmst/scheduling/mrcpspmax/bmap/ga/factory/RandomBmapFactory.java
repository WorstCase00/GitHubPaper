package edu.bocmst.scheduling.mrcpspmax.bmap.ga.factory;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.CandidateFactory;

import edu.bocmst.scheduling.mrcpspmax.bmap.solution.IModeAssignment;
import edu.bocmst.scheduling.mrcpspmax.instance.IMrcpspMaxInstance;

public class RandomBmapFactory implements CandidateFactory<IModeAssignment> {

	public RandomBmapFactory(IMrcpspMaxInstance problem) {
		// TODO Auto-generated constructor stub
	}

	public List<IModeAssignment> generateInitialPopulation(int arg0, Random arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IModeAssignment> generateInitialPopulation(int arg0,
			Collection<IModeAssignment> arg1, Random arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public IModeAssignment generateRandomCandidate(Random arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
