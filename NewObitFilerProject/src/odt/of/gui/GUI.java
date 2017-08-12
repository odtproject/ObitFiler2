package odt.of.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Toolkit;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import odt.of.main.ObitEntry;
import odt.of.main.ObituaryFiler;
import odt.of.util.ErrorLog;

public class GUI
{
	ObituaryFiler app;
	ObitMenuBar menuBar;
	public ObitListPanel previousPanel;
	public ObitListPanel todayPanel;
	TextPanel textPanel;
	public ButtonPanel buttonPanel;
	Frame mainFrame;
	SetDateDialog dateDialog;
	EditConfigDialog configDialog;
	MessageBox messageBox;
  
	public void addToPrevious(String paramString)
	{
		this.previousPanel.add(paramString);
	}
  
	public void updateConfigChanges()
	{
		this.previousPanel.updateConfigChanges();
		this.todayPanel.updateConfigChanges();
		this.textPanel.updateConfigChanges();
		this.buttonPanel.updateConfigChanges();
		this.menuBar.enableComponents(this.app.config.isConfigured());
		this.textPanel.enableComponents(this.app.config.isConfigured());
		this.buttonPanel.enableComponents(this.app.config.isConfigured());
		this.mainFrame.pack();
	}
  
	public void doDateDialog()
	{
		if (this.dateDialog == null) {
			this.dateDialog = new SetDateDialog(this.app, this.mainFrame, "Set Date");
		}
		this.dateDialog.showIt();
	}
  
	public void doConfigDialog()
	{
		if (this.configDialog == null) {
			this.configDialog = new EditConfigDialog(this.app, this.mainFrame, "Edit Configuration");
		}
		this.configDialog.showIt();
		updateConfigChanges();
	}
  
	public void changeDateLabel()
	{
		if (this.buttonPanel.dateLabel == null) {
			new ErrorLog(null, "null date label in changeDateLabel");
		}
		if (this.app.config.isConfigured()) {
			this.buttonPanel.dateLabel.setText(this.app.config.getCurrentDateDISPLAY());
		}
	}
  
	public GUI(ObituaryFiler paramObituaryFiler)
	{
		this.app = paramObituaryFiler;
		this.menuBar = new ObitMenuBar(paramObituaryFiler);
		Panel localPanel1 = setupTopPanel(paramObituaryFiler, this.todayPanel, this.previousPanel);
		this.textPanel = new TextPanel(paramObituaryFiler);
		this.buttonPanel = new ButtonPanel(paramObituaryFiler);
		Panel localPanel2 = new Panel();
		localPanel2.add(this.textPanel);
		localPanel2.add(this.buttonPanel);
		this.mainFrame = new Frame();
		this.mainFrame.setTitle("Obituary Filer");
		this.mainFrame.setMenuBar(this.menuBar);
		this.mainFrame.add("North", localPanel1);
		this.mainFrame.add("Center", localPanel2);
		this.mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("dove_purple.gif"));
		updateConfigChanges();
		this.mainFrame.pack();
		Dimension localDimension1 = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension localDimension2 = this.mainFrame.getSize();
		this.mainFrame.setLocation((localDimension1.width - localDimension2.width) / 2, (localDimension1.height - localDimension2.height) / 2);
		this.mainFrame.setVisible(true);
		this.mainFrame.addWindowListener(new CommandListener(0, paramObituaryFiler));
	}
  
	public Date getChangedDate()
	{
		return this.dateDialog.theDate;
	}
  
	public void removeTodayItem(int paramInt)
	{
		this.todayPanel.remove(paramInt);
	}
  
	public void fillPreviousList(Vector<ObitEntry> paramVector)
	{
		Enumeration<ObitEntry> localEnumeration = paramVector.elements();
		while (localEnumeration.hasMoreElements())
		{
			ObitEntry localObitEntry = localEnumeration.nextElement();
			String str = localObitEntry.format(this.app.config);
			this.previousPanel.add(str);
		}
	}
  
	public void initTextFields()
	{
		clearTextFields();
		if (this.app.config.isConfigured())
		{
			this.textPanel.field[9].setText(this.app.config.getCurrentPaperLocation());
			this.textPanel.field[9].selectAll();
			if (this.app.hasDirtyItems()) {
				this.buttonPanel.statusLabel.setText("Unsent Obits");
			} else {
				this.buttonPanel.statusLabel.setText("");
			}
		}
		this.textPanel.field[0].requestFocus();
	}
  
	public void fillTodayList(Vector<ObitEntry> paramVector)
	{
		Enumeration<ObitEntry> localEnumeration = paramVector.elements();
		while (localEnumeration.hasMoreElements())
		{
			ObitEntry localObitEntry = localEnumeration.nextElement();
			String str = localObitEntry.format(this.app.config);
			this.todayPanel.add(str);
		}
	}
  
	public void clearTextFields()
	{
		this.textPanel.clearAll();
	}
  
	public void doMessageBox(String paramString)
	{
		if (this.messageBox == null) {
			this.messageBox = new MessageBox(this.mainFrame);
		}
		this.messageBox.showIt(paramString);
	}
  
	public void focusAndSelectTextField(int paramInt)
	{
		this.textPanel.field[paramInt].selectAll();
		this.textPanel.field[paramInt].requestFocus();
	}
  
	public String getAutoCity()
	{
		if (this.textPanel.aCCheckbox.getState()) {
			return this.textPanel.field[8].getText();
		}
		return "";
	}
  
	public void addToToday(String paramString)
	{
		this.todayPanel.add(paramString);
	}
  
	public void initListBoxes()
	{
		this.todayPanel.removeAll();
		this.previousPanel.removeAll();
	}
  
	public void fillTextFields(ObitEntry paramObitEntry)
	{
		this.textPanel.field[0].setText(paramObitEntry.getLastName());
		this.textPanel.field[1].setText(paramObitEntry.getFirstName());
		this.textPanel.field[2].setText(paramObitEntry.getMaidenName());
		this.textPanel.field[4].setText(paramObitEntry.getNickName());
		this.textPanel.field[5].setText(paramObitEntry.getAge());
		this.textPanel.field[8].setText(paramObitEntry.getCity());
		this.textPanel.field[9].setText(paramObitEntry.getState());
		this.textPanel.field[6].setText(paramObitEntry.getBirthCity());
		this.textPanel.field[7].setText(paramObitEntry.getBirthState());
		String str1 = new String("");
		Enumeration<String> localEnumeration = paramObitEntry.getOtherNames().elements();
		while (localEnumeration.hasMoreElements())
		{
			String str2 = localEnumeration.nextElement();
			if (str2.indexOf(' ') == -1) {
				str1 = str1 + str2 + ' ';
			} else {
				str1 = str1 + '"' + str2 + "\" ";
			}
		}
		this.textPanel.field[3].setText(str1.trim());
		int i = 0;
		do
		{
			this.textPanel.field[i].selectAll();
			i++;
		} while (i < 10);
		
		this.textPanel.field[0].requestFocus();
	}
  
	public Vector<String> getTextFields()
	{
		Vector<String> localVector = new Vector<String>(10);
		int i = 0;
		do
		{
			localVector.addElement(this.textPanel.getText(i));
			i++;
		} while (i < 10);
		return localVector;
	}
  
	private Panel setupTopPanel(ObituaryFiler paramObituaryFiler, ObitListPanel paramObitListPanel1, ObitListPanel paramObitListPanel2)
	{
		Panel localPanel = new Panel();
		localPanel.setLayout(new BorderLayout());
		this.previousPanel = new ObitListPanel(paramObituaryFiler, 300);
		localPanel.add("North", this.previousPanel);
		this.todayPanel = new ObitListPanel(paramObituaryFiler, 301);
		localPanel.add("South", this.todayPanel);
		return localPanel;
	}
  
	public boolean isValidText()
	{
		String str1 = this.textPanel.getText(0).trim();
		if (str1.equals(""))
		{
			focusAndSelectTextField(0);
			return false;
		}
		String str2 = this.textPanel.getText(1).trim();
		if (str2.equals(""))
		{
			focusAndSelectTextField(1);
			return false;
		}
		String str3 = this.textPanel.getText(3);
		int i = 0;
		for (int j = 0; j < str3.length(); j++) {
			if (str3.charAt(j) == '"') {
				i++;
			}
		}
		if (i % 2 != 0)
		{
			focusAndSelectTextField(3);
			return false;
		}
		String str4 = this.textPanel.getText(5).trim();
		if (!str4.equals(""))
		{
			try
			{
				Integer localInteger = new Integer(str4);
				if (localInteger.intValue() > 199)
				{
					focusAndSelectTextField(5);
					return false;
				}
			}
			catch (NumberFormatException localNumberFormatException)
			{
				focusAndSelectTextField(5);
				return false;
			}
			if ((str4.length() > 1) && (str4.startsWith("0")))
			{
				focusAndSelectTextField(5);
				return false;
			}
		}
		return true;
	}
}
