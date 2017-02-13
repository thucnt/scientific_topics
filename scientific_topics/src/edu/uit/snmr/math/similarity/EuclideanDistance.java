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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Package: uk.ac.shef.wit.simmetrics.similaritymetrics.euclideandistance
 * Description: uk.ac.shef.wit.simmetrics.similaritymetrics.euclideandistance implements a

 * Date: 05-Apr-2004
 * Time: 11:12:01
 * @author Sam Chapman <a href="http://www.dcs.shef.ac.uk/~sam/">Website</a>, <a href="mailto:sam@dcs.shef.ac.uk">Email</a>.
 * @version 1.2
 */
public final class EuclideanDistance extends AbstractStringMetric implements Serializable {

  
    
    /**
     * gets the similarity of the two strings using EuclideanDistance
     *
     * the 0-1 return is calcualted from the maximum possible Euclidean
     * distance between the strings from the number of terms within them.
     *
     * @param string1
     * @param string2
     * @return a value between 0-1 of the similarity 1.0 identical
     */
    public float getSimilarity(final List<String> wordset1, final List<String> wordset2) {
       
        float totalPossible = (float) Math.sqrt((wordset1.size()*wordset1.size()) + (wordset2.size()*wordset2.size()));
        final float totalDistance = getUnNormalisedSimilarity(wordset1, wordset2);
        return (totalPossible - totalDistance) / totalPossible;
    }

    /**
     * gets the un-normalised similarity measure of the metric for the given strings.
     *
     * @param string1
     * @param string2
     * @return returns the score of the similarity measure (un-normalised)
     */
    public float getUnNormalisedSimilarity(final List<String> wordset1, final List<String> wordset2) {
       
        final Set<String> allTokens = new HashSet<String>();
        allTokens.addAll(wordset1);
        allTokens.addAll(wordset2);

        float totalDistance = 0.0f;
        for (final String token : allTokens) {
            int countInString1 = 0;
            int countInString2 = 0;
            for (final String sToken : wordset1) {
                if (sToken.equals(token)) {
                    countInString1++;
                }
            }
            for (final String sToken : wordset2) {
                if (sToken.equals(token)) {
                    countInString2++;
                }
            }

            totalDistance += ((countInString1 - countInString2) * (countInString1 - countInString2));
        }

        totalDistance = (float) Math.sqrt(totalDistance);
        return totalDistance;
    }

    /**
     * gets the actual euclidean distance ie not the value between 0-1.
     *
     * @param string1
     * @param string2
     * @return the actual euclidean distance
     */
    public float getEuclidDistance(final List<String> wordset1, final List<String> wordset2) {
       
        final Set<String> allTokens = new HashSet<String>();
        allTokens.addAll(wordset1);
        allTokens.addAll(wordset2);

        float totalDistance = 0.0f;
        for (final String token : allTokens) {
            int countInString1 = 0;
            int countInString2 = 0;
            for (final String sToken : wordset1) {
                if (sToken.equals(token)) {
                    countInString1++;
                }
            }
            for (final String sToken : wordset2) {
                if (sToken.equals(token)) {
                    countInString2++;
                }
            }

            totalDistance += ((countInString1 - countInString2) * (countInString1 - countInString2));
        }

        return (float) Math.sqrt(totalDistance);
    }
}




