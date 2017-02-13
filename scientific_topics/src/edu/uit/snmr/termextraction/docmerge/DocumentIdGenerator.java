/**
 * 
 */
package edu.uit.snmr.termextraction.docmerge;

/**
 * @author muonnv
 *
 */
public class DocumentIdGenerator {

	
	private DocumentIdGenerator() {		
	}
	
	public static String generateDocuemntIdForTopic(String topic) {
		long subfixId = System.currentTimeMillis();
		return topic + subfixId;
	}
}
