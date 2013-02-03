package edu.bocmst.utils;

public class IntInterval {

	private final int lowerBound;
	private final Integer upperBound;

	public IntInterval(int lowerBound, Integer upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public int getLowerBound() {
		return this.lowerBound;
	}

	public Integer getUpperBound() {
		return this.upperBound;
	}

	@Override
	public String toString() {
		return "StartTimeWindow [" + lowerBound + ", "
				+ upperBound + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lowerBound;
		result = prime * result
				+ ((upperBound == null) ? 0 : upperBound.hashCode());
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
		IntInterval other = (IntInterval) obj;
		if (lowerBound != other.lowerBound)
			return false;
		if (upperBound == null) {
			if (other.upperBound != null)
				return false;
		} else if (!upperBound.equals(other.upperBound))
			return false;
		return true;
	}
	
}
