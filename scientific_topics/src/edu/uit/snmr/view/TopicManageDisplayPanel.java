/**
 * 
 */
package edu.uit.snmr.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import edu.uit.snmr.confs.DatasetPropertiesConfig;
import edu.uit.snmr.dataset.termextractor.technical.TermEntry;
import edu.uit.snmr.dataset.termextractor.technical.UserEntry;

/**
 * @author muonnv
 *
 */
public class TopicManageDisplayPanel extends JPanel {
	private UnlabelingTopicListDisplayPanel topicListPanel;
	private TopicDetailDisplayPanel topicDetailPanel;

	private List<UserEntry> topicSenderEntries;
	private List<UserEntry> topicRecipientEntries;
	private List<TermEntry> topicTermEntries;

	private DatasetPropertiesConfig propertiesConfig = DatasetPropertiesConfig.getInstance();

	public TopicManageDisplayPanel(List<Integer> topicNrs) {
		this.topicSenderEntries = new ArrayList<UserEntry>();
		this.topicRecipientEntries = new ArrayList<UserEntry>();
		this.topicTermEntries = new ArrayList<TermEntry>();

		this.topicListPanel = new UnlabelingTopicListDisplayPanel(this, topicNrs, 0);
		this.topicDetailPanel = new DefaultTopicDetailDisplayPanel();
		changeTopicNr(this.topicListPanel.getSelectedTopic());
		createContentPane();
	}


	private void createContentPane() {
		this.setLayout(new GridLayout(1, 1));
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				topicDetailPanel, topicListPanel);

		splitPane.setResizeWeight(0.85);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);		

		//Provide minimum sizes for the two components in the split pane.
		Dimension minimumSize = new Dimension(150, 50);
		topicListPanel.setMinimumSize(minimumSize);
		topicListPanel.setMinimumSize(minimumSize);      
		this.add(splitPane);
	}

	public void changeTopicNr(int topicNr) {
		try {
			this.loadTopicInfo(topicNr);
			this.topicDetailPanel.setTermProbEntries(this.topicTermEntries); 
			this.topicDetailPanel.setSenderProbEntries(this.topicSenderEntries);
			this.topicDetailPanel.setRecipientProbEntries(topicRecipientEntries);
			this.topicDetailPanel.reloadTablesData();
		} catch (FileNotFoundException e) {
			//custom title, error icon
			JOptionPane.showMessageDialog(this,	"Topic data not found" + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}		
	}


	public void loadTopicInfo(int topicNr) throws FileNotFoundException {
		this.topicRecipientEntries.clear();
		this.topicSenderEntries.clear();
		this.topicTermEntries.clear();


		boolean isStartTermSection = false;
		boolean isStartSendesSection = false;
		boolean isStartrecipientsSectons = false;

		String topicPath = propertiesConfig.getExtractedTopicsFolder() + File.separator + topicNr + ".txt";
		Scanner scanner = new Scanner(new File(topicPath));

		if (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.trim().equalsIgnoreCase("#terms#")) {
				isStartTermSection = true;
				isStartSendesSection = false;
				isStartrecipientsSectons = false;
			} else {
				scanner.close();
				return;
			}

		}
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.trim().equalsIgnoreCase("#senders#")) {
				isStartTermSection = false;
				isStartSendesSection = true;
				isStartrecipientsSectons = false;
			} else if (line.trim().equalsIgnoreCase("#recipients#")) {
				isStartTermSection = false;
				isStartSendesSection = false;
				isStartrecipientsSectons = true;
			} else {
				String[] array = line.trim().split("\t");
				String name = array[0].trim();
				//Double prob = new Double(array[1].trim());

				if (isStartTermSection) {
					Double prob = new Double(array[1].trim());
					TermEntry entry = new TermEntry(name, prob);
					this.topicTermEntries.add(entry);
				} else if (isStartSendesSection) {
					Double prob = new Double(array[1].trim());
					UserEntry entry = new UserEntry(name, prob);
					this.topicSenderEntries.add(entry);
				} else if (isStartrecipientsSectons) {
					UserEntry entry = new UserEntry(name, 0.0); //TODO prob value
					this.topicRecipientEntries.add(entry);
				}
			}				
		}
		scanner.close();
	}


	public DatasetPropertiesConfig getPropertiesConfig() {
		return propertiesConfig;
	}


	public void setPropertiesConfig(DatasetPropertiesConfig propertiesConfig) {
		this.propertiesConfig = propertiesConfig;
	}


	public UnlabelingTopicListDisplayPanel getTopicListPanel() {
		return topicListPanel;
	}


	public TopicDetailDisplayPanel getTopicDetailPanel() {
		return topicDetailPanel;
	}


	public List<UserEntry> getTopicSenderEntries() {
		return topicSenderEntries;
	}


	public List<UserEntry> getTopicRecipientEntries() {
		return topicRecipientEntries;
	}


	public List<TermEntry> getTopicTermEntries() {
		return topicTermEntries;
	}
	
	
}
