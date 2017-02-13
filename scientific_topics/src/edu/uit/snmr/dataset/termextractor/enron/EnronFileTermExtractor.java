/**
 * 
 */
package edu.uit.snmr.dataset.termextractor.enron;

import java.io.IOException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import uk.ac.shef.dcs.oak.jate.JATEException;
import uk.ac.shef.dcs.oak.jate.core.feature.indexer.GlobalIndexMem;
import uk.ac.shef.dcs.oak.jate.model.Document;
import edu.uit.snmr.confs.DatasetPropertiesConfig;
import edu.uit.snmr.dataset.termextractor.FileTermExtractor;
import edu.uit.snmr.dataset.termextractor.technical.TermExtractorGlobalIndexResult;
import edu.uit.snmr.exceptions.SnmrException;
import edu.uit.snmr.utils.IOUtils;

/**
 * @author muonnv
 *
 */
public class EnronFileTermExtractor extends FileTermExtractor {

	private static Logger logger = Logger.getLogger(EnronFileTermExtractor.class);

	private DatasetPropertiesConfig propertiesConfig = DatasetPropertiesConfig.getInstance();

	public void execute(String messageSetFolder, String extractedTermsFolder)
			throws SnmrException, JATEException, IOException {

		Map<String, Integer> messageIdMap = this.loadMessageId(propertiesConfig.getMessageIdFilePath());		
		//		EnronMailFilterOptions options = new EnronMailFilterOptions(true, true);
		//		Map<String, Integer[]> artMap  = filter.doFilterMessages(options);

		TermExtractorGlobalIndexResult globalIndexResult = this.extractTerms(messageSetFolder, extractedTermsFolder);

		GlobalIndexMem termDocIndex = globalIndexResult.getTermDocIndex();
		Map<Integer,Set<Integer>> doc2TermsMap = termDocIndex.getDoc2Terms();
		Set<Entry<Integer, Set<Integer>>> entryies = doc2TermsMap.entrySet();

		Formatter formatter1 = new Formatter(propertiesConfig.getMessageTermFrequenlyPath());
		Formatter formatter2 = new Formatter(propertiesConfig.getTermIdsPath());
		Formatter formatter3 = new Formatter(propertiesConfig.getTermsPath());
		int nWordTokens = 0;
		int nMessages = 0;
		int nTerms = 0;

		//store doc_author_recipients
		for (Entry<Integer, Set<Integer>> entry : entryies) {

			Document doc = termDocIndex.retrieveDocument(entry.getKey());
			String pathDoc = doc.getUrl().getPath();
			String docName = pathDoc.substring(pathDoc.lastIndexOf('/') + 1);
			docName = docName.substring(0, docName.lastIndexOf(".txt"));

			Set<Integer> termIds = entry.getValue();
			for (Integer termId : termIds) {
				int fre = globalIndexResult.getTermDocFreq().getTermFreqInDoc(termId, entry.getKey());
				nWordTokens += fre;
				formatter1.format("%d\t%d\t%d\n", 
						messageIdMap.get(docName), termId + 1, fre);
				formatter1.flush();					
			}

			nMessages++;
		}



		//store terms
		Map<String, Integer> termMap = termDocIndex.getTermIdMap();
		Set<Entry<String, Integer>> entries = termMap.entrySet();
		for (Entry<String, Integer> entry : entries) {
			formatter2.format("%s\t%d\n", entry.getKey(), entry.getValue() + 1);
			formatter2.flush();
			formatter3.format("%s\n", entry.getKey());
			formatter3.flush();
			
			nTerms++;
		}

		//store number of tokens
		
		propertiesConfig.setNTokens(nWordTokens);
		propertiesConfig.setNMessages(nMessages);
		propertiesConfig.setNTerms(nTerms);
		propertiesConfig.store();

		formatter1.close();
		formatter2.close();
		formatter3.close();
	}

	private Map<String, Integer> loadMessageId(String messageIdFile) {
		Map<String, Integer> messageIdMap = new HashMap<String, Integer>();
		Scanner scanner = IOUtils.makeScanner(messageIdFile);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();				
			if (line != null && line.trim().length() > 0) {					
				String[] subStrs = line.split("\t");
				messageIdMap.put(subStrs[0].trim(), Integer.parseInt(subStrs[1].trim()));
			}
		}
		scanner.close();
		return messageIdMap;
	}

	public static void main(String[] args) {

		try {
			BasicConfigurator.configure();

			EnronFileTermExtractor termExtractor = new EnronFileTermExtractor();
			termExtractor.execute("D:/muonnv/data/test/aa", "D:/muonnv/data/test/term");
		} catch (SnmrException e) {
			e.printStackTrace();
		}  catch (IOException e) {			
			e.printStackTrace();
		} catch (JATEException e) {
			e.printStackTrace();
		}
	}
}
