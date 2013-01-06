package com.uc4.scheduling.mrcpspmax.instance.loader;

import com.uc4.scheduling.mrcpspmax.instance.INetworkEdge;
import com.uc4.scheduling.mrcpspmax.instance.INetworkVertex;

public class AonNetworkEdge implements INetworkEdge {

	private final INetworkVertex sourceVertex;
	private final INetworkVertex targetVertex;

	public AonNetworkEdge(INetworkVertex sourceVertex, INetworkVertex targetVertex) {
		this.sourceVertex = sourceVertex;
		this.targetVertex = targetVertex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sourceVertex == null) ? 0 : sourceVertex.hashCode());
		result = prime * result
				+ ((targetVertex == null) ? 0 : targetVertex.hashCode());
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
		if (sourceVertex == null) {
			if (other.sourceVertex != null)
				return false;
		} else if (!sourceVertex.equals(other.sourceVertex))
			return false;
		if (targetVertex == null) {
			if (other.targetVertex != null)
				return false;
		} else if (!targetVertex.equals(other.targetVertex))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NetworkEdge [sourceVertex=" + sourceVertex + ", targetVertex="
				+ targetVertex + "]";
	}

	@Override
	public int getSourceActivity() {
		return sourceVertex.getActivityNumber();
	}

	@Override
	public int getTargetActivity() {
		return targetVertex.getActivityNumber();
	}

}
