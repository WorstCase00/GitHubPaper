package edu.bocmst.metaheuristic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOrderCrossoverHelper<T> 
{

	public List<List<T>> mateWithCrossoverPoints(List<T> parent1,
			List<T> parent2, int point1, int point2) {
		List<T> offspring1 = new ArrayList<T>(parent1); // Use a random-access list for performance.
        List<T> offspring2 = new ArrayList<T>(parent2);
        
        int length = point2 - point1;
        if (length < 0)
        {
            length += parent1.size();
        }

        Map<T, T> mapping1 = new HashMap<T, T>(length * 2); // Big enough map to avoid re-hashing.
        Map<T, T> mapping2 = new HashMap<T, T>(length * 2);
        for (int i = 0; i < length; i++)
        {
            int index = (i + point1) % parent1.size();
            T item1 = offspring1.get(index);
            T item2 = offspring2.get(index);
            offspring1.set(index, item2);
            offspring2.set(index, item1);
            mapping1.put(item1, item2);
            mapping2.put(item2, item1);
        }

        checkUnmappedElements(offspring1, mapping2, point1, point2);
        checkUnmappedElements(offspring2, mapping1, point1, point2);

        List<List<T>> result = new ArrayList<List<T>>(2);
        result.add(offspring1);
        result.add(offspring2);
        return result;
	}


    /**
     * Checks elements that are outside of the partially mapped section to
     * see if there are any duplicate items in the list.  If there are, they
     * are mapped appropriately.
     */
    private void checkUnmappedElements(List<T> offspring,
                                       Map<T, T> mapping,
                                       int mappingStart,
                                       int mappingEnd)
    {
        for (int i = 0; i < offspring.size(); i++)
        {
            if (!isInsideMappedRegion(i, mappingStart, mappingEnd))
            {
                T mapped = offspring.get(i);
                while (mapping.containsKey(mapped))
                {
                    mapped = mapping.get(mapped);
                }
                offspring.set(i, mapped);
            }
        }
    }


    /**
     * Checks whether a given list position is within the partially mapped
     * region used for cross-over.
     * @param position The list position to check.
     * @param startPoint The starting index (inclusive) of the mapped region.
     * @param endPoint The end index (exclusive) of the mapped region.
     * @return True if the specified position is in the mapped region, false
     * otherwise.
     */
    private boolean isInsideMappedRegion(int position,
                                         int startPoint,
                                         int endPoint)
    {
        boolean enclosed = (position < endPoint && position >= startPoint);
        boolean wrapAround = (startPoint > endPoint && (position >= startPoint || position < endPoint)); 
        return enclosed || wrapAround;
    }
}
