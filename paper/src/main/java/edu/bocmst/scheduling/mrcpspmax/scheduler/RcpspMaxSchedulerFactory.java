package edu.bocmst.scheduling.mrcpspmax.scheduler;

public abstract class RcpspMaxSchedulerFactory {
	
	private RcpspMaxSchedulerFactory() {}
	
	public static IRcpspMaxScheduler createInstance() {
		return new SerialRcpspMaxScheduler();
	}
}
