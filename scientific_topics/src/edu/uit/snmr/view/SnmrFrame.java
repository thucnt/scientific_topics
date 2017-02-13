package edu.uit.snmr.view;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.tree.MutableTreeNode;

import org.apache.log4j.Logger;

import edu.uit.snmr.confs.DatasetPropertiesConfig;
import edu.uit.snmr.dataset.termextractor.enron.EnronFileTermExtractor;
import edu.uit.snmr.dataset.termextractor.enron.EnronMailContentFilter;
import edu.uit.snmr.dataset.termextractor.enron.EnronMailFilterOptions;
import edu.uit.snmr.topicmodeling.art.ARTAlgorithm;
import edu.uit.snmr.topicmodeling.art.AutoTopicLabeling;
import edu.uit.snmr.view.utils.GUIUtils;
import edu.uit.snmr.view.utils.MyTreeNode;

/**
 * 
 */

/**
 * @author muonnv
 *
 */
public class SnmrFrame extends JFrame implements PropertyChangeListener {	
	private Logger logger = Logger.getLogger(SnmrFrame.class);
	private TopicManageDisplayPanel topicManageDisplayPanel;
	private TopicListDisplayPanel topicListPanel;
	private TopicDetailDisplayPanel topicDetailPanel;
	private JPanel container = new JPanel();
	private DatasetConfDialog datasetConfDialog;
	private JProgressBar progressBar;
	private TermExtractionResultPanel termExtractionResultPanel;
	private int progress = 0;
	private DatasetPropertiesConfig datasetConfig = DatasetPropertiesConfig.getInstance();
	private EnronMailFilterOptions options = new EnronMailFilterOptions(true, true);
	private CopyOfControlMenuBar controlMenuBar;
	private TopicImportDialog  topicImportDialog;
	private Map<String, String> result = new HashMap<String, String>();

	public SnmrFrame( Dimension size) {
		this.setSize(size);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		Container contentPane = this.getContentPane();
		this.controlMenuBar = new CopyOfControlMenuBar(this);
		contentPane.add(this.controlMenuBar, BorderLayout.NORTH);
		this.container =  new JPanel(new BorderLayout());
		this.add(container, BorderLayout.CENTER);
		setViewByState(datasetConfig.getViewState());
	}


	private void setViewByState(int viewState) {
		this.controlMenuBar.setViewState(viewState);	

		if (datasetConfig.getViewState() == 0) {
			this.container.removeAll();
			this.revalidate();
		}
		if (datasetConfig.getViewState() == 1) {
			this.diplayDatasetConfigurationView();
		} else if(datasetConfig.getViewState() == 2) {
			this.displayTermExtractionResult();
		} else if(datasetConfig.getViewState() == 3) {
			this.displayTopicModelingResult();
		}

	}

	private void setViewState(int value) {
		try {
			this.datasetConfig.setViewState(value);
			datasetConfig.store();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	
	public void diplayTopicImportView() {
		if (!datasetConfig.isTrainningTopicImported() || topicImportDialog == null) {
			topicImportDialog = new TopicImportDialog(this);
		} else {
			loadImportedTopic(topicImportDialog.getTopNode());
		}
	}
	
	public void loadImportedTopic(MutableTreeNode tree) {	
		this.container.removeAll();
		this.container.revalidate();
		this.topicListPanel = new TopicListDisplayPanel(this, tree);			
		JPanel topicManageDisplayPanel = new JPanel();
		topicManageDisplayPanel.setLayout(new GridLayout(1, 1));
		topicManageDisplayPanel.add(topicListPanel);
		this.container.add(topicManageDisplayPanel, BorderLayout.CENTER);	
		this.container.revalidate();
	}

	public void diplayDatasetConfigurationView() {
		this.container.removeAll();
		this.datasetConfDialog = new DatasetConfDialog(this);
		this.container.add(this.datasetConfDialog, BorderLayout.CENTER);
		this.container.revalidate();
		this.setViewState(0);
		this.controlMenuBar.setViewState(0);
	}

	public void saveDatasetConfiguration() {
		this.datasetConfig = this.datasetConfDialog.retrieveConfig();
		try {
			this.datasetConfig.store();
			this.setViewState(1);
			this.controlMenuBar.setViewState(1);
			JOptionPane.showMessageDialog(SnmrFrame.this, "Saving sucessfully");
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(SnmrFrame.this, 
					GUIUtils.get("fail") + "(" + e1.getMessage()+ ")", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	public void runTermExtractionView() {
		if (datasetConfig.getViewState() != 1) {
			this.container.removeAll();
			this.datasetConfDialog = new DatasetConfDialog(this);
			this.container.add(this.datasetConfDialog, BorderLayout.CENTER);
			this.container.revalidate();
			this.setViewState(1);
		}
		this.container.add(makeProgressBar("Terms are extracting..."), BorderLayout.SOUTH);
		this.container.revalidate();
		TermExtractionTask task = new TermExtractionTask();
		task.addPropertyChangeListener(this);
		task.execute();
	}


	public void displayTermExtractionResult() {
		this.container.removeAll();
		this.termExtractionResultPanel = new TermExtractionResultPanel();
		this.container.add(this.termExtractionResultPanel, BorderLayout.CENTER);
		this.container.revalidate();		
	}

	public void runTopicModellingView() {
		if (datasetConfig.getViewState() != 2) {
			this.container.removeAll();
			this.termExtractionResultPanel = new TermExtractionResultPanel();
			this.container.add(this.termExtractionResultPanel, BorderLayout.CENTER);
			this.container.revalidate();	
			this.setViewState(2);
		}
		this.container.add(makeProgressBar("Topics are modeling..."), BorderLayout.SOUTH);
		this.container.revalidate();		
		TopicModelTask task = new TopicModelTask();
		task.addPropertyChangeListener(this);
		task.execute();
	}

	private void displayTopicModelingResult() {
		this.container.removeAll();
		List<Integer> unnameTopics = new ArrayList<Integer>();
		
		for (int i = 0; i < datasetConfig.getNTopics(); i++) {
			unnameTopics.add(i );
		}
		topicManageDisplayPanel = new TopicManageDisplayPanel(unnameTopics);
		this.container.add(topicManageDisplayPanel);
		this.container.revalidate();
	}
	
	public void runTopicLabelingView() {
		if (datasetConfig.getViewState() != 3) {
			this.container.removeAll();
			List<Integer> unnameTopics = new ArrayList<Integer>();
			for (int i = 0; i < datasetConfig.getNTopics(); i++) {
				unnameTopics.add(i);
			}
			this.container.add(new TopicManageDisplayPanel(unnameTopics));
			this.container.revalidate();
			this.setViewState(3);
		}
		this.container.add(makeProgressBar("Topics are labeling..."), BorderLayout.SOUTH);
		this.container.revalidate();		
		TopicLabelingTask task = new TopicLabelingTask();
		task.addPropertyChangeListener(this);
		task.execute();		
	}
	
	private void displayTopicLabelingResult() {
		topicManageDisplayPanel.getTopicListPanel().updateTopicLabel(result);
	}
	

	private JProgressBar makeProgressBar(String str) {
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);	 
		//Call setStringPainted now so that the progress bar height
		//stays the same whether or not the string is shown.
		progressBar.setStringPainted(true); 
		progressBar.setIndeterminate(true);
		progressBar.setString(str);

		return progressBar;		
	}


	//For progress monitor
	class TermExtractionTask extends SwingWorker<Void, Void> {
		@Override
		public Void doInBackground() {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			progress = 0;
			setProgress(progress);					
			try {
				EnronMailContentFilter messageFiler = new EnronMailContentFilter(datasetConfig);
				messageFiler.doFilterMessages(options);

				EnronFileTermExtractor termExtractor = new EnronFileTermExtractor();
				termExtractor.execute(datasetConfig.getFilteredMessageFolder(), datasetConfig.getResultFolder());
				progress = 100;
				setProgress(progress);
			} catch (Exception e) {
				progress = -1;
				logger.error(e.getMessage());
				e.printStackTrace();
			}			
			return null;
		}

		@Override
		public void done() {
			setCursor(null);
			Toolkit.getDefaultToolkit().beep();
		}
	}

	class TopicModelTask extends SwingWorker<Void, Void> {
		@Override
		public Void doInBackground() {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			progress = 0;
			setProgress(progress);	
			logger.info("Topic model: Starting gibbs sasmpling.....");
			ARTAlgorithm algorithm = new ARTAlgorithm(datasetConfig);
			algorithm.run();
			logger.info("Topic model: Starting writing result.....");
			algorithm.analyzeTopPairsWords();
			logger.info("Topic model: DONE");
			progress = 100;
			setProgress(progress);
			return null;
		}

		@Override
		public void done() {
			setCursor(null);
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	class TopicLabelingTask extends SwingWorker<Void, Void> {
		@Override
		public Void doInBackground() {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			progress = 0;
			setProgress(progress);	
			logger.info("Topic model: Starting labeling.....");
			
			try {
				AutoTopicLabeling autoTopicLabeling = new AutoTopicLabeling("tree.xml");
				result = autoTopicLabeling.doLabelingForTopics(new File(datasetConfig.getExtractedTopicsFolder()));				
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());
				JOptionPane.showMessageDialog(SnmrFrame.this, 
						"Error found! Please check the log file", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			logger.info("Topic labeling: DONE");
			progress = 100;
			setProgress(progress);
			return null;
		}

		@Override
		public void done() {
			setCursor(null);
			Toolkit.getDefaultToolkit().beep();
		}
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if ("state" == evt.getPropertyName()) {
			String value = evt.getNewValue().toString();
			revalidate();
			if (value.equals("DONE")) {
				if (progress == 100) {
					if (datasetConfig.getViewState() == 1) {
						this.displayTermExtractionResult();
						this.setViewState(2);
						this.controlMenuBar.setViewState(2);
					} else if(datasetConfig.getViewState() == 2) {
						this.setViewState(3);
						this.displayTopicModelingResult();
						this.controlMenuBar.setViewState(3);
					}  else if(datasetConfig.getViewState() == 3) {						
						this.displayTopicLabelingResult();
						this.container.remove(progressBar);
					}
				} else if (progress == -1) {
					this.container.remove(progressBar);
					this.container.revalidate();
					JOptionPane.showMessageDialog(SnmrFrame.this, 
							"Error found! Please check the log file", "Error", JOptionPane.ERROR_MESSAGE);

				}
			}
		} 
	}


}
