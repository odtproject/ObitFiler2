package odt.of.gui;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Enumeration;
import java.util.Vector;

import odt.of.main.ObituaryFiler;

public class EditConfigDialog
  extends Dialog
  implements WindowListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 3242567245912046395L;
	static final int NEW_PAPER = 0;
	static final int REMOVE_PAPER = 1;
	static final int OK = 2;
	static final int CANCEL = 3;
	static final int APPLY = 4;
	static final int BUTTONS = 5;
	static final int ADD_AUTO_CITY = 10;
	static final int DELETE_AUTO_CITY = 11;
	static final int CONFIG_CHOICES = 100;
	static final int FONTNAME_CHOICES = 200;
	String[] buttonNames = { "New Paper", "Remove Paper", "Ok", "Cancel", "Apply" };
	Button[] button = new Button[5];
	Choice configChoices;
	CardLayout cl;
	Panel cards;
	String currentItem;
	ObituaryFiler app;
	GeneralConfigPanel generalConfigPanel;
	PaperConfigPanel paperConfigPanel;
	ConfigPanel currentPanel;
	Frame mainFrame;
  
	void buttonNewPaper()
	{
		this.cl.show(this.cards, "Paper");
		this.currentPanel = this.paperConfigPanel;
		this.configChoices.add("");
		this.configChoices.select("");
		fillPaper(0);
	}
  
	void buttonDeleteAutoCity()
	{
		String str = this.paperConfigPanel.acField.getText();
		if (str.compareTo("") == 0) {
			return;
		}
		try
		{
			this.paperConfigPanel.editAutoCities.remove(str);
			this.paperConfigPanel.acField.setText("");
		}
		catch (IllegalArgumentException localIllegalArgumentException)
		{
			Toolkit.getDefaultToolkit().beep();
		}
		this.paperConfigPanel.acField.requestFocus();
	}
  
	void fillGeneral()
	{
		String str = this.app.config.getTagname();
		int i = this.app.config.getFontNameIndex();
		Integer localInteger1 = new Integer(this.app.config.getFontSize());
		Integer localInteger2 = new Integer(this.app.config.getListEntries());
		if (str == null)
		{
			str = "";
			this.configChoices.setEnabled(false);
			this.button[0].setEnabled(false);
			this.button[4].setEnabled(false);
		}
		else
		{
			this.configChoices.setEnabled(true);
			this.button[0].setEnabled(true);
		}
		this.button[1].setEnabled(false);
		this.generalConfigPanel.setField(0, str);
		this.generalConfigPanel.fontNameChoice.select(i);
		this.generalConfigPanel.setField(1, localInteger1.toString());
		this.generalConfigPanel.setField(2, localInteger2.toString());
		this.currentItem = "General";
		packAndMove(this.mainFrame);
	}
  
	public EditConfigDialog(ObituaryFiler paramObituaryFiler, Frame paramFrame, String paramString)
	{
		super(paramFrame, paramString, true);
		this.app = paramObituaryFiler;
		this.mainFrame = paramFrame;
		this.cards = new Panel();
		this.cl = new CardLayout();
		setLayout(new BorderLayout());
		Panel localPanel1 = new Panel();
		this.configChoices = new Choice();
		this.configChoices.setFont(paramObituaryFiler.config.getFont());
		localPanel1.add(this.configChoices);
		this.configChoices.addItemListener(new DialogCommandListener(this, 100));
		Panel localPanel2 = new Panel();
		localPanel2.setLayout(new GridLayout(1, 2, 2, 2));
		int i = 0;
		do
		{
			this.button[i] = new Button(this.buttonNames[i]);
			this.button[i].addActionListener(new DialogCommandListener(this, i));
			localPanel2.add(this.button[i]);
			i++;
		} while (i <= 1);
		
		localPanel1.add(localPanel2);
		Panel localPanel3 = new Panel();
		Panel localPanel4 = new Panel();
		localPanel4.setLayout(new GridLayout(1, 2, 2, 2));
		
		int j = 2;
		do
		{
			this.button[j] = new Button(this.buttonNames[j]);
			this.button[j].addActionListener(new DialogCommandListener(this, j));
			localPanel4.add(this.button[j]);
			j++;
		} while (j <= 4);
		
		localPanel3.add(localPanel4);
		add("North", localPanel1);
		add("South", localPanel3);
		this.cards.setLayout(this.cl);
		this.cards.add("General", this.generalConfigPanel = new GeneralConfigPanel(this));
		this.cards.add("Paper", this.paperConfigPanel = new PaperConfigPanel(this));
		add("Center", this.cards);
		addWindowListener(this);
	}
  
	public void windowDeactivated(WindowEvent paramWindowEvent) {}
  
	void buttonApply()
	{
		if (this.currentPanel == this.generalConfigPanel)
		{
			saveGeneral();
			fillGeneral();
			return;
		}
		String str1 = savePaper();
		if (this.currentItem.equals(""))
		{
			int i = 1;
			for (int j = 1; j < this.configChoices.getItemCount(); j++)
			{
				String str2 = this.configChoices.getItem(j);
				if (str2.equals(str1))
				{
					i = 0;
					break;
				}
			}
			this.configChoices.remove(this.configChoices.getSelectedIndex());
			if (i != 0)
			{
				this.configChoices.add(str1);
				this.configChoices.invalidate();
			}
			this.configChoices.select(str1);
		}
		fillPaper(this.configChoices.getSelectedIndex());
	}
  
	public void windowClosing(WindowEvent paramWindowEvent) {}
  
	void packAndMove(Frame paramFrame)
	{
		pack();
		Point localPoint = paramFrame.getLocation();
		Dimension localDimension1 = paramFrame.getSize();
		Dimension localDimension2 = getSize();
		localPoint.translate((localDimension1.width - localDimension2.width) / 2, (localDimension1.height - localDimension2.height) / 2);
		setLocation(localPoint);
	}
  
	String savePaper()
	{
		Vector<String> localVector = this.paperConfigPanel.getTextFields();
		String abbrev = localVector.elementAt(PaperConfigPanel.ABBREVIATION);
		String location = localVector.elementAt(PaperConfigPanel.LOCATION);
		int daysPrevious = new Integer(localVector.elementAt(PaperConfigPanel.DAYS_PREVIOUS)).intValue();
		int frequency = this.paperConfigPanel.frequencyChoice.getSelectedIndex();
		this.app.config.addPaper(abbrev, location, frequency, daysPrevious, this.paperConfigPanel.editAutoCities.getItems());
		return abbrev;
	}
  
	void updateFonts()
	{
		Font localFont = this.app.config.getFont();
		int i = 0;
		do
		{
			this.button[i].setFont(localFont);
			this.button[i].invalidate();
			i++;
		} while (i < 5);
		
		this.configChoices.setFont(localFont);
		this.configChoices.invalidate();
		this.generalConfigPanel.updateFonts(localFont);
		this.paperConfigPanel.updateFonts(localFont);
	}
  
	void buttonOk()
	{
		if (this.currentPanel.isValidForm(true))
		{
			if (this.currentPanel == this.generalConfigPanel) {
				saveGeneral();
			} else {
				savePaper();
			}
			setVisible(false);
		}
	}
  
	public void windowOpened(WindowEvent paramWindowEvent) {}
  
	public void windowClosed(WindowEvent paramWindowEvent) {}
  
	public void windowDeiconified(WindowEvent paramWindowEvent) {}
  
	public void windowActivated(WindowEvent paramWindowEvent)
	{
		this.generalConfigPanel.fields[0].requestFocus();
	}
  
	void buttonCancel()
	{
		setVisible(false);
	}
  
	void fillPaper(int paramInt)
	{
		if (paramInt == 0)
		{
			this.paperConfigPanel.clearAll();
			this.paperConfigPanel.fields[0].setEnabled(true);
			this.paperConfigPanel.setField(2, "3");
			this.button[0].setEnabled(false);
			this.button[1].setEnabled(false);
			this.currentItem = "";
		}
		else
		{
			this.paperConfigPanel.setField(0, this.app.config.getPaperAbbreviationAt(paramInt - 1));
			this.paperConfigPanel.setField(1, this.app.config.getPaperLocationAt(paramInt - 1));
			Integer localInteger = new Integer(this.app.config.getDaysPreviousAt(paramInt - 1));
			this.paperConfigPanel.setField(2, localInteger.toString());
			this.paperConfigPanel.frequencyChoice.select(this.app.config.getFrequencyAt(paramInt - 1));
			this.paperConfigPanel.fields[0].setEnabled(false);
			this.paperConfigPanel.editAutoCities.removeAll();
			for (int i = 0; i < this.app.config.getNumberOfAutoCitiesAt(paramInt - 1); i++) {
				this.paperConfigPanel.editAutoCities.add(this.app.config.getAutoCityAtAt(paramInt - 1, i));
			}
			this.button[0].setEnabled(true);
			this.button[1].setEnabled(true);
			this.currentItem = this.app.config.getPaperAbbreviationAt(paramInt - 1);
		}
		packAndMove(this.mainFrame);
	}
  
	void buttonRemovePaper()
	{
		this.app.config.removePaper(this.configChoices.getSelectedItem());
		this.configChoices.remove(this.configChoices.getSelectedItem());
		this.currentPanel = this.generalConfigPanel;
		addChoices();
		fillGeneral();
		this.cl.show(this.cards, "General");
	}
  
	void addChoices()
	{
		this.configChoices.removeAll();
		this.configChoices.add("General");
		if (this.app.config.isConfigured())
		{
			@SuppressWarnings("rawtypes")
			Enumeration localEnumeration = this.app.config.paperAbbreviationElements();
			while (localEnumeration.hasMoreElements()) {
				this.configChoices.add((String)localEnumeration.nextElement());
			}
		}
		this.configChoices.select(0);
		this.configChoices.invalidate();
	}
  
	void buttonAddAutoCity()
	{
		int i = 0;
		List localList = this.paperConfigPanel.editAutoCities;
		String str1 = this.paperConfigPanel.acField.getText();
		if (str1.compareTo("") == 0) {
			return;
		}
		for (int j = 0; j < localList.getItemCount(); j++)
		{
			String str2 = localList.getItem(j);
			if (str2.compareTo(str1) == 0) {
				i = 1;
			}
		}
		if (i == 0)
		{
			localList.add(str1);
			localList.makeVisible(localList.getItemCount() - 1);
			this.paperConfigPanel.acField.setText("");
		}
		else
		{
			Toolkit.getDefaultToolkit().beep();
		}
		this.paperConfigPanel.acField.requestFocus();
	}
  
	public void windowIconified(WindowEvent paramWindowEvent) {}
  
	void saveGeneral()
	{
		Vector<String> localVector = this.generalConfigPanel.getTextFields();
		String str = localVector.elementAt(0);
		this.app.config.setTagname(str);
		int i = this.app.config.getFontNameIndex();
    	int j = this.generalConfigPanel.fontNameChoice.getSelectedIndex();
    	this.app.config.setFontNameIndex(j);
    	int k = this.app.config.getFontSize();
    	int m = new Integer(localVector.elementAt(1)).intValue();
    	this.app.config.setFontSize(m);
    	int n = new Integer(localVector.elementAt(2)).intValue();
    	this.app.config.setListEntries(n);
    	if ((i != j) || (k != m)) {
    		updateFonts();
    	}
	}
  
	void showIt()
	{
		this.cl.show(this.cards, "General");
		this.currentPanel = this.generalConfigPanel;
		addChoices();
		fillGeneral();
		updateFonts();
		packAndMove(this.mainFrame);
		setVisible(true);
	}
}
