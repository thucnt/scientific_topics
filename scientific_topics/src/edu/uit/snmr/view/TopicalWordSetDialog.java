package edu.uit.snmr.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import edu.uit.snmr.view.utils.GUIUtils;

public class TopicalWordSetDialog extends JDialog {
	//JLabel jLabel_Title;
	JList list;

	public TopicalWordSetDialog(List<String> wordset, String title) {
		//this.jLabel_Title = new JLabel(GUIUtils.get("title")+title);//title
		this.list = new JList<String>(new Vector<String>(wordset));
		JScrollPane pane = new JScrollPane(list);
		
		list.setCellRenderer(new SelectedListCellRenderer());
		//add(jLabel_Title, BorderLayout.NORTH);
		add(pane, BorderLayout.CENTER);
		setTitle(GUIUtils.get("title")+title);
		setModal(true);
		setSize(300, 400);
		setResizable(false);
		setVisible(true);
	}
}

class SelectedListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (index%2==0) {
            c.setBackground(Color.LIGHT_GRAY);
        }
        else
        	c.setBackground(Color.white);
        return c;
    }
}