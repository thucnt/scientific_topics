package view;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import edu.uit.snmr.view.TopicalWordSetDialog;
import edu.uit.snmr.view.utils.GUIUtils;
import edu.uit.snmr.view.utils.MyMutableTreeNode;


public class GUI extends JFrame {
	public static MutableTreeNode root = new DefaultMutableTreeNode("All");
	JPanel jPanelCenter = new JPanel();
	JPanel jPanelEast = new JPanel();
	JPanel jPanelSouth = new JPanel();
	DefaultTreeModel treeModel = new DefaultTreeModel(root);
	JTree tree = new JTree(treeModel);
	JScrollPane jScoJScrollPane = new JScrollPane(tree);
	JMenuItem jMenuItem_rename = new JMenuItem(GUIUtils.get("popup_rename"));
	JMenuItem jMenuItem_delete = new JMenuItem(GUIUtils.get("popup_delete"));
	JMenuItem jMenuItem_showWords = new JMenuItem(
			GUIUtils.get("popup_showWords"));

	public GUI() {
		initComponents();
		initMenu();

		jScoJScrollPane.setVisible(false);
		setTitle(GUIUtils.get("tilte_main")); //
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 500);
		// setResizable(false);
		setVisible(true);
	}

	public void initComponents() {
		setLayout(new BorderLayout());

		// init east panel

		jPanelEast.add(jScoJScrollPane);
		add(jPanelCenter, BorderLayout.CENTER);
		add(jPanelEast, BorderLayout.EAST);
		add(jPanelSouth, BorderLayout.SOUTH);
	}

	public void initMenu() {
		JMenuBar bar = new JMenuBar();
		JMenu jMenuFile = new JMenu(GUIUtils.get("menu_file"));
		JMenu jMenuEdit = new JMenu(GUIUtils.get("menu_edit"));
		JMenu jMenuView = new JMenu(GUIUtils.get("menu_view"));
		JMenu jMenuHelp = new JMenu(GUIUtils.get("menu_help"));
		// for file menu
		JMenuItem jMenuItemImport = new JMenuItem(
				GUIUtils.get("menuItem_import"));
		jMenuItemImport.setAccelerator(KeyStroke.getKeyStroke('T',
				Event.CTRL_MASK));
		JMenuItem jMenuItemConfig = new JMenuItem(
				GUIUtils.get("menuItem_config"));
		jMenuItemConfig.setAccelerator(KeyStroke.getKeyStroke('D',
				Event.CTRL_MASK));
		JMenuItem jMenuItemExit = new JMenuItem(GUIUtils.get("menuItem_exit"));
		jMenuItemExit.setAccelerator(KeyStroke
				.getKeyStroke('X', Event.ALT_MASK));
		// for help menu
		JMenuItem jMenuItemAbout = new JMenuItem(GUIUtils.get("menuItem_about"));
		// add to file menu
		jMenuFile.add(jMenuItemImport);
		jMenuFile.add(jMenuItemConfig);
		jMenuFile.addSeparator();
		jMenuFile.add(jMenuItemExit);
		// add to help menu
		jMenuHelp.add(jMenuItemAbout);
		// add to menu bar
		bar.add(jMenuFile);
		bar.add(jMenuEdit);
		bar.add(jMenuView);
		bar.add(jMenuHelp);
		setJMenuBar(bar);
		// add action
		tree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					Rectangle pathBounds = tree.getUI().getPathBounds(tree,
							path);
					tree.setSelectionPath(path);
					if (pathBounds != null
							&& pathBounds.contains(e.getX(), e.getY())) {
						JPopupMenu menu = new JPopupMenu();
						menu.add(jMenuItem_rename);
						menu.add(jMenuItem_delete);
						menu.add(jMenuItem_showWords);
						menu.show(tree, pathBounds.x, pathBounds.y
								+ pathBounds.height);
					}
				}
			}
		});

		jMenuItem_delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode dmtn, node;
				TreePath path = tree.getSelectionPath();
				dmtn = (DefaultMutableTreeNode) path.getLastPathComponent();
				node = (DefaultMutableTreeNode) dmtn.getParent();
				// Bug fix by essam
				int nodeIndex = node.getIndex(dmtn); // declare an integer to
														// hold the selected
														// nodes index
				dmtn.removeAllChildren(); // remove any children of selected
											// node
				node.remove(nodeIndex); // remove the selected node, retain its
										// siblings
				((DefaultTreeModel) treeModel)
						.nodeStructureChanged((TreeNode) dmtn);
			}
		});

		jMenuItem_rename.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode dmtn;
				TreePath path = tree.getSelectionPath();
				dmtn = (DefaultMutableTreeNode) path.getLastPathComponent();
				tree.setEditable(true);
				tree.startEditingAtPath(path);
				((DefaultTreeModel) treeModel)
						.nodeStructureChanged((TreeNode) dmtn);
			}
		});
		
		jMenuItem_showWords.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MyMutableTreeNode dmtn;
				TreePath path = tree.getSelectionPath();
				dmtn = (MyMutableTreeNode) path.getLastPathComponent();
				new TopicalWordSetDialog(dmtn.getWordset(), dmtn.getUserObject().toString());
			}
		});

		jMenuItemImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TopicImportDialog();
				if (root.getChildCount() > 0) {
					tree.setEditable(true);
					treeModel.setRoot(root);
					jScoJScrollPane.setVisible(true);
					validate();
				}
			}
		});

		jMenuItemConfig.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DatasetConfDialog pa = new DatasetConfDialog();
			}
		});

		jMenuItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		jMenuItemAbout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(GUI.this, GUIUtils.get("about"));
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GUI();
			}
		});
	}
}
