/* Copyright (C) 2003 Univ. of Massachusetts Amherst, Computer Science Dept.
   This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
   http://www.cs.umass.edu/~mccallum/mallet
   This software is provided under the terms of the Common Public License,
   version 1.0, as published by http://www.opensource.org.  For further
   information, see the file `LICENSE' included with this distribution. */
package edu.uit.snmr.topicmodeling.art;

import edu.uit.snmr.utils.ArrayUtils;

/**
 * @author muonnv
 *
 */
public class DivergenceUtils {

	public static final double log2 = Math.log(2);

	/**
	 * Returns the Jensen-Shannon divergence.
	 */
	public static double jensenShannonDivergence(double[] p1, double[] p2) {
		assert(p1.length == p2.length);
		double[] average = new double[p1.length];
		for (int i = 0; i < p1.length; ++i) {
			average[i] += (p1[i] + p2[i])/2;
		}
		return (klDivergence(p1, average) + klDivergence(p2, average))/2;
	}



	/**
	 * Returns the KL divergence, K(p1 || p2).
	 *
	 * The log is w.r.t. base 2. <p>
	 *
	 * *Note*: If any value in <tt>p2</tt> is <tt>0.0</tt> then the KL-divergence
	 * is <tt>infinite</tt>. Limin changes it to zero instead of infinite. 
	 * 
	 */
	public static double klDivergence(double[] p1, double[] p2) {

		double klDiv = 0.0;

		for (int i = 0; i < p1.length; ++i) {
			if (p1[i] == 0) { continue; }
			if (p2[i] == 0.0) { continue; } // Limin

			klDiv += p1[i] * Math.log( p1[i] / p2[i] );
		}

		return klDiv / log2; // moved this division out of the loop -DM
	}
	
	/**
	 * Compute Jensen-Shanon divergence of two discrete distributions
	 * p1, p2: the two distributions, sum(p1) = 1, sum(p2) = 1, 0 <= p1(i) <= 1, 0 <= p2(i) <= 1
	 * lambda1, lambda2: the weights of p1 and p2, lambda1 + lambda2 = 1, 0 <= lambda1 <= 1, 0 <= lambda2 <= 1
	 * function d = JSD(p1,p2, lambda1, lambda2) 
	 */
	public static double jsd(double[] p1, double[] p2, double lambda1, double lambda2) {
		double a = entropy(ArrayUtils.plus(ArrayUtils.multi(p1, lambda1), ArrayUtils.multi(p2, lambda2)));
		double b = entropy(p1) * lambda1;
		double c = entropy(p2) * lambda2;
		
		return a - b - c;
	}
	
	//Shannon entropy
	private static double entropy(double[] p) {
		double e = 0.0;
		for (int i = 0; i < p.length; i++) {
			if (p[i] != 0) {
				e = e - p[i] * Math.log(p[i]);
			}
		}
		return e;
	}
}
