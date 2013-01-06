package com.uc4.scheduling.mrcpspmax.instance;

import java.util.List;

import com.uc4.scheduling.mrcpspmax.instance.loader.IAonNetwork;

public interface IMrcpspMaxInstance {

	IAonNetwork getAonNetwork();

	List<INonRenewableResource> getNonRenewableResourceList();

	List<IRenewableResource> getRenewableResourceList();

	int getNonRenewableResourceConsumption(int activity, int mode, int resource);

	int getTimeLag(int source, int sourceMode, int target, int targetMode);

	int getRenewableResourceConsumption(int activity, int mode, int resource);
}
