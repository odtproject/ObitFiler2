package odt.of.gui;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;

import odt.of.main.ObituaryFiler;

public class ObitListPanel
  extends Panel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8124360828001972504L;
	ObituaryFiler app;
	public List obitList;
	Label lab;
	String name;
  
	public void removeAll()
	{
		this.obitList.removeAll();
		setLabel();
	}
  
	public void updateConfigChanges()
	{
		Font localFont = this.app.config.getFont();
		this.lab.setFont(localFont);
		this.lab.invalidate();
		this.obitList.setFont(localFont);
		this.obitList.invalidate();
	}
  
	ObitListPanel(ObituaryFiler paramObituaryFiler, int paramInt)
	{
		this.app = paramObituaryFiler;
		if (paramInt == 300) {
			this.name = "Previous Obits - ";
		} else {
			this.name = "Today's Obits - ";
		}
		GridBagLayout localGridBagLayout = new GridBagLayout();
		setLayout(localGridBagLayout);
		GridBagConstraints localGridBagConstraints = new GridBagConstraints();
		localGridBagConstraints.insets = new Insets(0, 5, 0, 5);
		this.lab = new Label(this.name + 0, 1);
		localGridBagConstraints.gridwidth = 0;
		localGridBagLayout.setConstraints(this.lab, localGridBagConstraints);
		add(this.lab);
		
		this.obitList = new List(paramObituaryFiler.config.getListEntries(), false);
		this.obitList.addItemListener(new CommandListener(paramInt, paramObituaryFiler));
		this.obitList.addActionListener(new CommandListener(paramInt, paramObituaryFiler));
		localGridBagConstraints.fill = 1;
		localGridBagConstraints.weightx = 1.0D;
		localGridBagLayout.setConstraints(this.obitList, localGridBagConstraints);
		add(this.obitList);
	}
  
	public void add(String paramString)
	{
		this.obitList.add(paramString);
		setLabel();
	}
  
	public void setLabel()
	{
		this.lab.setText(this.name + this.obitList.getItemCount());
	}
  
	public void remove(int paramInt)
	{
		this.obitList.remove(paramInt);
		setLabel();
	}
}
