/**
 * 
 */
package edu.uit.snmr.view;

import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author muonnv
 *
 */
public class UnlabelingTopicListDisplayPanel extends JPanel implements ListSelectionListener{
	private String[] topicNames;
	private Integer[] topicValues;
	private Integer selectedTopicNr;
	private String selectedTopicName;
	private JList list;
	private TopicManageDisplayPanel parent;
	
	
	public UnlabelingTopicListDisplayPanel(TopicManageDisplayPanel parent, List<Integer> topicNrs, int selectedTopicNr) {
		this.parent = parent;
		this.selectedTopicNr = selectedTopicNr;
		this.topicValues = topicNrs.toArray(new Integer[0]);
		this.topicNames = new String[this.topicValues.length];
		for (int i = 0; i < this.topicNames.length; i++) {
			this.topicNames[i] = this.topicValues[i] + "";
		}
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		createContentPane();
		this.selectedTopicName = this.topicNames[selectedTopicNr];
		//this.list.setSelectedValue(this.selectedTopicName, true);
	}

	private void createContentPane() {
		this.setLayout(new GridLayout(1, 1));
		this.list = new JList(this.topicNames); //data has type Object[]
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.addListSelectionListener(this);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setAlignmentX(LEFT_ALIGNMENT);
		//listScroller.setPreferredSize(new Dimension(250, 500));
		
		this.add(listScroller);
	}

	public void valueChanged(ListSelectionEvent e) {
		String topicName = (String) list.getSelectedValue();
		for (int i = 0; i < this.topicNames.length; i++) {
			if (topicName.equalsIgnoreCase(this.topicNames[i])) {
				this.selectedTopicNr = this.topicValues[i];
				this.parent.changeTopicNr(selectedTopicNr);
				break;
			}
		}
		
	}
	
	public int getSelectedTopic() {
		return selectedTopicNr;
	}

	public void updateTopicLabel(Map<String, String> result) {
		Set<String> keySet = result.keySet();
		for (int i = 0; i < topicValues.length; i++) {
			String acc = topicValues[i] + "";
			if (keySet.contains(acc)) {
				this.topicNames[i] = result.get(acc);
			}			
		}
		this.list.setListData(topicNames);
		this.list.setSelectedIndex(this.selectedTopicNr);
		this.selectedTopicName = this.topicNames[this.selectedTopicNr];
	}
}
