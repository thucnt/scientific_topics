package model;

import java.util.ArrayList;
import java.util.List;

import edu.uit.snmr.view.utils.FileUtils;


public class ATopic {
	private ATopic parent;
	private String label;
	private List<String> wordSet = new ArrayList<String>();

	
	
	/**
	 * @param label
	 * @param wordSet
	 */
	public ATopic(String label, List<String> wordSet) {
		this.label = label;
		this.wordSet = wordSet;
	}

	public ATopic(ATopic parent, String label) {
		this.parent = parent;
		this.label = label;
	}

	public void addWord(String word) {
		this.wordSet.add(word);
	}

	public ATopic getParent() {
		return parent;
	}

	public String getLabel() {
		return label;
	}

	public List<String> getWordSet() {
		return wordSet;
	}

	public void setParent(ATopic parent) {
		this.parent = parent;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setWordSet(List<String> wordSet) {
		this.wordSet = wordSet;
	}

	@Override
	public String toString() {
		return this.label;
	}
}
// <topic>... <wordset> <word pro="68">gay</word>....<wordset>