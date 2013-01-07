package edu.bocmst.scheduling.mrcpspmax.instance;


public class NetworkVertex implements INetworkVertex {

	private final int activityIndex;

	public NetworkVertex(int activityIndex) {
		this.activityIndex = activityIndex;
	}

	@Override
	public int getActivityNumber() {
		return activityIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + activityIndex;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NetworkVertex other = (NetworkVertex) obj;
		if (activityIndex != other.activityIndex)
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "NetworkVertex [activityIndex=" + activityIndex + "]";
	}

	public static INetworkVertex createInstance(int activityIndex) {
		return new NetworkVertex(activityIndex);
	}
}
