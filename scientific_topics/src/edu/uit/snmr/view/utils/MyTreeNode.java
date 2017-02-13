package edu.uit.snmr.view.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import com.thoughtworks.xstream.annotations.XStreamAlias;
//For GUI_Tree
public class MyTreeNode extends DefaultMutableTreeNode implements Serializable {
	private Boolean data1 = null;
	private String name = null;
	private Object userObject = null;
	private List<String> wordSet = new ArrayList<String>();

	public MyTreeNode(String name) {
		this.name = name;
		this.userObject=name;
	}

	public void setData1(Boolean val) {
		data1 = val;
	}

	public Boolean getData1() {
		return data1;
	}

	public String getName() {
		return name;
	}

	public List<String> getWordSet() {
		return wordSet;
	}

	public void setWordSet(List<String> wordSet) {
		this.wordSet = wordSet;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MyTreeNode))
			return false;
		else {
			MyTreeNode node = (MyTreeNode) obj;
			return this.name.equals(node.getName());
		}
	}

	@Override
	public void add(MutableTreeNode newChild) {
		super.add(newChild);
	}
}