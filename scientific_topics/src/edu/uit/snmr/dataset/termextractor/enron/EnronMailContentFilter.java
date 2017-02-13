/**
 * filter will return art matrix and message-id.txt
 * the art matrix is: sender-recipient-messageId.
 */
package edu.uit.snmr.dataset.termextractor.enron;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.log4j.Logger;

import edu.uit.snmr.confs.DatasetPropertiesConfig;
import edu.uit.snmr.exceptions.ErrorCode;
import edu.uit.snmr.exceptions.SnmrException;

/**
 * @author muonnv
 *
 */
public class EnronMailContentFilter {
	private static Logger logger = Logger.getLogger(EnronMailContentFilter.class);

	private static String SENT_BY = "Sent by: ";
	private static String FROM = "From: ";
	private static String TO = "To: ";
	private static String CC = "cc: ";
	private static String DUPLICATED_SUBJECT = "Subject: ";
	private static String FW = "FW: ";
	private static String RE = "Re: ";

	private static String ORIGINAL_MESSAGE = ".*?[-]{3,}Original Message[-]{3,}.*?";
	private static String FORWARDED_BY = "[-]+ Forwarded by .*?[-]*?";
	private static SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z (z)");

	private volatile Integer currentMessageId = 0;
	private volatile Integer nPairAppearances = 0;
	private DatasetPropertiesConfig datasetConfig;

	private FileWriter messageIdsWriter;
	private FileWriter authorRecipientMessageWriter;
	private Map<String, Integer> userMap;
	

	/**
	 * @param config
	 */
	public EnronMailContentFilter(DatasetPropertiesConfig datasetConfig) throws SnmrException {
		this.datasetConfig = datasetConfig;
		this.userMap = new HashMap<String, Integer>();
		//	this.artMap = new HashMap<String, Integer[]>();
		try {
			Scanner scanner = new Scanner(new File(datasetConfig.getEmployeeListFile()));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] array = line.trim().split("\t");
				this.userMap.put(array[0].trim(), Integer.valueOf(array[1].trim()));
			}
			scanner.close();
		}catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			throw new SnmrException(ErrorCode.SNMR_IOE_001, e.getMessage());
		}

	}

	public void doFilterMessages(EnronMailFilterOptions options) throws SnmrException {
		try {
			this.messageIdsWriter = new FileWriter(datasetConfig.getMessageIdFilePath(), true);		
			this.authorRecipientMessageWriter = new FileWriter(datasetConfig.getMessageSenderRecipientsFile(), true);
			//		currentUserIId = 0;
			File corpus = new File(datasetConfig.getCorpusPath());
			if (corpus.isDirectory()) {
				File[] users = corpus.listFiles();
				if (users != null) {
					for (File user : users) {
						filterMessageOnUser(user, options);
					}
				}
			}
			
			//store nPairAppearances
			this.datasetConfig.store();
		} catch (IOException e) {
			throw new SnmrException(ErrorCode.SNMR_IOE_001, e.getMessage());
		} finally {
			try {
				if (messageIdsWriter != null) {messageIdsWriter.close();}
				if (authorRecipientMessageWriter != null) {authorRecipientMessageWriter.close();}
			} catch (IOException ex) {
				throw new SnmrException(ErrorCode.SNMR_IOE_001, ex.getMessage());
			}
		}
	}


	private void filterMessageOnUser(File usernameFoler, EnronMailFilterOptions options) throws SnmrException {
		if (usernameFoler.isDirectory()) {
			File[] messages = usernameFoler.listFiles();
			if (messages != null) {
				for (File file : messages) {
					filterMessageOnFolder(file, options);
				}
			}

		}

	}


	private void filterMessageOnFolder(File directory, EnronMailFilterOptions options) throws SnmrException {
		if (directory.isDirectory()) {
			File[] messages = directory.listFiles();
			if (messages != null) {
				for (File file : messages) {
					if (file.isFile()) {
						filterOnMessage(file, options);
					}
				}
			}

		}
	}

	private void filterOnMessage(File messageFile, EnronMailFilterOptions options) throws SnmrException{
		if (messageFile == null || !messageFile.exists()) {
			return;
		}

		try {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			MimeMessage message = new MimeMessage(session,  new FileInputStream(messageFile));
			String messageId = message.getMessageID();
			messageId = messageId.substring(1, messageId.length() - 1);
			Date sentDate = sdf.parse(message.getHeader("Date")[0]);

			String from = message.getFrom()[0].toString();
			if (from == null || from.trim().isEmpty()) {
				System.out.println(messageFile.getAbsolutePath());
				return;
			}
			Integer senderId = userMap.get(from);
			if (senderId == null) {
				System.out.println("the author '" + from + "' not found");
				return;
			}
			Address[] recipients = message.getRecipients(RecipientType.TO);
			List<Integer> validRecipientIds = this.getValidRecipients(recipients, senderId); // removed id that equal to  senderId
			if (validRecipientIds == null || validRecipientIds.isEmpty()) {
				System.out.println("the recipients '" + Arrays.toString(recipients) + "' not found");
				return;
			}

			Address[] cc = message.getRecipients(RecipientType.CC);
			Address[] bcc = message.getRecipients(RecipientType.BCC);
			String subject = message.getSubject();

			String content = message.getContent().toString();
			String cleanedContent = "";
			Scanner scanner = new Scanner(content);
			boolean isbreak = false;
			while (scanner.hasNextLine() && !isbreak) {
				String acc = scanner.nextLine().trim();
				if (acc.matches(FORWARDED_BY)) {
					if (options.isRemoveForwardedContent()) {
						isbreak = true;
					}
				} else if (acc.matches(ORIGINAL_MESSAGE))  {
					if (options.isRemoveOriginalContent()) {
						isbreak = true;
					}
				} else if (acc.startsWith(FROM) || acc.startsWith(SENT_BY)) {
					if (options.isRemoveOriginalContent() && options.isRemoveForwardedContent()) {				
						isbreak = true;
					}
				} else	if (acc.startsWith(TO)) {
					if (options.isRemoveOriginalContent() && options.isRemoveForwardedContent()) {				
						isbreak = true;
					} else {

						boolean isBreakExtraInfo = false;
						while (scanner.hasNextLine() && !isBreakExtraInfo) {
							acc = scanner.nextLine().trim();
							if (acc.startsWith(DUPLICATED_SUBJECT)) {
								isBreakExtraInfo = true;
							}
						}
						if (acc.startsWith(DUPLICATED_SUBJECT)) {
							if (!acc.contains(subject)) {
								cleanedContent += acc + "\n";
							}
						}
					}
				}
				else if (acc.startsWith(DUPLICATED_SUBJECT)) {
					if (options.isRemoveOriginalContent() && options.isRemoveForwardedContent()) {				
						isbreak = true;
					} else if (!acc.contains(subject)) {
						cleanedContent += acc + "\n";
					}
				}
				else {
					cleanedContent += !acc.trim().isEmpty()? acc.trim() + "\n" : "";
				}
			}
			scanner.close();

			//	Integer mid = messageIdMap.get(messageId);

			if (!validRecipientIds.isEmpty() && !cleanedContent.trim().isEmpty()) {
				currentMessageId = currentMessageId + 1;
				messageId = renameMessageName(messageId);
				this.messageIdsWriter.write(messageId + "\t" + currentMessageId + "\n");

				writeToAuthorRecipientMessageFile(senderId, validRecipientIds, currentMessageId);				
				writeMessageContent(messageId, subject, cleanedContent);
				
				//increase nPairAppearances for art model
				nPairAppearances += validRecipientIds.size();
			}
			return;
		} catch (FileNotFoundException e) {			
			logger.error(e.getMessage());
			throw new SnmrException(ErrorCode.SNMR_IOE_001, e.getMessage());
		} catch (ParseException e) {
			logger.error(messageFile.getAbsoluteFile() + e.getMessage());
			throw new SnmrException(ErrorCode.SNMR_COMM_010, e.getMessage());
		} catch (MessagingException e) {
			logger.error(messageFile.getAbsoluteFile() + e.getMessage());
			throw new SnmrException(ErrorCode.SNMR_COMM_010, e.getMessage());
		} catch (IOException e) {
			logger.error(messageFile.getAbsoluteFile() + e.getMessage());
			throw new SnmrException(ErrorCode.SNMR_COMM_010, e.getMessage());
		} 
	}



	private void writeMessageContent(String messageId, String subject,	String msgContent) throws IOException {
		Writer writer = null;

		try {
			File folder = new File(datasetConfig.getFilteredMessageFolder());
			if (!folder.exists()) {
				folder.mkdir();
			}
			//messageId = renameMessageName(messageId);

			File file = new File(folder, messageId + ".txt");
			if (!file.exists()) {
				writer = new FileWriter(file);
				writer.write(String.format("%s\n%s\n", subject, msgContent));
				writer.flush();
			}
		} 	
		finally {
			if (writer != null) {writer.close();}
		}
	}

	private String renameMessageName(String messageName) {
		messageName = messageName.replaceAll("[@]", "_");
		messageName = messageName.replaceAll("[.]", "_");
		return messageName;
	}

	private void writeToAuthorRecipientMessageFile(Integer senderId,
			List<Integer> validRecipientIds, Integer messageIdAsInt) throws IOException {
		for (Integer recipientId : validRecipientIds) {
			this.authorRecipientMessageWriter.write(messageIdAsInt + "\t" + senderId + "\t" + recipientId + "\n");
		}
	}

	public List<Integer> getValidRecipients(Address[] recipients, Integer senderId) {
		List<Integer> recipientIds = new ArrayList<Integer>();
		if (recipients != null) {
			for (int i = 0; i < recipients.length; i++) {
				String recipient = recipients[i].toString();
				if (userMap.containsKey(recipient)) {
					Integer recipientId = userMap.get(recipient);
					recipientIds.add(recipientId);
				}
			}
			recipientIds.remove(senderId);
		}
		return recipientIds;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			DatasetPropertiesConfig config = DatasetPropertiesConfig.getInstance();

			EnronMailContentFilter filter =
					new EnronMailContentFilter(config);
			EnronMailFilterOptions options = new EnronMailFilterOptions(true, true);
			//filter.filterOnMessage(new File("C:/data/8061057_1075842134098_JavaMail_evans_thyme.txt"), 1, options);
			filter.doFilterMessages(options);
			//System.out.println(artMap.size());
		} catch (SnmrException e) {
			e.printStackTrace();
		} 

	}

}
