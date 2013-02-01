package edu.bocmst.scheduling.mrcpspmax.instance.loader;

abstract class AbstractResource {

	private final int supply;

	protected AbstractResource(int supply) {
		this.supply = supply;
	}

	public int getSupply() {
		return supply;
	}
}
