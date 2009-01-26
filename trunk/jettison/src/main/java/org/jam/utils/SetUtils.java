package org.jam.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SetUtils {
	private SetUtils() {
		
	}
	
	/**
	 * Return the disjoint set of elements from {@code col1} and {@code col2}.
	 * 
	 * <p>Specifically, this returns the set of elements that are only in 
	 * {@code col1} or only in {@code col2}.</p>
	 * 
	 * @param <T> the type of object in the collection.
	 * @param col1 the first collection
	 * @param col2 the second collection
	 * 
	 * @return the disjoint set of elements from {@code col1} and {@code col2}
	 */
	public static <T> Collection<T> disjoint(Collection<T> col1, Collection<T> col2) {
		Set<T> disjoint = new HashSet<T>();
		
		if(col1 == null) {
			if(col2 != null) {
				disjoint.addAll(col2);
			}
		} else if(col2 != null) {
			for(T obj : col1) {
				if(!col2.contains(obj))
					disjoint.add(obj);
			}
			for(T obj : col2) {
				if(!col1.contains(obj))
					disjoint.add(obj);
			}
		} else {
			disjoint.addAll(col1);
		}
		return disjoint;
	}
	
	/**
	 * Return the intersection of elements from {@code col1} and {@code col2}.
	 * 
	 * <p>The {@code Collection} returned from this method will contain all the 
	 * objects from {@code col1} that are also in {@code col2}. 
	 * 
	 * @param <T> the type of elements in the collection
	 * @param col1 first collection.
	 * @param col2 second collection.
	 * 
	 * @return the intersection of elements from {@code col1} and {@code col2}
	 */
	public static <T> Collection<T> intersection(Collection<T> col1, Collection<T> col2) {
		Set<T> intersection = new HashSet<T>();
		
		if(col1 != null && col2 != null) {
			for(T obj : col1) {
				if(col2.contains(obj)) {
					intersection.add(obj);
				}
			}
		}
		
		return intersection;
	}
	
	public static <V> Map<Integer, V> getIdentityMap(Collection<V> col) {
    	Map<Integer, V> identityMap = new HashMap<Integer, V>();
    	for(V obj : col) {
    		identityMap.put(obj.hashCode(), obj);
    	}
    	return identityMap;
    }
}
