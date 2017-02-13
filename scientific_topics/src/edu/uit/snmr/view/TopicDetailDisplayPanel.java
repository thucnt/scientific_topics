/**
 * 
 */
package edu.uit.snmr.view;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.uit.snmr.dataset.termextractor.technical.TermEntry;
import edu.uit.snmr.dataset.termextractor.technical.UserEntry;
import edu.uit.snmr.view.utils.GUIUtils;

/**
 * @author muonnv
 *
 */
public abstract class TopicDetailDisplayPanel  extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9039113739515718403L;
	protected List<UserEntry> senderProbEntries; 
	protected List<UserEntry> recipientProbEntries;
	protected List<TermEntry> termProbEntries;

	public TopicDetailDisplayPanel() {
		this.senderProbEntries = new ArrayList<UserEntry>();
		this.recipientProbEntries = new ArrayList<UserEntry>();
		this.termProbEntries = new ArrayList<TermEntry>();
		createContentPane();
	}
		
	protected TopicDetailDisplayPanel(
			List<UserEntry> senderProbEntries, 
			List<UserEntry> recipientProbEntries,
			List<TermEntry> termProbEntries) {
		this.senderProbEntries = senderProbEntries;
		this.recipientProbEntries = recipientProbEntries;
		this.termProbEntries = termProbEntries;
		createContentPane();
	}
		
	private void createContentPane() {
		this.setLayout(new GridLayout(1, 1));
		
		JTabbedPane tabbedPane = new JTabbedPane();
		ImageIcon senderIcon = createImageIcon("resources/images/senders.gif");
		ImageIcon recipientIcon = createImageIcon("resources/images/recipients.gif");
		ImageIcon termsIcon = createImageIcon("resources/images/terms.gif");

		JComponent sendersOfTopicDisplayPanel = makeSendersOfTopicDisplayPanel();
		tabbedPane.addTab(GUIUtils.get("sender_list"), senderIcon, sendersOfTopicDisplayPanel, GUIUtils.get("sender_list"));
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_G);

		JComponent recipientsOfTopicDisplayPanel = makeRecipientsOfTopicDisplayPanel();
		tabbedPane.addTab(GUIUtils.get("recipient_list"), recipientIcon, recipientsOfTopicDisplayPanel, GUIUtils.get("recipient_list"));
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_N);

		JComponent wordsOfTopicDisplayPanel = makeTermsOfTopicDisplayPanel();
		tabbedPane.addTab(GUIUtils.get("term_list"), termsIcon, wordsOfTopicDisplayPanel, GUIUtils.get("term_list"));
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_W);

		//Add the tabbed pane to this panel.
		add(tabbedPane);

		//The following line enables to use scrolling tabs.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
	}
	
	
	protected abstract JComponent makeSendersOfTopicDisplayPanel();
	
	protected abstract JComponent makeRecipientsOfTopicDisplayPanel();
	
	protected abstract JComponent makeTermsOfTopicDisplayPanel();
	
	
	
	/**
	 * @return the senderProbEntries
	 */
	public List<UserEntry> getSenderProbEntries() {
		return senderProbEntries;
	}

	/**
	 * @param senderProbEntries the senderProbEntries to set
	 */
	public void setSenderProbEntries(List<UserEntry> senderProbEntries) {
		this.senderProbEntries = senderProbEntries;
	}

	/**
	 * @return the recipientProbEntries
	 */
	public List<UserEntry> getRecipientProbEntries() {
		return recipientProbEntries;
	}

	/**
	 * @param recipientProbEntries the recipientProbEntries to set
	 */
	public void setRecipientProbEntries(List<UserEntry> recipientProbEntries) {
		this.recipientProbEntries = recipientProbEntries;
	}

	/**
	 * @return the termProbEntries
	 */
	public List<TermEntry> getTermProbEntries() {
		return termProbEntries;
	}

	/**
	 * @param termProbEntries the termProbEntries to set
	 */
	public void setTermProbEntries(List<TermEntry> termProbEntries) {
		this.termProbEntries = termProbEntries;
	}

	public abstract void reloadTablesData();
	
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		
		if (path != null) {
			return new ImageIcon(path);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}



}
