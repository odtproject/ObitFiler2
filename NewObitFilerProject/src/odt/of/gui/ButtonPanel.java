package odt.of.gui;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import odt.of.main.ObituaryFiler;

public class ButtonPanel
  extends Panel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4340848646699791753L;
	static final int ADD = 0;
	static final int EDIT = 1;
	static final int DELETE = 2;
	static final int CLEAR = 3;
	static final int PREVIOUS = 4;
	static final int NEXT = 5;
	static final int DONE = 6;
	static final int BUTTONS = 7;
	static final String[] buttonName = { "Add", "Edit", "Delete", "Clear", "Previous Day", "Next Day", "Done" };
	ObituaryFiler app;
	Button[] button;
	Label dateLabel;
	public Label statusLabel;
	Choice choice;
  
	public void updateConfigChanges()
	{
		Font localFont = this.app.config.getFont();
		int i = 0;
		do
		{
			this.button[i].setFont(localFont);
			this.button[i].invalidate();
			i++;
		} while (i < BUTTONS);
		this.choice.setFont(localFont);
		this.choice.invalidate();
		if (this.app.config.isConfigured())
		{
			String str;
			if (this.choice.getItemCount() != 0) {
				str = this.choice.getSelectedItem();
			} else {
				str = this.app.config.getCurrentPaperAbbreviation();
			}
			this.choice.removeAll();
			@SuppressWarnings("rawtypes")
			Enumeration localEnumeration = this.app.config.paperAbbreviationElements();
			ArrayList<String> paperArray = new ArrayList<>();
			while (localEnumeration.hasMoreElements()) {
				paperArray.add((String)localEnumeration.nextElement());
//				this.choice.add((String)localEnumeration.nextElement());
			}
			Collections.sort(paperArray);
			Iterator<String> paperIterator = paperArray.iterator();
			while (paperIterator.hasNext()) {
				this.choice.add((String)paperIterator.next());
			}
			this.choice.select(str);
		}
		this.dateLabel.setFont(localFont);
		if (this.app.config.isConfigured()) {
			this.dateLabel.setText(this.app.config.getCurrentDateDISPLAY());
		}
		this.dateLabel.invalidate();
	}
  
	public ButtonPanel(ObituaryFiler paramObituaryFiler)
	{
		this.app = paramObituaryFiler;
		this.button = new Button[BUTTONS];
		GridBagLayout localGridBagLayout = new GridBagLayout();
		setLayout(localGridBagLayout);
		GridBagConstraints localGridBagConstraints = new GridBagConstraints();
		localGridBagConstraints.fill = 1;
		localGridBagConstraints.weightx = 1.0D;
		localGridBagConstraints.insets = new Insets(5, 2, 5, 2);
		add(this.button[0] = new Button(buttonName[ADD]));
		localGridBagLayout.setConstraints(this.button[ADD], localGridBagConstraints);
		this.button[0].addActionListener(new CommandListener(ADD, paramObituaryFiler));
		add(this.button[1] = new Button(buttonName[EDIT]));
		localGridBagLayout.setConstraints(this.button[EDIT], localGridBagConstraints);
		this.button[1].addActionListener(new CommandListener(EDIT, paramObituaryFiler));
		add(this.button[2] = new Button(buttonName[DELETE]));
		localGridBagConstraints.gridwidth = -1;
		localGridBagLayout.setConstraints(this.button[DELETE], localGridBagConstraints);
		this.button[2].addActionListener(new CommandListener(DELETE, paramObituaryFiler));
		add(this.button[3] = new Button(buttonName[CLEAR]));
		localGridBagConstraints.gridwidth = 0;
		localGridBagLayout.setConstraints(this.button[CLEAR], localGridBagConstraints);
		this.button[3].addActionListener(new CommandListener(CLEAR, paramObituaryFiler));
		add(this.button[4] = new Button(buttonName[PREVIOUS]));
		localGridBagConstraints.weightx = 0.0D;
		localGridBagConstraints.gridwidth = 2;
		localGridBagLayout.setConstraints(this.button[PREVIOUS], localGridBagConstraints);
		this.button[4].addActionListener(new CommandListener(PREVIOUS, paramObituaryFiler));
		add(this.button[5] = new Button(buttonName[NEXT]));
		localGridBagConstraints.gridwidth = 0;
		localGridBagLayout.setConstraints(this.button[NEXT], localGridBagConstraints);
		this.button[5].addActionListener(new CommandListener(NEXT, paramObituaryFiler));
		add(this.button[6] = new Button(buttonName[DONE]));
		localGridBagConstraints.gridwidth = 0;
		localGridBagLayout.setConstraints(this.button[DONE], localGridBagConstraints);
		this.button[6].addActionListener(new CommandListener(DONE, paramObituaryFiler));
		add(this.choice = new Choice());
		this.choice.addItemListener(new CommandListener(200, paramObituaryFiler));
		localGridBagConstraints.gridwidth = 0;
		localGridBagLayout.setConstraints(this.choice, localGridBagConstraints);
		if (paramObituaryFiler.config.isConfigured()) {
			this.dateLabel = new Label(paramObituaryFiler.config.getCurrentDateDISPLAY(), 1);
		} else {
			this.dateLabel = new Label("", 1);
		}
		add(this.dateLabel);
		localGridBagConstraints.gridwidth = 0;
		localGridBagLayout.setConstraints(this.dateLabel, localGridBagConstraints);
		if ((paramObituaryFiler.config.isConfigured()) && (paramObituaryFiler.hasDirtyItems())) {
			this.statusLabel = new Label("Unsent Obits", 1);
		} else {
			this.statusLabel = new Label("", 1);
		}
		add(this.statusLabel);
		localGridBagConstraints.gridwidth = 0;
		localGridBagLayout.setConstraints(this.statusLabel, localGridBagConstraints);
	}
  
	public void enableComponents(boolean paramBoolean)
	{
		int i = 0;
		do
		{
			this.button[i].setEnabled(paramBoolean);
			i++;
		} while (i <= 5);
		this.choice.setEnabled(paramBoolean);
	}
}
