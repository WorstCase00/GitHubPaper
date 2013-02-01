package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import edu.bocmst.scheduling.mrcpspmax.instance.INonRenewableResource;

class NonRenewableResource extends AbstractResource implements INonRenewableResource {

	NonRenewableResource(int supply) {
		super(supply);
	}
}
