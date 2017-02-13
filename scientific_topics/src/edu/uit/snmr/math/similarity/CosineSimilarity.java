/**
 * SimMetrics - SimMetrics is a java library of Similarity or Distance
 * Metrics, e.g. Levenshtein Distance, that provide float based similarity
 * measures between String Data. All metrics return consistant measures
 * rather than unbounded similarity scores.
 *
 * Copyright (C) 2005 Sam Chapman - Open Source Release v1.1
 *
 * Please Feel free to contact me about this library, I would appreciate
 * knowing quickly what you wish to use it for and any criticisms/comments
 * upon the SimMetric library.
 *
 * email:       s.chapman@dcs.shef.ac.uk
 * www:         http://www.dcs.shef.ac.uk/~sam/
 * www:         http://www.dcs.shef.ac.uk/~sam/stringmetrics.html
 *
 * address:     Sam Chapman,
 *              Department of Computer Science,
 *              University of Sheffield,
 *              Sheffield,
 *              S. Yorks,
 *              S1 4DP
 *              United Kingdom,
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package edu.uit.snmr.math.similarity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public final class CosineSimilarity extends AbstractStringMetric implements Serializable {

   
    /**
	 * 
	 */
	private static final long serialVersionUID = 676322035312681469L;

	/**
     * gets the similarity of the two strings using CosineSimilarity.
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

        //return CosineSimilarity
        return (float) (commonTerms) / (float) (Math.pow((float) termsInString1, 0.5f) * Math.pow((float) termsInString2, 0.5f));
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



