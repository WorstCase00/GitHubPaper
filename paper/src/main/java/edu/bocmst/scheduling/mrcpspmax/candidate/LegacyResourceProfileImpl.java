package edu.bocmst.scheduling.mrcpspmax.candidate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Deprecated
class LegacyResourceProfileImpl {
	private static final int TIME_INDEX = 0;
	private static final int VALUE_INDEX = 1;
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceProfileListImpl.class);
	
	private final int[] resourceLimits;
	
	private ArrayList<int[]>[] resourceProfile;

	@SuppressWarnings("unchecked")
	public LegacyResourceProfileImpl(int[] resourceLimits) {
		this.resourceLimits = resourceLimits;
		this.resourceProfile = new ArrayList[resourceLimits.length];
		for(int i = 0; i < resourceLimits.length; i++) {
			resourceProfile[i] = new ArrayList<int[]>();
			resourceProfile[i].add(new int[] {0, resourceLimits[i]});
			resourceProfile[i].add(new int[] {Integer.MAX_VALUE, resourceLimits[i]});
		}
	}
	
	public Integer findEarliestResourceFeasibleStart(int lowerBound,
			int duration, int[] resourceDemands) {
		int lowBound = Integer.MIN_VALUE;
		int upBound = Integer.MAX_VALUE;
		LegacyIndexedTimeWindow[] timeWindows = new LegacyIndexedTimeWindow[resourceProfile.length];

		// check for every resource, first check
		for(int resIndex = 0; resIndex < resourceDemands.length; resIndex++) {
			timeWindows[resIndex] = getEarliestTimeWindowResource(lowerBound, duration, resourceDemands[resIndex], resourceProfile[resIndex]);
			if(timeWindows[resIndex] == null) {
				return null;
			}
			if(timeWindows[resIndex].earliest > lowBound) {
				lowBound = timeWindows[resIndex].earliest;
			}
			if(timeWindows[resIndex].latest < upBound) {
				upBound = timeWindows[resIndex].latest;
			}
		}	

		while(upBound - lowBound < duration) {
			for(int resIndex = 0; resIndex < resourceDemands.length; resIndex++) {
				if(upBound == timeWindows[resIndex].latest) {
					timeWindows[resIndex] = getEarliestTimeWindowResource(upBound+1, duration, resourceDemands[resIndex], resourceProfile[resIndex]);
					if(timeWindows[resIndex] == null) {
						return null;
					}	
				}
			}
			lowBound = Integer.MIN_VALUE;
			upBound = Integer.MAX_VALUE;
			for(int resIndex = 0; resIndex < resourceDemands.length; resIndex++) {
				if(timeWindows[resIndex].earliest > lowBound) {
					lowBound = timeWindows[resIndex].earliest;
				}
				if(timeWindows[resIndex].latest < upBound) {
					upBound = timeWindows[resIndex].latest;
				}
			}
		}
		if(LOGGER.isDebugEnabled()) {
			for(int i = 0; i < timeWindows.length; i++) {
				LOGGER.debug("time window for resource " + i + " : " + timeWindows[i]);
			}
		}

		return lowBound;
	}
	
	public Integer findLatestResourceFeasibleStart(int upperBound,
			int duration, int[] resourceDemands) {
		
		int lowBound = Integer.MIN_VALUE;
		int upBound = Integer.MAX_VALUE;
		LegacyIndexedTimeWindow[] timeWindows = new LegacyIndexedTimeWindow[resourceProfile.length];

		// check for every resource, first check
		for(int resIndex = 0; resIndex < resourceDemands.length; resIndex++) {
			timeWindows[resIndex] = getLatestTimeWindowResource(upperBound, duration, resourceDemands[resIndex], resourceProfile[resIndex]);
			if(timeWindows[resIndex] == null) {
				return null;
			}
			if(timeWindows[resIndex].earliest > lowBound) {
				lowBound = timeWindows[resIndex].earliest;
			}
			if(timeWindows[resIndex].latest < upBound) {
				upBound = timeWindows[resIndex].latest;
			}
		}	

		while(upBound - lowBound < duration) {
			for(int resIndex = 0; resIndex < resourceDemands.length; resIndex++) {
				if(lowBound == timeWindows[resIndex].earliest) {
					timeWindows[resIndex] = getLatestTimeWindowResource(lowBound-1, duration, resourceDemands[resIndex], resourceProfile[resIndex]);
					if(timeWindows[resIndex] == null) {
						return null;
					}	
				}
			}
			lowBound = Integer.MIN_VALUE;
			upBound = Integer.MAX_VALUE;
			for(int resIndex = 0; resIndex < resourceDemands.length; resIndex++) {
				if(timeWindows[resIndex].earliest > lowBound) {
					lowBound = timeWindows[resIndex].earliest;
				}
				if(timeWindows[resIndex].latest < upBound) {
					upBound = timeWindows[resIndex].latest;
				}
			}
		}

		return upBound - duration;
		
	}


	private LegacyIndexedTimeWindow getLatestTimeWindowResource(int upperBound,
			int duration, int demand, List<int[]> resFunction) {
		int endTime = -1;
		int[] actualTV = resFunction.get(0);
		int timeSum = 0;
		if(demand > resFunction.get(resFunction.size()-1)[1]) {
			return null;
		}
		for(int fIndex = resFunction.size() - 2; fIndex >= 0; fIndex--) {
			actualTV = resFunction.get(fIndex);


			if(actualTV[0] <= upperBound) {
				if(endTime == -1) { // you are in no window

					if(actualTV[1] >= demand) { // if enough resources, start new window
						int i = 0;
						while(fIndex + i <= resFunction.size()-1) {
							endTime = resFunction.get(fIndex + i)[0];
							if(resFunction.get(fIndex + i)[1] < demand) {
								break;
							}
							i++;
						}
						endTime = Math.min(endTime, upperBound + duration);
						if(actualTV[0] == 0) {
							if(endTime - duration < 0) {
								return null; 
							} else {
								return new LegacyIndexedTimeWindow(0, endTime, fIndex);
							}
						}
						timeSum = endTime - actualTV[0];
					}
				} else { // you are in window
					if(actualTV[1] >= demand) {
						timeSum = endTime - actualTV[0];
					}
					if((actualTV[1] < demand) || actualTV[0] == 0) { // found end of window
						if(timeSum >= duration) { // if window big enough return or end reached
							return new LegacyIndexedTimeWindow(resFunction.get(fIndex+1)[0], endTime, fIndex);
						}
						endTime = -1;
						timeSum = 0;
					}
				}
			}
		}
		return null;
	}

	public void freeResources(int timeFrom, int duration, int[] resources) {
		int timeTo = timeFrom + duration;

		for(int resIndex = 0; resIndex < resourceProfile.length; resIndex++) {
			List<int[]> resfct =  resourceProfile[resIndex];
			LOGGER.debug("free res: " + resIndex + " in [" + timeFrom + ", " + timeTo + "]");
			for(int[] timeVal : resfct) {
				if(timeVal[0] >= timeFrom && timeVal[0] < timeTo) {
					timeVal[1] += resources[resIndex];
					LOGGER.debug("modified time value : " + Arrays.toString(timeVal));
				}
				if(timeVal[0] == timeTo) {
					break;
				}
			}
		}
	}

	public void bindResources(int timeFrom, int duration, int[] resourceDemands) {

		int[] timeValuePairSaved = null;
		int timeTo = timeFrom + duration;
		for(int resourceIndex = 0; resourceIndex < resourceDemands.length; resourceIndex ++) {
			List<int[]> availabilityFunction = resourceProfile[resourceIndex];
			int resourceDemand = resourceDemands[resourceIndex];
			
			for(int timeIndex = 0; timeIndex < availabilityFunction.size(); timeIndex++) {
				int[] timeVal = availabilityFunction.get(timeIndex);
				// if over point in time
				if(timeFrom <= timeVal[0]) {
					// insert new entry
					if(timeFrom != timeVal[0]) {
						int[] newEntry = new int[] {timeFrom, timeValuePairSaved[1] - resourceDemand};
						if(newEntry[1] < 0) {
							LOGGER.error("updated resource with negative function value");
						}
						availabilityFunction.add(timeIndex, newEntry);
						timeValuePairSaved = newEntry;
						timeIndex++;
					}
					// update within interval
					while(timeTo > timeVal[0]) {
						timeVal[1] -= resourceDemand;
						if(timeVal[1] < 0) {
							LOGGER.error("updated resource with negative function value");
						}
						timeValuePairSaved = timeVal;
						timeIndex++;
						timeVal = availabilityFunction.get(timeIndex);
					}
					// insert new entry
					if(timeTo != timeVal[0]) {
						int[] newEntry = new int[] {timeTo, timeValuePairSaved[1] + resourceDemand};
						availabilityFunction.add(timeIndex, newEntry);
					}
					break;
				}
				// else just save actual timeVal
				else {
					timeValuePairSaved = timeVal;
				}
			}
		}

	}

	public boolean isStartPossibleAt(int startTime, int duration, int[] resourceDemands) {
		int completionTime = startTime + duration;
		int checkFromIndex = findFunctionIndexBeforeOrEqualTime(startTime);
		int checkToIndex = findFunctionIndexAfterOrEqualTime(completionTime, checkFromIndex);
		
		for(int resourceIndex = 0; resourceIndex < resourceDemands.length; resourceIndex++) {
			List<int[]> function = this.resourceProfile[resourceIndex];
			int resourceDemand = resourceDemands[resourceIndex];
			if(!checkFunctionGreaterEqualFromTo(checkFromIndex, checkToIndex, function, resourceDemand)) {
				return false;
			}
		}
		return true;
	}
	
//	@Override
//	public ResourceProfileListImpl clone() {
//		ResourceProfileListImpl clone = new ResourceProfileListImpl(this.resourceLimits);
//		for(int resource = 0; resource < this.resourceProfile.length; resource ++) {
//			clone.resourceProfile[resource] = cloneResourceFunction(this.resourceProfile[resource]);
//		}
//		return clone;
//	}

//	private ArrayList<int[]> cloneResourceFunction(ArrayList<int[]> list) {
//		ArrayList<int[]> clone = new ArrayList<int[]>(list.size());
//		for(int i = 0; i < list.size(); i++) {
//			clone.add(i, list.get(i).clone());
//		}
//		return clone;
//	}

	private boolean checkFunctionGreaterEqualFromTo(int checkFromIndex,
			int checkToIndex, List<int[]> function, int resourceDemand) {
		for(int i = checkFromIndex; i < checkToIndex; i++) {
			int value = function.get(i)[VALUE_INDEX];
			if(value < resourceDemand) {
				return false;
			}
		}
		return true;
	}

	private int findFunctionIndexAfterOrEqualTime(int completionTime, int checkFromIndex) {
		List<int[]> function = resourceProfile[0];
		for(int index = checkFromIndex; index <= function.size(); index ++) {
			int time = function.get(index)[TIME_INDEX];
			if(time >= completionTime) {
				return index;
			}
		}
		return -1;
	}

	private int findFunctionIndexBeforeOrEqualTime(int startTime) {
		List<int[]> function = resourceProfile[0];
		for(int index = 0; index <= function.size(); index ++) {
			int time = function.get(index)[TIME_INDEX];
			if((time == startTime) || (function.get(index + 1)[TIME_INDEX] > startTime)) {
				return index;
			}
		}
		return -1;
	}

	private LegacyIndexedTimeWindow getEarliestTimeWindowResource(int lowerBound, int duration, int consumption, List<int[]> resourceFunction) {
		int startTime = -1;
		int[] actualTV = resourceFunction.get(0);
		int timeSum;
		if(consumption > resourceFunction.get(resourceFunction.size()-1)[1]) {
			return null;
		}
		for(int fIndex = 0; fIndex < resourceFunction.size(); fIndex++) {
			actualTV = resourceFunction.get(fIndex);


			if(actualTV[0] >= lowerBound) {
				if(startTime == -1) { // you are in no window
					if(actualTV[0] == Integer.MAX_VALUE) {
						return new LegacyIndexedTimeWindow(lowerBound, Integer.MAX_VALUE, 0);
					}
					if(actualTV[1] >= consumption) { // if enough resources, start new window
						if((fIndex != 0) && (resourceFunction.get(fIndex-1)[1] >= consumption)) {
							startTime = Math.max(resourceFunction.get(fIndex-1)[0], lowerBound);
						} else {
							startTime = actualTV[0]; 
						}
					}


				} else { // you are in window
					timeSum = actualTV[0] - startTime;

					if((actualTV[1] < consumption)) { // found end of window
						if(timeSum >= duration) { // if window big enough return or end reached
							return new LegacyIndexedTimeWindow(startTime, actualTV[0], fIndex);
						}
						startTime = -1;
						timeSum = 0;
					} else if(actualTV[0] == Integer.MAX_VALUE) {
						return new LegacyIndexedTimeWindow(startTime, actualTV[0], fIndex);
					}
				}
			}
		}
		throw new RuntimeException("Time Window Error");
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("Resource profile:\n");
		for(List<int[]> availabilityFuntion : this.resourceProfile) {
			for(int[] timeValuePair: availabilityFuntion) {
				sb.append(ArrayUtils.toString(timeValuePair));
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public void shift(int delta) {
		for(ArrayList<int[]> function : resourceProfile) {
			for(int[] point : function) {
				point[TIME_INDEX] +=  delta;
			}
			int[] lastRemovedPoint = null;
			while(function.get(0)[TIME_INDEX] < 0) {
				 lastRemovedPoint = function.remove(0);
			}
			if(function.get(0)[TIME_INDEX] > 0) {
				function.set(0, new int[] {0, lastRemovedPoint[VALUE_INDEX]});
			}
			function.get(function.size()-1)[TIME_INDEX] = Integer.MAX_VALUE;
		}
	}

	
}
