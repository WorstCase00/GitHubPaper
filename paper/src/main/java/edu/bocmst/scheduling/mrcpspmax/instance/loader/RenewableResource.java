package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import edu.bocmst.scheduling.mrcpspmax.instance.IRenewableResource;

class RenewableResource extends AbstractResource implements IRenewableResource {

	RenewableResource(int supply) {
		super(supply);
	}
}
