package edu.bocmst.scheduling.mrcpspmax.instance;

import java.util.Set;

import com.google.common.collect.ImmutableList;

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

	Set<IAonNetworkEdge> getAonNetworkEdges();

	Set<Set<IAonNetworkEdge>> getCycleStructures();

	int[] getNonRenewableResourceConsumption(int activity, int mode);

	int[] getRenewableResourceConsumption(int activity, int mode);
}
