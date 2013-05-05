package edu.bocmst.scheduling.mrcpspmax.instance;

import java.util.Set;

import com.google.common.collect.ImmutableList;

import edu.bocmst.graph.IDirectedEdge;
import edu.bocmst.graph.IDirectedGraph;

public interface IMrcpspMaxInstance {

	ImmutableList<INonRenewableResource> getNonRenewableResourceList();

	ImmutableList<IRenewableResource> getRenewableResourceList();

	int getNonRenewableResourceConsumption(int activity, int mode, int resource);

	int getTimeLag(int source, int sourceMode, int target, int targetMode);

	int getRenewableResourceConsumption(int activity, int mode, int resource);
	
	int getModeCount(int activity);

	int getNonRenewableResourceCount();
	
	int getRenewableResourceCount();

	int getActivityCount();

	int getProcessingTime(int activity, int mode);

	Set<IDirectedEdge> getAonNetworkEdges();

	Set<Set<IDirectedEdge>> getCycleStructures();

	int[] getNonRenewableResourceConsumption(int activity, int mode);

	int[] getRenewableResourceConsumption(int activity, int mode);

	IDirectedGraph getAonNetwork();

	int getInstanceId();

	int[] getModeCounts();
}
