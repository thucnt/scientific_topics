package edu.uit.snmr.view.utils;

import java.util.*;
//For tree in GUI
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public class MyMutableTreeNode extends DefaultMutableTreeNode {
	private List<String> wordset = new ArrayList<String>();

	
	public MyMutableTreeNode(Object userObject) {
		super(userObject);
	}

	public List<String> getWordset() {
		return wordset;
	}

	public void setWordset(List<String> wordset) {
		this.wordset = wordset;
	}

	@Override
	public void add(MutableTreeNode newChild) {
		super.add(newChild);
	}
}
