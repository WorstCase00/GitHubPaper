package edu.bocmst.scheduling.mrcpspmax.instance.loader;

import edu.bocmst.scheduling.mrcpspmax.instance.IAonNetworkEdge;

public class AonNetworkEdge implements IAonNetworkEdge {

	private final int source;
	private final int target;

	public AonNetworkEdge(int source, int target) {
		this.source = source;
		this.target = target;
	}

	@Override
	public int getSource() {
		return source;
	}

	@Override
	public int getTarget() {
		return target;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + source;
		result = prime * result + target;
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
		AonNetworkEdge other = (AonNetworkEdge) obj;
		if (source != other.source)
			return false;
		if (target != other.target)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[source=" + source + ", target=" + target + "]";
	}
	
	
	
}
