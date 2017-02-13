/**
 * 
 */
package edu.uit.snmr.view;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import edu.uit.snmr.view.utils.GUIUtils;
import edu.uit.snmr.view.utils.MyMutableTreeNode;

/**
 * @author muonnv
 *
 */
public class TopicListDisplayPanel extends JPanel {

	private SnmrFrame parent;
//	private MyTreeNode top;
	private  MutableTreeNode root;
	private DefaultTreeModel treeModel;
	private JTree tree;
	
	JScrollPane jScoJScrollPane = new JScrollPane(tree);
	JMenuItem jMenuItem_rename = new JMenuItem(GUIUtils.get("popup_rename"));
	JMenuItem jMenuItem_delete = new JMenuItem(GUIUtils.get("popup_delete"));
	JMenuItem jMenuItem_showWords = new JMenuItem(GUIUtils.get("popup_showWords"));


	public TopicListDisplayPanel(SnmrFrame parent, MutableTreeNode tree) {
		this.parent = parent;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.root = tree;
		this.treeModel= new DefaultTreeModel(root);
		this.tree = new JTree(treeModel);
		createContentPane();		
	}

	private void createContentPane() {
		JScrollPane jScoJScrollPane = new JScrollPane(tree);
		this.add(jScoJScrollPane);
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
				((DefaultTreeModel) treeModel).nodeStructureChanged((TreeNode) dmtn);
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
				System.out.println(dmtn.getWordset());
				new TopicalWordSetDialog(dmtn.getWordset(), dmtn.getUserObject().toString());
			}
		});
	}
}
