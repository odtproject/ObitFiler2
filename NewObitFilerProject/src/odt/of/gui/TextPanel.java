package odt.of.gui;
import java.awt.Checkbox;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import javax.swing.JTextField;

import odt.of.main.ObituaryFiler;
import odt.of.util.AutoCityTextField;
import odt.of.util.CapitalizedTextField;
import odt.of.util.DigitOnlyTextField;
import odt.of.util.WordCapitalizedTextField;

class TextPanel
  extends Panel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7306899983613882363L;
	static final int FIELDS = 10;
	static final String[] fieldNames = { "Last Name", "First and Middle Names", "Maiden Name", "Other Last Names", "Nickname", "Age", "City of Birth", "State/Country of Birth", "City of Death", "State/Country of Death" };
	ObituaryFiler app;
	Label[] label;
	JTextField[] field;
	Checkbox aCCheckbox;
  
	public void updateConfigChanges()
	{
		Font localFont = this.app.config.getFont();
		int i = 0;
		do
		{
			this.field[i].setFont(localFont);
			this.field[i].invalidate();
			this.label[i].setFont(localFont);
			this.label[i].invalidate();
			i++;
		} while (i < FIELDS);
		
		this.aCCheckbox.setFont(localFont);
		this.aCCheckbox.invalidate();
	}
  
	public TextPanel(ObituaryFiler paramObituaryFiler)
	{
		this.app = paramObituaryFiler;
		this.label = new Label[10];
		this.field = new JTextField[10];
		this.aCCheckbox = new Checkbox();
		GridBagLayout localGridBagLayout = new GridBagLayout();
		setLayout(localGridBagLayout);
		GridBagConstraints localGridBagConstraints = new GridBagConstraints();
		localGridBagConstraints.fill = 2;
		localGridBagConstraints.insets = new Insets(0, 5, 2, 5);
		int i = 0;
		do
		{
			add(this.label[i] = new Label(fieldNames[i], 0));
			localGridBagConstraints.gridwidth = -1;
			localGridBagLayout.setConstraints(this.label[i], localGridBagConstraints);
			switch (i)
			{
			case 8: 
				add(this.field[i] = new AutoCityTextField(15, paramObituaryFiler));
				break;
			case 1: 
			case 4: 
			case 6: 
				add(this.field[i] = new WordCapitalizedTextField(15));
				break;
			case 0: 
			case 2: 
				add(this.field[i] = new CapitalizedTextField(15, false, i, paramObituaryFiler));
				break;
			case 3: 
				add(this.field[i] = new CapitalizedTextField(15, true, i, paramObituaryFiler));
				break;
			case 5: 
				add(this.field[i] = new DigitOnlyTextField(15));
				break;
			case 7: 
			case 9: 
				add(this.field[i] = new CapitalizedTextField(3, false, i, paramObituaryFiler));
			}
			localGridBagConstraints.gridwidth = 0;
			localGridBagLayout.setConstraints(this.field[i], localGridBagConstraints);
			i++;
		} while (i < FIELDS);
		Label localLabel;
		add(localLabel = new Label("", 0));
		localGridBagConstraints.gridwidth = -1;
		localGridBagLayout.setConstraints(localLabel, localGridBagConstraints);
		add(this.aCCheckbox = new Checkbox("Add Auto-City"));
		localGridBagConstraints.gridwidth = 0;
		localGridBagLayout.setConstraints(this.aCCheckbox, localGridBagConstraints);
	}
  
	public void clearAll()
	{
		int i = 0;
		do
		{
			/* Bug or something with setText so the age field won't get
			 * cleared if we don't set it to blank first.
			 */
			this.field[i].setText(" ");
			this.field[i].setText("");			
			i++;
		} while (i < FIELDS);
	}
  
	public void enableComponents(boolean paramBoolean)
	{
		int i = 0;
		do
		{
			this.field[i].setEnabled(paramBoolean);
			i++;
		} while (i < FIELDS);
		
		this.aCCheckbox.setEnabled(paramBoolean);
	}
  
	public String getText(int paramInt)
	{
		return this.field[paramInt].getText();
	}
}
