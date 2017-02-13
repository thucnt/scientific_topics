/**
 * 
 */
package edu.uit.snmr.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import edu.uit.snmr.dataset.termextractor.technical.ProbEntry;

/**
 * @author muonnv
 *
 */
public class ProbEntryTableModel<T extends ProbEntry> extends AbstractTableModel{
	private String[] columnNames;
	private List<T> probEntries;

	public ProbEntryTableModel(String[] columnNames, List<T> probEntries) {
		this.columnNames = columnNames;
		this.probEntries = probEntries;
	}
	
	public String[] getColumnNames() {
		return columnNames;
	}

	public String getColumnName(int col) {
		return columnNames[col].toString();
	}

	@Override
	public int getColumnCount() {		
		return columnNames.length;
	}

	@Override
	public int getRowCount() {		
		return probEntries.size();
	}

	public boolean isCellEditable(int row, int col)	{ 
		return false; 
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {		
		ProbEntry entry = this.probEntries.get(rowIndex);
		if (columnIndex == 0) {
			return entry.getName();
		} else {
			return entry.getProbability();
		}
	}



}
