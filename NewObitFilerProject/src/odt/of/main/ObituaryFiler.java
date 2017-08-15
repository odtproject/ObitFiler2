package odt.of.main;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.Ostermiller.util.Browser;

import odt.of.gui.GUI;
import odt.of.util.DirectorySetup;
import odt.of.util.ErrorLog;

public class ObituaryFiler
{
	static final int VERSION = 100;
	static final int LAST = 0;
	static final int FIRST = 1;
	static final int MAIDEN = 2;
	static final int OTHER = 3;
	static final int NICK = 4;
	public static final int AGE = 5;
	static final int BIRTHCITY = 6;
	static final int BIRTHSTATE = 7;
	static final int CITY = 8;
	static final int STATE = 9;
	static final int ODTFIELDS = 10;

	public static GUI gui;
	public ObitEntryConfig config;
	Vector<ObitEntry> todayObits;
	Vector<ObitEntry> previousObits;
	int previousSelected;
	int todaySelected;
	StringSelection clipString;
	String abbreviations;
	DirectorySetup dirSetup;
	
	public static void main(String[] paramArrayOfString)
	{
		ObituaryFiler localObituaryFiler = new ObituaryFiler();
		gui = new GUI(localObituaryFiler);
		localObituaryFiler.initialize();
	}
  
	public void menuHelpAbout()
	{
		/* Get the version number for this */
		String versionNumber = ObituaryFiler.class.getPackage().getImplementationVersion();

		String message_str = "Obituary Filer 2 Version " + versionNumber + "\n";
		message_str = message_str + "\nCopyright \u00a9 1996-1999 Michael Rice, All rights reserved.\n\n";
		message_str = message_str + "Updated by Alice Ramsay - 2016.\n\n";

		JOptionPane.showMessageDialog(null, message_str, "About Obituary Filer 2", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void menuHelpContents()
	{
		try {
			Browser.displayURL("http://www.rootsweb.ancestry.com/~obituary/manual/obitfiler_help.html");
		}
		catch (IOException localIOException)
		{
			new ErrorLog(localIOException, "IOException in menuHelpContents");
			
			String message_str = "Online help unavailable at this time.";
			JOptionPane.showMessageDialog(null, message_str, "Obituary Filer Help Menu Message", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void menuHelpAbbreviations()
	{
		try {
			Browser.displayURL("http://www.rootsweb.ancestry.com/~obituary/abbrev.txt");
		}
		catch (IOException localIOException)
		{
			new ErrorLog(localIOException, "IOException in menuHelpAbbreviations");
			
			String message_str = "Online abbreviations unavailable at this time.";
			JOptionPane.showMessageDialog(null, message_str, "Obituary Filer Help Menu Message", JOptionPane.ERROR_MESSAGE);
		}
	}
  
	public void initialize()
	{
		if (this.config.isConfigured())
		{
			gui.initTextFields();
			gui.initListBoxes();
			readToday();
			gui.changeDateLabel();
			gui.fillTodayList(this.todayObits);
			generatePreviousList();
			gui.fillPreviousList(this.previousObits);
		}
	}
  
	void merge(ObitEntry paramObitEntry)
	{
		String str = paramObitEntry.sortKey();
		for (int i = 0; i < this.previousObits.size(); i++)
		{
			ObitEntry localObitEntry = this.previousObits.elementAt(i);
			if (str.compareTo(localObitEntry.sortKey()) < 0)
			{
				this.previousObits.insertElementAt(paramObitEntry, i);
				return;
			}
		}
		this.previousObits.addElement(paramObitEntry);
	}
  
	public void menuActionSetDate()
	{
		gui.doDateDialog();
		Date localDate = gui.getChangedDate();
		if (localDate != null)
		{
			writeToday();
			this.config.setCurrentDate(localDate);
			initialize();
		}
	}
  
	public void menuFileExit()
	{
		if (this.config.isConfigured())
		{
			writeToday();
			this.config.incrementDate();
			writeConfigFile();
		}
		System.exit(0);
	}
  
	public ObituaryFiler()
	{
		/* Get path information for files */
		dirSetup = new DirectorySetup();
		if ( ! dirSetup.isSetupComplete() ) {
			return;
		}
		
		Browser.init();
		
		readConfigFile();
		this.todayObits = new Vector<ObitEntry>();
		this.previousObits = new Vector<ObitEntry>();
		this.previousSelected = -1;
		this.todaySelected = -1;
		this.abbreviations = "";		
		
		try
		{
			/* Use the ODT folder structure to find the abbreviations file.
			 */
			String filePathString = dirSetup.getLibFileString("abbrev.txt");
			BufferedReader localBufferedReader = new BufferedReader(new FileReader(filePathString));
			while (localBufferedReader.ready())
			{
				String str = localBufferedReader.readLine() + "\n";
				this.abbreviations += str;
			}
			localBufferedReader.close();
			return;
		}
		catch (FileNotFoundException localFileNotFoundException)
		{
			new ErrorLog(localFileNotFoundException, "Error opening abbrev.txt");
			return;
		}
		catch (IOException localIOException)
		{
			new ErrorLog(localIOException, "Error reading abbreviation file.");
		}
	}
  
	public void menuActionExport()
	{
		String msgString;
		if (!hasDirtyItems())
		{
			/* Beep for the user to know there's a problem. */
			Toolkit.getDefaultToolkit().beep();

			msgString = "There are no Unsent Obits to Export!";
			JOptionPane.showMessageDialog(null, msgString, "ObituaryFiler Message", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Clipboard localClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		String str1 = processDirtyFiles();
		this.clipString = new StringSelection(str1);
		localClipboard.setContents(this.clipString, this.clipString);
		String filePathString = writeSentFile(str1);
		msgString = "All unsent entries have been stored in the system ";
		msgString = msgString + "clipboard.\nOpen your email program and paste them ";
		msgString = msgString + "in now.\n\n";
		if (filePathString != null)
		{
			msgString = msgString + "A backup of these entries has been stored in the ";
			msgString = msgString + "file " + filePathString + ".\n\n";
		}
		else
		{
			msgString = msgString + "Normally a backup of these entries is stored in ";
			msgString = msgString + "a file in the Sent directory. This operation ";
			msgString = msgString + "has failed.\n\n";
		}
		msgString = msgString + "After you are done pasting hit the Ok button.";
		gui.doMessageBox(msgString);
		writeToday();
		gui.buttonPanel.statusLabel.setText("");
	}
  
	public void MenuActionConfigure()
	{
		gui.doConfigDialog();
		writeConfigFile();
		gui.initTextFields();
		gui.initListBoxes();
		initialize();
	}
  
	public Vector<ObitEntry> read(Date paramDate)
	{
		return readFile(this.config.getCurrentPaperAbbreviation() + File.separator + this.config.getDateFILE(paramDate));
	}
  
	public void writeFile(String fileString, Vector<ObitEntry> paramVector)
	{
		try
		{
			/* The files should be in a folder that is writable by the user. */
			String filePathString = DirectorySetup.getAppFileString(fileString);
			File localFile = new File(filePathString.toString());
			if (paramVector.size() == 0)
			{
				if (localFile.exists()) {
					localFile.delete();
				}
				return;
			}
			
			FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
			ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localFileOutputStream);
			localObjectOutputStream.writeObject(paramVector);
			localObjectOutputStream.close();
			return;
		}
		catch (IOException localIOException)
		{
			new ErrorLog(localIOException, "IOException in writeFile: " + localIOException.getMessage());
		}
	}
  
	@SuppressWarnings("unchecked")
	public Vector<ObitEntry> readFile(String fileNameString)
	{
		Vector<ObitEntry> localVector = new Vector<ObitEntry>();
		try
		{
			/* Get a path to a file in a folder that is writable by the user. */
			String filePathString = DirectorySetup.getAppFileString(fileNameString);
			File localFile = new File(filePathString.toString());
			if (localFile.exists())
			{
				FileInputStream localFileInputStream = new FileInputStream(localFile);
				ObjectInputStream localObjectInputStream = new ObjectInputStream(localFileInputStream);
				localVector = (Vector<ObitEntry>)localObjectInputStream.readObject();
				localObjectInputStream.close();
			}
		}
		catch (FileNotFoundException localFileNotFoundException)
		{
			new ErrorLog(localFileNotFoundException, "FileNotFoundException: " + fileNameString);
		}
		catch (IOException localIOException)
		{
			new ErrorLog(localIOException, "IOException in readFile: " + fileNameString);
		}
		catch (ClassNotFoundException localClassNotFoundException)
		{
			new ErrorLog(localClassNotFoundException, "ClassNotFoundException in readFile");
		}
		
		return localVector;
	}
  
	public void buttonClear()
	{
		gui.initTextFields();
	}
  
	void generatePreviousList()
	{
		Date localDate = this.config.getCurrentDate();
		int i = this.config.getCurrentDaysPrevious();
		if (this.previousObits != null) {
			this.previousObits.removeAllElements();
		}
		
		for (int j = 0; j < i; j++)
		{
			localDate = this.config.getPreviousDate(localDate);
			Vector<ObitEntry> localVector = read(localDate);
			Enumeration<ObitEntry> localEnumeration = localVector.elements();
			while (localEnumeration.hasMoreElements())
			{
				ObitEntry localObitEntry = localEnumeration.nextElement();
				merge(localObitEntry);
			}
		}
	}
  
	public void choicePaperChange(String paramString)
	{
		writeToday();
		this.config.setPaper(paramString);
		initialize();
	}
  
	public void buttonEdit()
	{
		if (this.todaySelected != -1)
		{
			gui.removeTodayItem(this.todaySelected);
			ObitEntry localObitEntry = this.todayObits.elementAt(this.todaySelected);
			gui.fillTextFields(localObitEntry);
			this.todayObits.removeElementAt(this.todaySelected);
			this.todaySelected = -1;
			writeToday();
			return;
		}
		Toolkit.getDefaultToolkit().beep();
	}
  
	public void buttonDelete()
	{
		if (this.todaySelected != -1)
		{
			gui.removeTodayItem(this.todaySelected);
			this.todayObits.removeElementAt(this.todaySelected);
			this.todaySelected = -1;
			writeToday();
			return;
		}
		Toolkit.getDefaultToolkit().beep();
	}
  
  
	public void selectedPreviousItem(Integer paramInteger)
	{
		this.previousSelected = paramInteger.intValue();
	}
  
	public void doubleClickItem(String paramString)
	{
		ObitEntry localObitEntry1 = this.previousObits.elementAt(this.previousSelected);
		ObitEntry localObitEntry2 = (ObitEntry)localObitEntry1.getClone();
		localObitEntry2.date = this.config.getCurrentDate();
		localObitEntry2.dirty = true;
		this.todayObits.addElement(localObitEntry2);
		writeToday();
		gui.addToToday(localObitEntry2.format(this.config));
		gui.initTextFields();
		List localList = gui.todayPanel.obitList;
		localList.makeVisible(localList.getItemCount() - 1);
	}
    
	public void writeConfigFile()
	{
		try
		{
			/* Add on the path to a folder that is writable by the user. */
			String filePathString = DirectorySetup.getAppFileString("obit.dat");
			
			if ( ! filePathString.equals("Invalid") ) {
				File configFile = new File(filePathString.toString());

				FileOutputStream localFileOutputStream = new FileOutputStream(configFile.toString());
				ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localFileOutputStream);
				localObjectOutputStream.writeObject(this.config);
				localObjectOutputStream.close();
				return;
			}
		}
		catch (IOException localIOException)
		{
			new ErrorLog(localIOException, "Error writing initialization file obit.dat");
		}
	}
  
	public void readConfigFile()
	{
		try
		{
			/* Add on the path to a folder that is writable by the user. */
			
			String filePathString = DirectorySetup.getAppFileString("obit.dat");
			File configFile = new File(filePathString);

			FileInputStream localFileInputStream = new FileInputStream(configFile.toString());
			ObjectInputStream localObjectInputStream = new ObjectInputStream(localFileInputStream);
			
			this.config = ((ObitEntryConfig)localObjectInputStream.readObject());
			localObjectInputStream.close();
			return;
		}
		catch (FileNotFoundException localFileNotFoundException)
		{
			this.config = new ObitEntryConfig();
			return;
		}
		catch (IOException localIOException)
		{
			new ErrorLog(localIOException, "Error reading initialization file obit.dat");
			return;
		}
		catch (ClassNotFoundException localClassNotFoundException)
		{
			new ErrorLog(localClassNotFoundException, "ClassNotFoundException in readConfigFile for obit.dat");
		}
	}
  
	private String writeSentFile(String paramString)
	{
		String filePathString = null;
		try
		{
			/* Path to "Sent" folder that is writable by the user. */
			filePathString = DirectorySetup.getAppSubFolderString("Sent");
			
			/* If the directory set up was unsuccessful, log the error 
			 * and return.
			 */
			if ( filePathString.equals("Invalid") ) {
				new ErrorLog(null, "Error: file not saved.");
				return null;
			}

			File sentFolder = new File(filePathString);		
			
			/* Get the date */
			Date localDate = new Date();
			
			/* Put together the name of the file, which includes the date in the name */
			String sentObitsFileString, sentObitsFileSeq;
			sentObitsFileString = this.config.getDateFILE(localDate) + '.';
			
			/* The user may send several submissions in one day, so each file has a 
			 * sequential number at the end.  We have to keep looking
			 * until we don't find an already existing file with the sequential number.
			 */
			NumberFormat localNumberFormat = NumberFormat.getInstance();
			localNumberFormat.setMinimumIntegerDigits(3);
			int i = 0;
			int j = 0;
			do
			{
				String seqNumber = localNumberFormat.format(j);
				
				sentObitsFileSeq = new String(sentObitsFileString + seqNumber);
				Path sentFilePath = Paths.get(sentFolder.toString(),sentObitsFileSeq);

				File sentObitsFile = new File(sentFilePath.toString());
				if (!sentObitsFile.exists()) {
						PrintWriter localPrintWriter = new PrintWriter(new FileOutputStream(sentObitsFile));
						StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "\n");
						while (localStringTokenizer.hasMoreTokens()) {
							localPrintWriter.println(localStringTokenizer.nextToken());
						}
						localPrintWriter.close();
						i = 1;
						
						/* AJR: added this return. */
						return filePathString; 
						
				} /* end of if sentObitsFile does not exist */
				
				j++;			
			
			} while (j <= 999);
			
			if (i == 0)
			{
				new ErrorLog(null, "Error: file not saved.");
				return null;
			}

		}
		catch (IOException localIOException)
		{
			new ErrorLog(localIOException, "Error writing Sent obits");
		}

		return filePathString;
	}
  
	public void writeToday()
	{
		try
		{
			/* Each paper has a folder.  Then under the folder is a file named with the
			 * date that includes the sent obits.  We need the full path to that dated
			 * file, but we only want to store the last part of the path in the dirty files.
			 */
			String todayFileName = this.config.getCurrentDateFILE();
			String paperNameFolderString = this.config.getCurrentPaperAbbreviation();
			Path paperFilePath = Paths.get(paperNameFolderString, todayFileName);
			
			Path fullFilePath = Paths.get(DirectorySetup.getAppFilesPathString(), paperFilePath.toString());
			String todayFilePathString = fullFilePath.toString();
			File todayFile = new File(todayFilePathString);			

			/* If there are not any obits for today, then check for a file for today's
			 * obits and delete it if it exists.  Then return.
			 */
			if (this.todayObits.size() == 0)
			{
				if (todayFile.exists()) {
					todayFile.delete();
				}
				return;
			}
			
			/* Otherwise, there are obits to record */
			this.config.addDirtyFile(paperFilePath.toString());
			FileOutputStream localFileOutputStream = new FileOutputStream(todayFile);
			ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localFileOutputStream);
			localObjectOutputStream.writeObject(this.todayObits);
			localObjectOutputStream.close();
			return;
		}
		catch (IOException localIOException)
		{
			new ErrorLog(localIOException, "IOException in writeToday");
		}
		catch ( InvalidPathException ip ) {
			System.err.println("Error writing Today obits: " + ip.getMessage());
		}
	}
  
	public void readToday()
	{
		this.todayObits = read(this.config.getCurrentDate());
	}
  
	public String processDirtyFiles()
	{
		String str1 = new String("");
		int i;
		for (i = 0; i < this.config.dirtyFiles.size(); i++)
		{
			String fileName = (String)this.config.dirtyFiles.elementAt(i);
			Vector<ObitEntry> localVector = readFile(fileName);
			for (int j = 0; j < localVector.size(); j++)
			{
				ObitEntry localObitEntry = localVector.elementAt(j);
				if (localObitEntry.dirty)
				{
					str1 = str1 + "\n" + localObitEntry.format(this.config);
					localObitEntry.dirty = false;
				}
			}
			writeFile(fileName, localVector);
		}
		for (i = 0; i < this.todayObits.size(); i++) {
			this.todayObits.elementAt(i).dirty = false;
		}
		this.config.dirtyFiles.removeAllElements();
		return str1;
	}
  
	public void selectedTodayItem(Integer paramInteger)
	{
		this.todaySelected = paramInteger.intValue();
	}
  
	public boolean hasDirtyItems()
	{
		for (int i = 0; i < this.config.dirtyFiles.size(); i++)
		{
			String str = (String)this.config.dirtyFiles.elementAt(i);
			Vector<ObitEntry> localVector = readFile(str);
			for (int j = 0; j < localVector.size(); j++)
			{
				ObitEntry localObitEntry = localVector.elementAt(j);
				if (localObitEntry.dirty) {
					return true;
				}
			}
		}
		return false;
	}
  
	public void buttonAdd()
	{
		if (gui.isValidText())
		{
			this.config.addAutoCity(gui.getAutoCity());
			Vector<String> localVector = gui.getTextFields();
			ObitEntry localObitEntry = new ObitEntry(localVector, this.config.getCurrentDate(), this.config.getCurrentPaperAbbreviation(), this.config.getCurrentPaperLocation(), this.config.getTagname());
			this.todayObits.addElement(localObitEntry);
			gui.addToToday(localObitEntry.format(this.config));
			writeToday();
			gui.initTextFields();
			List localList = gui.todayPanel.obitList;
			localList.makeVisible(localList.getItemCount() - 1);
			return;
		}
		Toolkit.getDefaultToolkit().beep();
	}
  
	public void buttonPrev()
	{
		writeToday();
		this.config.decrementDate();
		initialize();
	}
  
	public void buttonNext()
	{
		writeToday();
		this.config.incrementDate();
		initialize();
	}
}
