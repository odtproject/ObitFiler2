package odt.of.gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class DialogCommandListener
  implements ActionListener, ItemListener, KeyListener
{
	EditConfigDialog dialog;
	int id;
  
	public DialogCommandListener(EditConfigDialog paramEditConfigDialog, int paramInt)
	{
		this.dialog = paramEditConfigDialog;
		this.id = paramInt;
	}
  
	public void itemStateChanged(ItemEvent paramItemEvent)
	{
		String str = (String)paramItemEvent.getItem();
		switch (this.id)
		{
		case 100: 
			if ((paramItemEvent.getStateChange() == 1) && (!str.equals(this.dialog.currentItem)))
			{
				this.dialog.currentItem = str;
				if (this.dialog.currentItem.equals("General"))
				{
					this.dialog.currentPanel = this.dialog.generalConfigPanel;
					this.dialog.fillGeneral();
				}
				else
				{
					this.dialog.currentPanel = this.dialog.paperConfigPanel;
					this.dialog.fillPaper(this.dialog.configChoices.getSelectedIndex());
				}
				this.dialog.cl.show(this.dialog.cards, this.dialog.currentPanel.cardType());
				return;
			}
			break;
		}
	}
  
	public void keyTyped(KeyEvent paramKeyEvent)
	{
		if (this.dialog.currentPanel.isValidForm(false))
		{
			this.dialog.button[4].setEnabled(true);
			return;
		}
		this.dialog.button[4].setEnabled(false);
	}
  
	public void keyPressed(KeyEvent paramKeyEvent) {}
  
	public void actionPerformed(ActionEvent paramActionEvent)
	{
		switch (this.id)
		{
		case 0: 
			this.dialog.buttonNewPaper();
			return;
		case 1: 
			this.dialog.buttonRemovePaper();
			return;
		case 2: 
			this.dialog.buttonOk();
			return;
		case 3: 
			this.dialog.buttonCancel();
			return;
		case 4: 
			this.dialog.buttonApply();
			return;
		case 10: 
			this.dialog.buttonAddAutoCity();
			return;
		case 11: 
			this.dialog.buttonDeleteAutoCity();
			return;
		}
	}
  
	public void keyReleased(KeyEvent paramKeyEvent) {}
}
