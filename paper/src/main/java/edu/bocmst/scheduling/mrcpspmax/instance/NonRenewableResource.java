package edu.bocmst.scheduling.mrcpspmax.instance;

public class NonRenewableResource extends AbstractResource implements INonRenewableResource {

	public NonRenewableResource(int supply) {
		super(supply);
	}
}