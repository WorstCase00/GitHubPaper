package edu.bocmst.graph;


class DirectedGraphEdge implements IDirectedEdge {

	private final int source;
	private final int target;

	DirectedGraphEdge(int source, int target) {
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
		DirectedGraphEdge other = (DirectedGraphEdge) obj;
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
