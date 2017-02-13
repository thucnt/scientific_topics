/**
 * 
 */
package edu.uit.snmr.topicmodeling.art;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author muonnv
 *
 */
public class ATopic {

	private String label;
	private List<String> wordSet;
	
	/**
	 * @param label
	 */
	public ATopic(String label) {
		this.label = label;
		this.wordSet = new ArrayList<String>();
	}
	
	
	/**
	 * @param label
	 * @param wordSet
	 */
	public ATopic(String label, List<String> wordSet) {
		this.label = label;
		this.wordSet = wordSet;
	}

	public void addWord(String aword) {
		this.wordSet.add(aword);
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the wordSet
	 */
	public List<String> getWordSet() {
		return wordSet;
	}
	/**
	 * @param wordSet the wordSet to set
	 */
	public void setWordSet(List<String> wordSet) {
		this.wordSet = wordSet;
	}
	
	@Override
	public String toString() {
		return this.label + ": " + this.wordSet;
	}

}
