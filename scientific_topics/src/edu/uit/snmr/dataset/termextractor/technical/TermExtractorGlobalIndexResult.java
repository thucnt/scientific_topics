/**
 * 
 */
package edu.uit.snmr.dataset.termextractor.technical;

import uk.ac.shef.dcs.oak.jate.core.feature.FeatureDocumentTermFrequency;
import uk.ac.shef.dcs.oak.jate.core.feature.indexer.GlobalIndexMem;


/**
 * @author muonnv
 *
 */
public class TermExtractorGlobalIndexResult {

	private FeatureDocumentTermFrequency termDocFreq;
	private GlobalIndexMem termDocIndex;

	/**
	 * @param termDocFreq
	 * @param termDocIndex
	 */
	public TermExtractorGlobalIndexResult(GlobalIndexMem termDocIndex, FeatureDocumentTermFrequency termDocFreq) {
		this.termDocFreq = termDocFreq;
		this.termDocIndex = termDocIndex;
	}

	/**
	 * @return the termDocFreq
	 */
	public FeatureDocumentTermFrequency getTermDocFreq() {
		return termDocFreq;
	}

	/**
	 * @param termDocFreq the termDocFreq to set
	 */
	public void setTermDocFreq(FeatureDocumentTermFrequency termDocFreq) {
		this.termDocFreq = termDocFreq;
	}

	/**
	 * @return the termDocIndex
	 */
	public GlobalIndexMem getTermDocIndex() {
		return termDocIndex;
	}

	/**
	 * @param termDocIndex the termDocIndex to set
	 */
	public void setTermDocIndex(GlobalIndexMem termDocIndex) {
		this.termDocIndex = termDocIndex;
	}


}
