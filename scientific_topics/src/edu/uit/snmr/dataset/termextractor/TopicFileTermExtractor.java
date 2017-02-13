/**
 * 
 */
package edu.uit.snmr.dataset.termextractor;

import java.io.File;
import java.io.IOException;

import uk.ac.shef.dcs.oak.jate.JATEException;
import edu.uit.snmr.dataset.termextractor.technical.TermExtractorGlobalIndexResult;
import edu.uit.snmr.exceptions.SnmrException;

/**
 * @author muonnv
 *
 */
public class TopicFileTermExtractor extends FileTermExtractor {

//	public void execute(String topicsCorpus, String ouputFolder) 
//			throws SnmrException, JATEException, IOException {
//
//		File topicsCorpusFolder = new File(topicsCorpus);
//		if (topicsCorpusFolder.isDirectory()) {
//			File[] topicFolders = topicsCorpusFolder.listFiles();
//			for (File eachTopic : topicFolders) {
//				if (eachTopic.isDirectory()) {
//					String outputTopicFolderName = ouputFolder + File.separator + eachTopic.getName();
//					File outputTopicFolder = new File(outputTopicFolderName);
//					if (outputTopicFolder.exists()) {
//						outputTopicFolder.delete();
//					}
//					outputTopicFolder.mkdir();
//					TermExtractorGlobalIndexResult globalIndexResult = 
//							this.extractTerms(eachTopic.getAbsolutePath(), outputTopicFolderName);
//					
//				}
//			}
//		}
//	}

	public static void main(String[] args) {
		String topicsCorpus = "F:/data/buzzle/paper_by_topics";
		String outputFolder = "F:/data/buzzle/extractedTopics";
		
		try {
			File topicsCorpusFolder = new File(topicsCorpus);
			if (topicsCorpusFolder.isDirectory()) {
				File[] topicFolders = topicsCorpusFolder.listFiles();
				for (File eachTopic : topicFolders) {
					if (eachTopic.isDirectory()) {
						String outputTopicFolderName = outputFolder + File.separator + eachTopic.getName();
						File outputTopicFolder = new File(outputTopicFolderName);
						if (!outputTopicFolder.exists()) {
							
						outputTopicFolder.mkdir();
						TopicFileTermExtractor termExtractor = new TopicFileTermExtractor();
						TermExtractorGlobalIndexResult globalIndexResult = 
								termExtractor.extractTerms(eachTopic.getAbsolutePath(), outputTopicFolderName);
						termExtractor = null;
						System.gc();
						}
					}
				}
			}
		} catch (JATEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
