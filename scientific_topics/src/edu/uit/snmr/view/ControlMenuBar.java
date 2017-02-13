/**
 * 
 */
package edu.uit.snmr.view;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import edu.uit.snmr.view.utils.GUIUtils;

/**
 * @author muonnv
 *
 */
public class ControlMenuBar extends JMenuBar{
	private SnmrFrame parent;

	public ControlMenuBar(SnmrFrame parent) {
		this.parent = parent;
		this.add(makeFileMenu(GUIUtils.get("menu_file")));
		this.add(makeEditMenu(GUIUtils.get("menu_edit")));
		this.add(makeViewMenu(GUIUtils.get("menu_view")));
		this.add(makeHelpMenu(GUIUtils.get("menu_help")));
	}

	private JMenu makeHelpMenu(String name) {
		JMenu helpMenu = new JMenu(name);
		helpMenu.setMnemonic(KeyEvent.VK_R);
		helpMenu.getAccessibleContext().setAccessibleDescription("Help");

		//a group of JMenuItems
		JMenuItem howItem = new JMenuItem(GUIUtils.get("menuItem_howto"));
		howItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		howItem.getAccessibleContext().setAccessibleDescription("How to use Social Network Marketing Recommendation System");		
		helpMenu.add(howItem);

		//a group of JMenuItems
		JMenuItem aboutItem = new JMenuItem(GUIUtils.get("menuItem_about"));
		aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		aboutItem.getAccessibleContext().setAccessibleDescription("About the Social Network Marketing Recommendation System");		
		helpMenu.add(aboutItem);

		return helpMenu;
	}

	private JMenu makeViewMenu(String name) {
		JMenu viewMenu = new JMenu(name);
		viewMenu.setMnemonic(KeyEvent.VK_I);
		viewMenu.getAccessibleContext().setAccessibleDescription("View");

		//a group of JMenuItems
		JMenuItem textItem = new JMenuItem(GUIUtils.get("menuItem_text"), KeyEvent.VK_T);
		textItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		textItem.getAccessibleContext().setAccessibleDescription(GUIUtils.get("menuItem_text"));		
		viewMenu.add(textItem);

		//a group of JMenuItems
		JMenuItem graphItem = new JMenuItem(GUIUtils.get("menuItem_graph"), KeyEvent.VK_G);
		graphItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		graphItem.getAccessibleContext().setAccessibleDescription(GUIUtils.get("menuItem_graph"));		
		viewMenu.add(graphItem);
		return viewMenu;
	}

	private JMenu makeEditMenu(String name) {
		JMenu editMenu = new JMenu(name);
		editMenu.setMnemonic(KeyEvent.VK_H);
		return editMenu;
	}

	private JMenu makeFileMenu(String name) {
		JMenu fileMenu = new JMenu(name);
		fileMenu.setMnemonic(KeyEvent.VK_T);
		fileMenu.getAccessibleContext().setAccessibleDescription("File menu");

		//a group of JMenuItems
		JMenuItem importTopicMenuItem = new JMenuItem(GUIUtils.get("menuItem_import"));
		importTopicMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		importTopicMenuItem.getAccessibleContext().setAccessibleDescription(GUIUtils.get("menuItem_import"));
		importTopicMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				new TopicImportDialog(parent);				

			}});
		fileMenu.add(importTopicMenuItem);

		JMenuItem datasetConfMenuItem = new JMenuItem(GUIUtils.get("menuItem_config"));
		datasetConfMenuItem.setAccelerator(KeyStroke.getKeyStroke('D',Event.CTRL_MASK));
		importTopicMenuItem.getAccessibleContext().setAccessibleDescription(GUIUtils.get("menuItem_config"));
		datasetConfMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				new DatasetConfDialog(parent);
				
			}});
		fileMenu.add(datasetConfMenuItem);

		//a group of JMenuItems
		JMenuItem exitItem = new JMenuItem(GUIUtils.get("menuItem_exit"));
		exitItem.setAccelerator(KeyStroke.getKeyStroke('X', Event.ALT_MASK));		
		fileMenu.add(exitItem);

		return fileMenu;
	}
}
