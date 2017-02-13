package edu.uit.snmr.view.utils;

import java.util.Enumeration;

import org.netbeans.swing.outline.RowModel;

public class MyRowModel implements RowModel {

	public Class getColumnClass(int col) {
		switch (col) {
		case 0:
			return String.class;
		case 1:
			return Boolean.class; // these return class definitions will
			// case 2: return Boolean.class; //trigger the checkbox rendering
		default:
			return null;
		}
	}

	public int getColumnCount() {
		return 2;// the first time, this value is 3.
	}

	public String getColumnName(int col) {
		if(col==0)
			return "Topics";
		else if(col==1)
			return "Select";
		return "";
	}

	public Object getValueFor(Object node, int col) {
		MyTreeNode n = (MyTreeNode) node;
		switch (col) {
		case 0:
			return n.getName();
		case 1:
			return n.getData1();
			// case 2: return n.getData2();
		default:
			return null;
		}
	}

	public boolean isCellEditable(Object node, int col) {
		return col > 0;
	}

	public void setValueFor(Object node, int col, Object val) {
		MyTreeNode n = (MyTreeNode) node;
		if (col == 1) {
			n.setData1((Boolean) val);
		}
		// else if (col == 2) {n.setData2((Boolean)val);}
		// EDIT: here is a recursive method to set all children
		// selected for one of the two checkboxes as it is
		// checked by the parent
		for (Enumeration<MyTreeNode> children = n.children(); children.hasMoreElements();) {
			MyTreeNode child = (MyTreeNode) children.nextElement();
			setValueFor(child, col, val);
		}
	}
}