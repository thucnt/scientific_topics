package edu.uit.snmr.math.similarity;

import java.util.List;


public abstract class AbstractStringMetric  {

    
    /**
     * gets the similarity measure of the metric for the given strings.
     *
     * @param string1
     * @param string2
     *
     * @return returns a value 0-1 of similarity 1 = similar 0 = not similar
     */
    public abstract float getSimilarity(List<String> wordSet1, List<String> wordSet2);

    /**
     * gets the un-normalised similarity measure of the metric for the given strings.
     *
     * @param string1
     * @param string2
     *
     * @return returns the score of the similarity measure (un-normalised)
     */
    public abstract float getUnNormalisedSimilarity(List<String> wordSet1, List<String> wordSet2);
}
