/**
 * 
 */
package edu.uit.snmr.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import edu.uit.snmr.confs.DatasetPropertiesConfig;

/**
 * @author muonnv
 *
 */
public class TermExtractionResultPanel extends JPanel{
	private JTextArea  messageAuthorRecipientsArea;
	private JTextArea messageTermFreArea;
	private JTextArea termIdArea;
	private DatasetPropertiesConfig datasetPropertiesConfig = DatasetPropertiesConfig.getInstance();

	public TermExtractionResultPanel() {
		setLayout(new GridLayout(1, 3));
		setName(TermExtractionResultPanel.class.toString());
		try {
			makeMessageAuthorRecipientsArea();
			makeMessageTermFreArea();
			makeTermIdArea();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(TermExtractionResultPanel.this, 
					e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	private void makeTermIdArea() throws FileNotFoundException {
		JPanel container = new JPanel(new BorderLayout());
		TitledBorder title = BorderFactory.createTitledBorder("Term-Id matrix");
		container.setBorder(title);
		
		this.termIdArea = new JTextArea();
		ScrollPane scrollPane = new ScrollPane();
		
		StringBuilder builder = loadDataContent(datasetPropertiesConfig.getTermIdsPath(), "Term", "ID");
		this.termIdArea.setText(builder.toString());
		this.termIdArea.setEditable(false);
		scrollPane.add(termIdArea);
		
		container.add(scrollPane, BorderLayout.CENTER);
		this.add(container);
	}

	private void makeMessageTermFreArea() throws FileNotFoundException {
		JPanel container = new JPanel(new BorderLayout());
		TitledBorder title = BorderFactory.createTitledBorder("Message-Term-Frequency matrix");
		container.setBorder(title);
		
		this.messageTermFreArea = new JTextArea();
		ScrollPane scrollPane = new ScrollPane();		
		StringBuilder builder = loadDataContent(datasetPropertiesConfig.getMessageTermFrequenlyPath(), "Msg", "Term", "Freq");
		this.messageTermFreArea.setText(builder.toString());
		this.messageTermFreArea.setEditable(false);
		scrollPane.add(messageTermFreArea);
		
		container.add(scrollPane, BorderLayout.CENTER);
		this.add(container);
	}

	private void makeMessageAuthorRecipientsArea() throws FileNotFoundException {
		JPanel container = new JPanel(new BorderLayout());
		TitledBorder title = BorderFactory.createTitledBorder("Message-Author-Recipients matrix");
		container.setBorder(title);
		
		this.messageAuthorRecipientsArea = new JTextArea();
		ScrollPane scrollPane = new ScrollPane();
		
		StringBuilder builder = loadDataContent(datasetPropertiesConfig.getMessageSenderRecipientsFile(), "Msg", "Author", "Recipient");
		this.messageAuthorRecipientsArea.setText(builder.toString());
		messageAuthorRecipientsArea.setEditable(false);
		scrollPane.add(messageAuthorRecipientsArea);
		container.add(scrollPane,  BorderLayout.CENTER);
		this.add(container);
	}

	private StringBuilder loadDataContent(String pairFilePath, String... headers) throws FileNotFoundException {
		StringBuilder builder = new StringBuilder();
		String headerLine = ""; 
		for (String header : headers) {
			headerLine += header + "\t";
		}
		builder.append(headerLine.trim() + "\n");
		Scanner fpPair = new Scanner(new File(pairFilePath)); //email_author_recipient.txt
		while (fpPair.hasNextLine()) {
			String line = fpPair.nextLine();				
			if (line != null && line.trim().length() > 0) {					
				builder.append(line + "\n");
			} 
		}
		fpPair.close();
		return builder;
	}

}
