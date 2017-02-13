package edu.uit.snmr.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import edu.uit.snmr.confs.DatasetPropertiesConfig;
import edu.uit.snmr.view.utils.GUIUtils;
import edu.uit.snmr.view.utils.TextFileFilter;

public class DatasetConfDialog extends JPanel {

	Date from = null;
	Date to = null;
	JPanel container = new JPanel();
//	JButton jButton_save;
	final JTextField jTextField_selectDataSet = new JTextField(20);
	final JTextField jTextField_selectUser = new JTextField(20);
	final JTextField jTextField_resultDir = new JTextField(20);
	final JTextField jTextField_topics = new JTextField(20);
	final JTextField jTextField_from = new JTextField(20);
	final JTextField jTextField_to = new JTextField(20);
	final JTextField jTextField_alpha = new JTextField(20);
	final JTextField jTextField_beta = new JTextField(20);
	final JTextField jTextField_burnIn = new JTextField(20);
	final JTextField jTextField_sampling = new JTextField(20);

	private DatasetPropertiesConfig datasetConf = DatasetPropertiesConfig.getInstance();

	public DatasetConfDialog(final SnmrFrame parent) {
		setName(DatasetConfDialog.class.toString());
		//		setLocationRelativeTo(parent);
		//		setModal(true);
		//		pack();
		//		
		final DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		try {
			from = format.parse(datasetConf.getValidFrom());
			to = format.parse(datasetConf.getValidTo());
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		setLayout(new BorderLayout());
		container.setLayout(new BorderLayout());
		JPanel center = new JPanel();
		JPanel south = new JPanel();

		FormLayout layout = new FormLayout(
				"right:pref, 4dlu, right:30dlu, 4dlu, 20dlu, 4dlu, 20dlu, 4dlu, right:40dlu, 4dlu, 20dlu, 4dlu, 20dlu, 4dlu, min",
				"pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu");
		layout.setRowGroups(new int[][] { { 1, 3, 5 } });
		center.setLayout(layout);

		JButton jButton_selectDataset = new JButton(GUIUtils.get("browse"));
		JButton jButton_selectUser = new JButton(GUIUtils.get("browse"));
		JButton jButton_resultDir = new JButton(GUIUtils.get("browse"));

		jTextField_selectDataSet.setText(datasetConf.getCorpusPath());
		jTextField_selectUser.setText(datasetConf.getEmployeeListFile());
		jTextField_resultDir.setText(datasetConf.getResultFolder());
		jTextField_topics.setText(datasetConf.getNTopics() +"");
		jTextField_from.setText(datasetConf.getValidFrom() + "");
		jTextField_to.setText(datasetConf.getValidTo());
		jTextField_alpha.setText(datasetConf.getAlpha() + "");
		jTextField_beta.setText(datasetConf.getBeta() + "");
		jTextField_burnIn.setText(datasetConf.getBurnin() + "");
		jTextField_sampling.setText(datasetConf.getNSamples() + "");

		JCalendarButton jCalendarButton_from = new JCalendarButton();
		JCalendarButton jCalendarButton_to = new JCalendarButton();

		CellConstraints cc = new CellConstraints();
		// row 1
		center.add(new JLabel(GUIUtils.get("select_dataset")), cc.xy(1, 1));
		center.add(jTextField_selectDataSet, cc.xyw(3, 1, 11));
		center.add(jButton_selectDataset, cc.xy(15, 1));
		// row 3
		center.add(new JLabel(GUIUtils.get("select_users")), cc.xy(1, 3));
		center.add(jTextField_selectUser, cc.xyw(3, 3, 11));
		center.add(jButton_selectUser, cc.xy(15, 3));
		// row 5
		center.add(new JLabel(GUIUtils.get("result_dir")), cc.xy(1, 5));
		center.add(jTextField_resultDir, cc.xyw(3, 5, 11));
		center.add(jButton_resultDir, cc.xy(15, 5));
		// row 7
		center.add(new JLabel(GUIUtils.get("number_topics")), cc.xy(1, 7));
		center.add(jTextField_topics, cc.xyw(3, 7, 11));
		// row 9
		center.add(new JLabel(GUIUtils.get("time_interval")), cc.xy(1, 9));
		center.add(jTextField_from, cc.xyw(3, 9, 3));
		center.add(jCalendarButton_from, cc.xy(7, 9));
		center.add(jTextField_to, cc.xyw(9, 9, 3));
		center.add(jCalendarButton_to, cc.xy(13, 9));

		//		add action listener
		jTextField_resultDir.setEditable(false);
		jTextField_selectDataSet.setEditable(false);
		jTextField_selectUser.setEditable(false);
		jTextField_from.setEditable(false);
		jTextField_to.setEditable(false);

		jTextField_topics.addKeyListener(new NumberOnlyKeyListener());
		jTextField_alpha.addKeyListener(new DoubleOnlyKeyListener());
		jTextField_beta.addKeyListener(new DoubleOnlyKeyListener());
		jTextField_burnIn.addKeyListener(new NumberOnlyKeyListener());
		jTextField_sampling.addKeyListener(new NumberOnlyKeyListener());

		jCalendarButton_from
		.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
			public void propertyChange(
					java.beans.PropertyChangeEvent evt) {
				if (evt.getNewValue() instanceof Date) {
					jTextField_from.setText(format.format(evt
							.getNewValue()));
					from = (Date) evt.getNewValue();

				}
			}
		});

		jCalendarButton_to
		.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
			public void propertyChange(
					java.beans.PropertyChangeEvent evt) {
				if (evt.getNewValue() instanceof Date) {
					to = (Date) evt.getNewValue();
					if (from == null || from.compareTo(to) <= 0)
						jTextField_to.setText(format.format(evt
								.getNewValue()));
					else {
						JOptionPane.showMessageDialog(
								DatasetConfDialog.this, "Invalid date",
								"Warning", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});

		// row 11
		center.add(new JLabel(GUIUtils.get("hyberparameters")), cc.xy(1, 11));
		center.add(new JLabel(GUIUtils.get("alpha")), cc.xy(3, 11));
		center.add(jTextField_alpha, cc.xyw(5, 11, 3));
		center.add(new JLabel(GUIUtils.get("beta")), cc.xy(9, 11));
		center.add(jTextField_beta, cc.xyw(11, 11, 3));
		// row 13
		center.add(new JLabel(GUIUtils.get("iter_number")), cc.xy(1, 13));
		center.add(new JLabel(GUIUtils.get("burn_in")), cc.xy(3, 13));
		center.add(jTextField_burnIn, cc.xyw(5, 13, 3));
		center.add(new JLabel(GUIUtils.get("sample")), cc.xy(9, 13));
		center.add(jTextField_sampling, cc.xyw(11, 13, 3));

		// south panel components
		//		jButton_analyze = new JButton(GUIUtils.get("analyze"));
		//		jButton_analyze.setIcon(new ImageIcon("resources/images/analyze.gif"));
		JButton jButton_save = new JButton(GUIUtils.get("save"));
		jButton_save.setIcon(new ImageIcon("resources/images/save.gif"));
				JButton jButton_cancel = new JButton(GUIUtils.get("cancel"));
				jButton_cancel.setIcon(new ImageIcon("resources/images/cancel.gif"));

//				south.add(jButton_analyze, cc.xy(3, 15));
		south.add(jButton_save, cc.xy(5, 15));
//				south.add(jButton_cancel, cc.xy(7, 15));

		// add panels to main panel
		center.setBorder(BorderFactory.createTitledBorder(GUIUtils.get("form")));
		south.setLayout(new FlowLayout(FlowLayout.CENTER));
		container.add(center, BorderLayout.CENTER);
		container.add(south, BorderLayout.SOUTH);
		add(container, BorderLayout.CENTER);
		// add action listener
		jButton_selectDataset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser_Dir = new JFileChooser();
				jFileChooser_Dir
				.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = jFileChooser_Dir
						.showOpenDialog(DatasetConfDialog.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser_Dir.getSelectedFile();
					jTextField_selectDataSet.setText(file.getPath());
				}
			}
		});
		jButton_selectUser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jFileChooser_Arch = new JFileChooser();
				jFileChooser_Arch.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jFileChooser_Arch.setFileFilter(new TextFileFilter());
				int returnVal = jFileChooser_Arch
						.showOpenDialog(DatasetConfDialog.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser_Arch.getSelectedFile();
					jTextField_selectUser.setText(file.getPath());
				}
			}
		});
		jButton_resultDir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser_Dir = new JFileChooser();
				jFileChooser_Dir
				.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = jFileChooser_Dir
						.showOpenDialog(DatasetConfDialog.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser_Dir.getSelectedFile();
					jTextField_resultDir.setText(file.getPath());
				}
			}
		});

		jButton_save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				parent.saveDatasetConfiguration();				
			}
		});
		//		jButton_analyze.addActionListener(new ActionListener() {
		//
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				String dataset = jTextField_selectDataSet.getText();
		//				String users = jTextField_selectUser.getText();
		//				String resultDir = jTextField_resultDir.getText();
		//				String topics = jTextField_topics.getText();
		//				String from = jTextField_from.getText();
		//				String to = jTextField_to.getText();
		//				String alpha = jTextField_alpha.getText();
		//				String beta = jTextField_beta.getText();
		//				String burnIn = jTextField_burnIn.getText();
		//				String sampling = jTextField_sampling.getText();
		////				dispose();
		//				boolean result = GUIUtils.saveProperties(dataset, users,
		//						resultDir, topics, from, to, alpha, beta, burnIn,
		//						sampling);
		//				// Show information about result of save method
		//				// call method to analyze...
		//				if (result) {
		//					parent.doTopicModelling();
		//				}				
		//				
		//			}
		//		});

		//		jButton_cancel.addActionListener(new ActionListener() {
		//
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				dispose();
		//			}
		//		});

		//		setTitle(GUIUtils.get("title_configForm"));
		//		setModal(true);
		//		setSize(650, 290);
		//		setResizable(false);
		setVisible(true);
	}

	// For progress monitor
	class Task extends SwingWorker<Void, Void> {
		@Override
		public Void doInBackground() {
			Random random = new Random();
			int progress = 0;
			setProgress(0);
			try {
				Thread.sleep(1000);
				while (progress < 100 && !isCancelled()) {
					// Sleep for up to one second.
					Thread.sleep(random.nextInt(1000));
					// Make random progress.
					progress += random.nextInt(10);
					setProgress(Math.min(progress, 100));
				}
			} catch (InterruptedException ignore) {
			}
			return null;
		}

		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			//			jButton_analyze.setEnabled(true);
			// progressMonitor.setProgress(0);
		}
	}

	public DatasetPropertiesConfig retrieveConfig() {
		DatasetPropertiesConfig prop = DatasetPropertiesConfig.getInstance();

		String dataset = jTextField_selectDataSet.getText();
		String users = jTextField_selectUser.getText();
		String resultDir = jTextField_resultDir.getText();
		String topics = jTextField_topics.getText();
		String from = jTextField_from.getText();
		String to = jTextField_to.getText();
		String alpha = jTextField_alpha.getText();
		String beta = jTextField_beta.getText();
		String burnIn = jTextField_burnIn.getText();
		String sampling = jTextField_sampling.getText();
		// set the properties value
		prop.setCorpusPath(dataset);
		prop.setEmployeeListFile(users);
		prop.setResultFolder(resultDir);
		prop.setNTopics(Integer.parseInt(topics));
		prop.setValidFrom(from);
		prop.setValidTo(to);
		prop.setAlpha(Double.parseDouble(alpha));
		prop.setBeta(Double.parseDouble(beta));
		prop.setBurnin(Integer.parseInt(burnIn));
		prop.setNSamples(Integer.parseInt(sampling));

		return prop;		
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		jTextField_selectDataSet.setEditable(enabled);
		jTextField_selectUser.setEditable(enabled);
		jTextField_resultDir.setEditable(enabled);
		jTextField_topics.setEditable(enabled);
		jTextField_from.setEditable(enabled);
		jTextField_to.setEditable(enabled);
		jTextField_alpha.setEditable(enabled);
		jTextField_beta.setEditable(enabled);
		jTextField_burnIn.setEditable(enabled);
		jTextField_sampling.setEditable(enabled);
	}
}