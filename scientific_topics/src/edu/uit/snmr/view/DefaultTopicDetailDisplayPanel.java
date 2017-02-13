/**
 * 
 */
package edu.uit.snmr.view;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.uit.snmr.dataset.termextractor.technical.TermEntry;
import edu.uit.snmr.dataset.termextractor.technical.UserEntry;
import edu.uit.snmr.view.utils.GUIUtils;


/**
 * @author muonnv
 *
 */
public class DefaultTopicDetailDisplayPanel extends TopicDetailDisplayPanel {
	private static String[] userColumnNames = {GUIUtils.get("sender"), GUIUtils.get("probability")};
	private static String[] termColumnNames = {GUIUtils.get("term"), GUIUtils.get("probability")};

	private JTable  topicSendersTable;
	private JTable topicRecipientTable;
	private JTable topicTermsTable;

	public DefaultTopicDetailDisplayPanel() {
		super();
	}

	protected DefaultTopicDetailDisplayPanel(List<UserEntry> senderProbEntries,
			List<UserEntry> recipientProbEntries,
			List<TermEntry> termProbEntries) {
		super(senderProbEntries, recipientProbEntries, termProbEntries);
	}

	
	
	@Override
	protected JComponent makeSendersOfTopicDisplayPanel() {
		
		JPanel panel = new JPanel(false);
		this.topicSendersTable = new JTable(
				new ProbEntryTableModel<UserEntry>(userColumnNames, this.senderProbEntries));

		panel.setLayout(new GridLayout(1, 1));
		
		JScrollPane tableScroller = new JScrollPane(this.topicSendersTable);
		tableScroller.setAlignmentX(LEFT_ALIGNMENT);
		panel.add(tableScroller);
		return panel;
	}



	@Override
	protected JComponent makeRecipientsOfTopicDisplayPanel() {
		

		JPanel panel = new JPanel(false);
		this.topicRecipientTable = new JTable(
				new ProbEntryTableModel<UserEntry>(userColumnNames, this.recipientProbEntries));

		panel.setLayout(new GridLayout(1, 1));
		JScrollPane tableScroller = new JScrollPane(this.topicRecipientTable);
		tableScroller.setAlignmentX(LEFT_ALIGNMENT);
		
		panel.add(tableScroller);
		return panel;
	}



	@Override
	protected JComponent makeTermsOfTopicDisplayPanel() {
		

		JPanel panel = new JPanel(false);
		this.topicTermsTable = new JTable(
				new ProbEntryTableModel<TermEntry>(termColumnNames, this.termProbEntries));

		panel.setLayout(new GridLayout(1, 1));
		
		JScrollPane tableScroller = new JScrollPane(this.topicTermsTable);
		tableScroller.setAlignmentX(LEFT_ALIGNMENT);
		panel.add(tableScroller);
		return panel;
	}

	
	@Override
	public void reloadTablesData() {
		this.topicTermsTable.setModel(new ProbEntryTableModel<TermEntry>(termColumnNames, this.termProbEntries));
		this.topicSendersTable.setModel(new ProbEntryTableModel<UserEntry>(userColumnNames, this.senderProbEntries));
		this.topicRecipientTable.setModel(new ProbEntryTableModel<UserEntry>(userColumnNames, this.recipientProbEntries));

	}
}
