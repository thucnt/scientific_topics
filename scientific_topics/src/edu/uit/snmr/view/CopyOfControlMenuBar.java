/**
 * 
 */
package edu.uit.snmr.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import edu.uit.snmr.view.utils.GUIUtils;

/**
 * @author muonnv
 *
 */
public class CopyOfControlMenuBar extends JToolBar implements ActionListener{
	private static final String DATASET_CONFIG = "configDateset";
	private static final String TRAINING_TOPIC_IMPORT = "importTrainingTopics";
	private static final String TERM_EXTRACT = "extractTerms";
	private static final String TOPIC_MODEL = "modelTopic";
	private static final String TOPIC_AUTO_LABELING = "labelTopics";

	private SnmrFrame parent;
	private JButton topicImportButton;
	private JButton datasetConfigButton;
	//private JButton saveDatasetConfigButton;
	private JButton termExtractionButton;
	private JButton topicModelingButton;
	private JButton topiLabelingButton;
	

	

	public CopyOfControlMenuBar(SnmrFrame parent) {
		this.parent = parent;
		
		this.topicImportButton = makeToolbarItem(TRAINING_TOPIC_IMPORT, 
				"resources/images/ImportTopic2.png", 
				GUIUtils.get("menuItem_importTopic"), GUIUtils.get("menuItem_importTopic"));
		this.add(this.topicImportButton);
		JLabel seperatorImage = new JLabel(new ImageIcon("resources/images/next.png"));
		seperatorImage.setEnabled(false);
		this.add(seperatorImage);
		this.add(new JLabel(" "));
		
		this.datasetConfigButton = makeToolbarItem(DATASET_CONFIG, 
				"resources/images/datasetConfig.png", 
				GUIUtils.get("menuItem_config"), GUIUtils.get("menuItem_config"));
		this.add(this.datasetConfigButton);
		
		JLabel seperatorImage2 = new JLabel(new ImageIcon("resources/images/next.png"));
		seperatorImage2.setEnabled(false);
		this.add(seperatorImage2);
		this.add(new JLabel(" "));
		
	
		this.termExtractionButton = makeToolbarItem(TERM_EXTRACT, 
				"resources/images/termExtract.png", 
				GUIUtils.get("term_extract"), GUIUtils.get("term_extract"));
		this.add(this.termExtractionButton);
		
		JLabel seperatorImage4 = new JLabel(new ImageIcon("resources/images/next.png"));
		seperatorImage4.setEnabled(false);
		this.add(seperatorImage4);
		this.add(new JLabel(" "));
		
		this.topicModelingButton = makeToolbarItem(TOPIC_MODEL, 
				"resources/images/topicModel2.png", 
				GUIUtils.get("topic_model"), GUIUtils.get("topic_model"));
		this.add(this.topicModelingButton);

		JLabel seperatorImage5 = new JLabel(new ImageIcon("resources/images/next.png"));
		seperatorImage5.setEnabled(false);
		this.add(seperatorImage5);	
		this.add(new JLabel(" "));
		
		this.topiLabelingButton = makeToolbarItem(TOPIC_AUTO_LABELING, 
				"resources/images/labeling.png", 
				GUIUtils.get("menuItem_labelTopic"), GUIUtils.get("menuItem_labelTopic"));
		this.add(this.topiLabelingButton);
		
	}

	private JButton makeToolbarItem(String actionCommand, String iconURL,  String toolTipText, String altText) {
		//URL imageURL = CopyOfControlMenuBar.class.getResource(iconURL);

		//Create and initialize the button.
		JButton button = new JButton();
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		button.addActionListener(this);

		if (iconURL != null) {                      //image found
			button.setIcon(new ImageIcon(iconURL, altText));
		} else {                                     //no image found
			button.setText(altText);
			System.err.println("Resource not found: "+ iconURL);
		}

		return button;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		// Handle each button.
		if (TRAINING_TOPIC_IMPORT.equals(cmd)) {
			parent.diplayTopicImportView();
		}
		if (DATASET_CONFIG.equals(cmd)) { //first button clicked
			parent.diplayDatasetConfigurationView();
		} else if (TERM_EXTRACT.equals(cmd)) { // third button clicked
			parent.runTermExtractionView();
		}  else if (TOPIC_MODEL.equals(cmd)) { // third button clicked
			parent.runTopicModellingView();
		} else if (TOPIC_AUTO_LABELING.equals(cmd)) { // second button clicked
			parent.runTopicLabelingView();
		} 
	}

	public void setViewState(int state) {
		//this.saveDatasetConfigButton.setEnabled(false);
		this.termExtractionButton.setEnabled(false);
		this.topicModelingButton.setEnabled(false);		
		this.topiLabelingButton.setEnabled(false);
		if (state == 1) {
			this.termExtractionButton.setEnabled(true);
		} else if (state == 2) {
			this.termExtractionButton.setEnabled(true);
			this.topicModelingButton.setEnabled(true);	
			
		} else if (state == 3) {
			this.termExtractionButton.setEnabled(true);
			this.topicModelingButton.setEnabled(true);
			this.topiLabelingButton.setEnabled(true);
		}
	}
}
