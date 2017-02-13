/**
 * 
 */
package edu.uit.snmr.math.similarity;

import java.util.List;

/**
 * @author muonnv
 * 
 */
public class TanimotoCoefficientSimilarity extends AbstractStringMetric {

	@Override
	public float getSimilarity(List<String> wordSet1, List<String> wordSet2) {
		
		int xPrefsSize = wordSet1.size();
		int yPrefsSize = wordSet2.size();
		if (xPrefsSize == 0 && yPrefsSize == 0) {
			
			return Float.NaN;
		}
		if (xPrefsSize == 0 || yPrefsSize == 0) {
			return 0.0f;
		}

		int intersectionSize = xPrefsSize < yPrefsSize ? intersectionSize(
				wordSet2, wordSet1) : intersectionSize(wordSet1, wordSet2);
		if (intersectionSize == 0) {
			return Float.NaN;
		}

		int unionSize = xPrefsSize + yPrefsSize - intersectionSize;

		return (float) intersectionSize / (float) unionSize;
	}

	/**
	 * Convenience method to quickly compute just the size of the intersection
	 * with another .
	 * 
	 * @param other
	 *            to intersect with
	 * @return number of elements in intersection
	 */
	public int intersectionSize(List<String> wordSet1, List<String> wordSet2) {
		int count = 0;
		for (String each : wordSet2) {
			if (each != null && !each.trim().isEmpty()
					&& contains(wordSet1, each)) {
				count++;
			}
		}
		return count;
	}

	private boolean contains(List<String> wordSet, String string) {
		for (String each : wordSet) {
			if (each != null && !each.trim().isEmpty()
					&& string.equalsIgnoreCase(each)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public float getUnNormalisedSimilarity(List<String> wordSet1,
			List<String> wordSet2) {
		return getSimilarity(wordSet1, wordSet2);
	}

}
