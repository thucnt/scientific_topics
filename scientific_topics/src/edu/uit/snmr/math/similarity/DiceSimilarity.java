
package edu.uit.snmr.math.similarity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public final class DiceSimilarity extends AbstractStringMetric implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3608032039026526512L;

	/**
     * gets the similarity of the two strings using DiceSimilarity
     * <p/>
     * Dices coefficient = (2*Common Terms) / (Number of terms in String1 + Number of terms in String2).
     *
     * @param string1
     * @param string2
     * @return a value between 0-1 of the similarity
     */
    public float getSimilarity(final List<String> wordset1, final List<String> wordset2) {
        
        final Set<String> allTokens = new HashSet<String>();
        allTokens.addAll(wordset1);
        final int termsInString1 = allTokens.size();
        final Set<String> secondStringTokens = new HashSet<String>();
        secondStringTokens.addAll(wordset2);
        final int termsInString2 = secondStringTokens.size();

        //now combine the sets
        allTokens.addAll(secondStringTokens);
        final int commonTerms = (termsInString1 + termsInString2) - allTokens.size();

        //return Dices coefficient = (2*Common Terms) / (Number of terms in String1 + Number of terms in String2)
        return (2.0f * commonTerms) / (termsInString1 + termsInString2);
    }

    /**
     * gets the un-normalised similarity measure of the metric for the given strings.
     *
     * @param string1
     * @param string2
     * @return returns the score of the similarity measure (un-normalised)
     */
    public float getUnNormalisedSimilarity(final List<String> wordset1, final List<String> wordset2) {
        return getSimilarity(wordset1, wordset2);
    }
}


