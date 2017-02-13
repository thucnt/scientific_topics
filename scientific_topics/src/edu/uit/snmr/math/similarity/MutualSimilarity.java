/**
 * 
 */
package edu.uit.snmr.math.similarity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author muonnv
 *
 */
public class MutualSimilarity extends AbstractStringMetric{

	@Override
	public float getSimilarity(List<String> wordSet1, List<String> wordSet2) {
		final Set<String> allTokens = new HashSet<String>();
		allTokens.addAll(wordSet1);
		final int termsInString1 = allTokens.size();
		final Set<String> secondStringTokens = new HashSet<String>();
		secondStringTokens.addAll(wordSet2);
		final int termsInString2 = secondStringTokens.size();

		//now combine the sets
		allTokens.addAll(secondStringTokens);
		final int commonTerms = (termsInString1 + termsInString2) - allTokens.size();

		final Set<String> allTokens2 = new HashSet<String>();
		allTokens2.addAll(wordSet2);
		final int termsInString3 = allTokens2.size();
		final Set<String> secondStringTokens2 = new HashSet<String>();
		secondStringTokens2.addAll(wordSet1);
		final int termsInString4 = secondStringTokens2.size();

		//now combine the sets
		allTokens2.addAll(secondStringTokens2);
		final int commonTerms2 = (termsInString3 + termsInString4) - allTokens2.size();
		return (((float)commonTerms / (float)allTokens.size()) + ((float)commonTerms2 / (float)allTokens2.size())) / 2;
	}

	@Override
	public float getUnNormalisedSimilarity(List<String> wordSet1,
			List<String> wordSet2) {
		return getSimilarity(wordSet1, wordSet2);
	}


}
