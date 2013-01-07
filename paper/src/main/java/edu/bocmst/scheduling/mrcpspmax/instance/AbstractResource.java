package edu.bocmst.scheduling.mrcpspmax.instance;

public abstract class AbstractResource {

	private final int supply;

	public AbstractResource(int supply) {
		super();
		this.supply = supply;
	}

	public int getSupply() {
		return supply;
	}
}
