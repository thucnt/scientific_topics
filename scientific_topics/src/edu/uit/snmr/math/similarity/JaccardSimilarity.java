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

/**
 * Package: uk.ac.shef.wit.simmetrics.similaritymetrics.jaccard
 * Description: uk.ac.shef.wit.simmetrics.similaritymetrics.jaccard implements a

 * Date: 02-Apr-2004
 * Time: 15:26:19
 * @author Sam Chapman <a href="http://www.dcs.shef.ac.uk/~sam/">Website</a>, <a href="mailto:sam@dcs.shef.ac.uk">Email</a>.
 * @version 1.1
 */
public final class JaccardSimilarity extends AbstractStringMetric implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5189458346489736584L;

	/**
	 * gets the similarity of the two strings using JaccardSimilarity.
	 *
	 * @param string1
	 * @param string2
	 * @return a value between 0-1 of the similarity
	 */
	public float getSimilarity(final List<String> wordset1, final List<String> wordset2) {
		/*
Each instance is represented as a Jaccard vector similarity function. The Jaccard between two vectors X and Y is

(X*Y) / (|X||Y|-(X*Y))

where (X*Y) is the inner product of X and Y, and |X| = (X*X)^1/2, i.e. the Euclidean norm of X.

This can more easily be described as ( |X & Y| ) / ( | X or Y | )
		 */
		//todo this needs checking
		
		final Set<String> allTokens = new HashSet<String>();
		allTokens.addAll(wordset1);
		final int termsInString1 = allTokens.size();
		final Set<String> secondStringTokens = new HashSet<String>();
		secondStringTokens.addAll(wordset2);
		final int termsInString2 = secondStringTokens.size();

		//now combine the sets
		allTokens.addAll(secondStringTokens);
		final int commonTerms = (termsInString1 + termsInString2) - allTokens.size();

		//return JaccardSimilarity
		return (float) (commonTerms) / (float) (allTokens.size());
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

