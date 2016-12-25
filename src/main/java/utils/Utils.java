package utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Utils {

	public static <T1, T2> Map<T2, T1> swapMap(Map<T1, T2> map)
	{
		Map<T2, T1> newMap = new HashMap<T2, T1>();
		
		Set<T1> keys = map.keySet();
		
		for (Iterator<T1> iterator = keys.iterator(); iterator.hasNext();) {
			T1 key = iterator.next();
			newMap.put(map.get(key), key);
		}
		
		return newMap;
	}
}
