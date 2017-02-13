/**
 * 
 */
package edu.uit.snmr.dataset.termextractor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.ac.shef.dcs.oak.jate.JATEException;
import uk.ac.shef.dcs.oak.jate.core.algorithm.AbstractFeatureWrapper;
import uk.ac.shef.dcs.oak.jate.core.algorithm.Algorithm;
import uk.ac.shef.dcs.oak.jate.core.feature.indexer.GlobalIndex;
import uk.ac.shef.dcs.oak.jate.io.ResultWriter2File;
import uk.ac.shef.dcs.oak.jate.model.Term;
import edu.uit.snmr.dataset.termextractor.technical.TermExtractorGlobalIndexResult;

/**
 * @author muonnv
 *
 */
public abstract class TermExtractor {
	
	protected String REFERENCE_CORPUS_PATH = "jate/nlp_resources/bnc_unifrqs.normal";
	
	protected Map<Algorithm, AbstractFeatureWrapper> algregistry = new HashMap<Algorithm, AbstractFeatureWrapper>();
	private static Logger logger = Logger.getLogger(TermExtractor.class);


	protected  void registerAlgorithm(Algorithm a, AbstractFeatureWrapper f) {
		algregistry.put(a, f);
	}

	protected void execute(GlobalIndex index, String extractedTermsFolder) throws JATEException, IOException {
		ResultWriter2File writer = new ResultWriter2File(index);
        if (algregistry.size() == 0) throw new JATEException("No algorithm registered!");
        logger.info("Running NP recognition...");

        /*.extractNP(c);*/
        for (Map.Entry<Algorithm, AbstractFeatureWrapper> en : algregistry.entrySet()) {
            logger.info("Running feature store builder and ATR..." + en.getKey().toString());
            Term[] result = en.getKey().execute(en.getValue());
            Term term = result[1];
            writer.output(result, extractedTermsFolder + File.separator + en.getKey().toString() + ".txt");
        }
	}
	
	public abstract TermExtractorGlobalIndexResult extractTerms(String corpusPath, String ouputPath) throws JATEException, IOException;
}
