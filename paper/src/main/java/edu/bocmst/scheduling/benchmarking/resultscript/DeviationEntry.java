package edu.bocmst.scheduling.benchmarking.resultscript;

class DeviationEntry {
	private static final String TEMPLATE = "dev for instance %d: %f (ballestin: %d, own: %d)";
	private final int instanceNr;
	private final double deviation;
	private final double result;
	private final double ballestin;
	
	public DeviationEntry(int instanceNr, double result, double ballestin) {
		super();
		this.result = result;
		this.ballestin = ballestin;
		this.instanceNr = instanceNr;
		this.deviation = ballestin - result;;
	}
	
	public int getInstanceNr() {
		return instanceNr;
	}

	public double getDeviation() {
		return deviation;
	}
	
	public String toString() {
		return String.format(TEMPLATE, instanceNr, deviation, (int) ballestin, (int) result);
	}
}
