package edu.bocmst.scheduling.mrcpspmax.bmap.ga.selection;

import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.SelectionStrategy;

import edu.bocmst.scheduling.mrcpspmax.bmap.candidate.IModeAssignment;

public class SimilarityTournament implements
		SelectionStrategy<IModeAssignment> {

	public <S extends IModeAssignment> List<S> select(
			List<EvaluatedCandidate<S>> arg0, boolean arg1, int arg2,
			Random arg3) {
		// TODO Auto-generated method stub
		return null;
	}

}
