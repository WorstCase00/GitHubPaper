package edu.bocmst.scheduling.mrcpspmax.metric;

public class Metric {

	private final String name;
	private final Double value;
	public Metric(String name, Double value) {
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public Double getValue() {
		return value;
	}
	@Override
	public String toString() {
		return "Metric [name=" + name + ", value=" + value + "]";
	}
	
}
