package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.zip.ZipException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeModel;

import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.OutlineModel;

import edu.uit.snmr.view.utils.ArchiveFileFilter;
import edu.uit.snmr.view.utils.GUIUtils;
import edu.uit.snmr.view.utils.MyRowModel;
import edu.uit.snmr.view.utils.MyTreeNode;
import edu.uit.snmr.view.utils.TestZipFile;
import edu.uit.snmr.view.utils.WrapLayout;


public class TopicImportDialog extends JDialog {
	JPanel container = new JPanel();
	JPanel north = new JPanel(new WrapLayout(FlowLayout.LEADING));
	JPanel center = new JPanel();
	JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
	MyTreeNode top = new MyTreeNode("All");
	JRadioButton jRadioButton_selectDir = new JRadioButton(
			GUIUtils.get("select_dir"));
	JRadioButton jRadioButton_selectArch = new JRadioButton(
			GUIUtils.get("select_arch"));
	JTextField jTextField_selectDir = new JTextField(20);
	JTextField jTextField_selectArch = new JTextField(20);
	JButton jButton_selectDir = new JButton(GUIUtils.get("browse"));
	JButton jButton_selectArch = new JButton(GUIUtils.get("browse"));
	JScrollPane jScrollPane = null;
	Outline outline = new Outline();
	DefaultTreeModel model = new DefaultTreeModel(top);
	OutlineModel outlineModel;

	public TopicImportDialog() {
		setLayout(new BorderLayout());
		container.setLayout(new BorderLayout());
		initComponents();

		add(container, BorderLayout.CENTER);
		setTitle(GUIUtils.get("title_gui"));
		setModal(true);
		setSize(500, 650);
		setResizable(false);
		setVisible(true);
	}

	private void initComponents() {

		ButtonGroup bg = new ButtonGroup();
		bg.add(jRadioButton_selectDir);
		bg.add(jRadioButton_selectArch);

		north.add(jRadioButton_selectDir);
		north.add(jTextField_selectDir);
		north.add(jButton_selectDir);
		north.add(jRadioButton_selectArch);
		north.add(jTextField_selectArch);
		north.add(jButton_selectArch);

		// init south panel
		JButton jButton_import = new JButton(GUIUtils.get("import"));
		JButton jButton_cancel = new JButton(GUIUtils.get("cancel"));

		south.add(jButton_import);
		south.add(jButton_cancel);

		// add panels to main panel
		center.setBorder(BorderFactory.createTitledBorder(GUIUtils
				.get("topics_title")));
		addStuffToCenter();
		container.add(north, BorderLayout.NORTH);
		container.add(center, BorderLayout.CENTER);
		container.add(south, BorderLayout.SOUTH);

		disableSelectArch();
		jRadioButton_selectDir.doClick();
		// Add action listener for buttons

		jButton_selectArch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jFileChooser_Arch = new JFileChooser();
				// jFileChooser_Arch.showOpenDialog(GUI.this);
				jFileChooser_Arch.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jFileChooser_Arch.setFileFilter(new ArchiveFileFilter());
				int returnVal = jFileChooser_Arch.showOpenDialog(TopicImportDialog.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser_Arch.getSelectedFile();
					jTextField_selectArch.setText(file.getPath());
					createNodes(file.getPath());
					outline.repaint();
				}
			}
		});

		jButton_selectDir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser_Dir = new JFileChooser();
				// jFileChooser_Dir.showOpenDialog(GUI.this);
				jFileChooser_Dir
						.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = jFileChooser_Dir.showOpenDialog(TopicImportDialog.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser_Dir.getSelectedFile();
					jTextField_selectDir.setText(file.getPath());
					createNodes(file.getPath());
					outline.validate();
				}
			}
		});
		jRadioButton_selectArch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				enableSelectArch();
			}
		});

		jRadioButton_selectDir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				disableSelectArch();

			}
		});

		jButton_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		jButton_import.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				top = GUIUtils.removeUncheckedRow(top);
				boolean result = GUIUtils.exportXML(top);
				if (result) {
					GUI.root = GUIUtils.convertToTreeNode(top);
//					JOptionPane.showMessageDialog(GUI_Tree.this, "Successful");
				}
				dispose();
			}
		});
	}

	private void addStuffToCenter() {
		outlineModel = DefaultOutlineModel.createOutlineModel(model,
				new MyRowModel());

		outline.setRootVisible(true);
		outline.setModel(outlineModel);
		jScrollPane = new JScrollPane(outline);
		jScrollPane.setPreferredSize(new Dimension(450, 450));
		center.add(jScrollPane);
	}

	private void enableSelectArch() {
		jTextField_selectArch.setEnabled(true);
		jButton_selectArch.setEnabled(true);
		jTextField_selectDir.setText("");
		jTextField_selectDir.setEnabled(false);
		jButton_selectDir.setEnabled(false);

	}

	private void disableSelectArch() {
		jTextField_selectArch.setText("");
		jTextField_selectArch.setEnabled(false);
		jButton_selectArch.setEnabled(false);
		jTextField_selectDir.setEnabled(true);
		jButton_selectDir.setEnabled(true);
	}

	private void createNodes(String fileName) {
		top = new MyTreeNode("All");
		// for folder
		File file = new File(fileName);
		if (file.isFile()) {
			try {
				TestZipFile.loadTopicsFromDir(fileName, top);
			} catch (ZipException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			GUIUtils.loadTopicsFromDir(fileName, top);

		model = new DefaultTreeModel(top);
		outlineModel = DefaultOutlineModel.createOutlineModel(model,
				new MyRowModel());
		outline.setModel(outlineModel);
	}

	public static void main(String[] args) {
		new TopicImportDialog();
	}
}
