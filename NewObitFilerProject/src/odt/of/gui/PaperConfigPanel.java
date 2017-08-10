package odt.of.gui;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JTextField;

import odt.of.util.CapitalizedTextField;
import odt.of.util.DigitOnlyTextField;
import odt.of.util.WordCapitalizedTextField;

class PaperConfigPanel
  extends ConfigPanel
  implements ActionListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -2849900331981378584L;
	static final int ABBREVIATION = 0;
	static final int LOCATION = 1;
	static final int DAYS_PREVIOUS = 2;
	static final int FREQUENCY = 3;
	static final int TEXT_FIELDS = 3;
	static final String[] fieldNames = { "Paper Abbreviation", "Paper Location", "Days in Previous List", "Paper Frequency" };
	JTextField[] fields;
	Choice frequencyChoice;
	List editAutoCities;
	EditConfigDialog dialog;
	JTextField acField;
  
	public boolean isValidForm(boolean paramBoolean)
	{
		int i = 0;
		do
		{
			String str = getText(i).trim();
			if (str.equals(""))
			{
				if (paramBoolean)
				{
					this.fields[i].selectAll();
					this.fields[i].requestFocus();
					Toolkit.getDefaultToolkit().beep();
				}
				return false;
			}
			i++;
		} while (i < 3);
		return true;
	}
  
	public PaperConfigPanel(EditConfigDialog paramEditConfigDialog)
	{
		GridBagLayout localGridBagLayout = new GridBagLayout();
		GridBagConstraints localGridBagConstraints = new GridBagConstraints();
		this.dialog = paramEditConfigDialog;
		this.fields = new JTextField[fieldNames.length];
		setLayout(localGridBagLayout);
		localGridBagConstraints.fill = 2;
		localGridBagConstraints.insets = new Insets(0, 5, 5, 5);
		Label localLabel;
		add(localLabel = new Label(fieldNames[0], 0));
		localGridBagConstraints.gridwidth = -1;
		localGridBagLayout.setConstraints(localLabel, localGridBagConstraints);
		add(this.fields[0] = new JTextField("", 20));
		this.fields[0].addKeyListener(new DialogCommandListener(paramEditConfigDialog, 0));
		localGridBagConstraints.gridwidth = 0;
		localGridBagLayout.setConstraints(this.fields[0], localGridBagConstraints);
		add(localLabel = new Label(fieldNames[1], 0));
		localGridBagConstraints.gridwidth = -1;
		localGridBagLayout.setConstraints(localLabel, localGridBagConstraints);
		add(this.fields[1] = new CapitalizedTextField(20, false, -1, null));
		this.fields[1].addKeyListener(new DialogCommandListener(paramEditConfigDialog, 1));
		localGridBagConstraints.gridwidth = 0;
		localGridBagLayout.setConstraints(this.fields[1], localGridBagConstraints);
		add(localLabel = new Label(fieldNames[2], 0));
		localGridBagConstraints.gridwidth = -1;
		localGridBagLayout.setConstraints(localLabel, localGridBagConstraints);
		add(this.fields[2] = new DigitOnlyTextField(20));
		this.fields[2].addKeyListener(new DialogCommandListener(paramEditConfigDialog, 2));
		localGridBagConstraints.gridwidth = 0;
		localGridBagLayout.setConstraints(this.fields[2], localGridBagConstraints);
		add(localLabel = new Label(fieldNames[3], 0));
		localGridBagConstraints.gridwidth = -1;
		localGridBagLayout.setConstraints(localLabel, localGridBagConstraints);
		this.frequencyChoice = new Choice();
		this.frequencyChoice.add("Daily");
		this.frequencyChoice.add("Weekly");
		this.frequencyChoice.add("Monthly");
		add(this.frequencyChoice);
		localGridBagConstraints.gridwidth = 0;
		localGridBagLayout.setConstraints(this.frequencyChoice, localGridBagConstraints);
		Panel localPanel1 = new Panel(new BorderLayout());
		localPanel1.add("North", localLabel = new Label("Auto-Cities", 1));
		localPanel1.add("Center", this.acField = new WordCapitalizedTextField(20));
		Panel localPanel2 = new Panel();
		Button localButton1;
		localPanel2.add(localButton1 = new Button("Add"));
		localButton1.addActionListener(new DialogCommandListener(paramEditConfigDialog, 10));
		Button localButton2;
		localPanel2.add(localButton2 = new Button("Delete"));
		localButton2.addActionListener(new DialogCommandListener(paramEditConfigDialog, 11));
		localPanel1.add("South", localPanel2);
		localGridBagConstraints.gridwidth = -1;
		localGridBagLayout.setConstraints(localPanel1, localGridBagConstraints);
		add(localPanel1);
		this.editAutoCities = new List(6, false);
		this.editAutoCities.addActionListener(this);
		Panel localPanel3 = new Panel();
		localPanel3.add(this.editAutoCities);
		localGridBagConstraints.gridwidth = 0;
		localGridBagLayout.setConstraints(localPanel3, localGridBagConstraints);
		add(localPanel3);
	}
  
	public void updateFonts(Font paramFont)
	{
		int i = 0;
		do
		{
			this.fields[i].setFont(paramFont);
			this.fields[i].invalidate();
			i++;
		} while (i < 3);
		this.frequencyChoice.setFont(paramFont);
		this.frequencyChoice.invalidate();
	}
  
	public void actionPerformed(ActionEvent paramActionEvent)
	{
		this.acField.setText(paramActionEvent.getActionCommand());
		this.acField.requestFocus();
	}
  
	public void setField(int paramInt, String paramString)
	{
		if (!paramString.equals(getText(paramInt))) {
			this.fields[paramInt].setText(paramString);
		}
	}
  
	public void clearAll()
	{
		int i = 0;
		do
		{
			this.fields[i].setText("");
			i++;
		} while (i < 3);
		
		this.frequencyChoice.select(0);
		this.editAutoCities.removeAll();
	}
  
	public void selectAll()
	{
		int i = 0;
		do
		{
			this.fields[i].selectAll();
			i++;
		} while (i < 3);
	}
  
	public Dimension getPreferredSize()
	{
		Dimension localDimension = super.getPreferredSize();
		localDimension.height += 30;
		return localDimension;
	}
  
	public String cardType()
	{
		return "Paper";
	}
  
	public Vector<String> getTextFields()
	{
		Vector<String> localVector = new Vector<String>(PaperConfigPanel.TEXT_FIELDS);
		int i = 0;
		do
		{
			localVector.addElement(this.fields[i].getText().trim());
			i++;
		} while (i < PaperConfigPanel.TEXT_FIELDS);
		
		return localVector;
	}
  
	public String getText(int paramInt)
	{
		return this.fields[paramInt].getText();
	}
}
