package odt.of.gui;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;

import odt.of.main.ObituaryFiler;

class ObitMenuBar
  extends MenuBar
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 3939955154495874696L;
	static final int FILE = 0;
	static final int ACTION = 1;
	static final int HELP = 2;
	static final int MENUS = 3;
	MenuItem[] fileMenuItem = { new MenuItem("Exit") };
	MenuItem[] actionMenuItem = { new MenuItem("Export..."), new MenuItem("Set Date..."), new MenuItem("Configure...") };
	MenuItem[] helpMenuItem = { new MenuItem("Online Help"), new MenuItem("Abbreviations"), new MenuItem("About...") };
	Menu[] menu = { new Menu("File"), new Menu("Action"), new Menu("Help") };
	int[] fileListenerId = { 90 };
	int[] actionListenerId = { 100, 101, 102 };
	int[] helpListenerId = { 110, 111, 112 };
  
	public ObitMenuBar(ObituaryFiler paramObituaryFiler)
	{
		int i;
		for (i = 0; i < this.fileMenuItem.length; i++)
		{
			this.menu[0].add(this.fileMenuItem[i]);
			this.fileMenuItem[i].addActionListener(new CommandListener(this.fileListenerId[i], paramObituaryFiler));
		}
		
		for (i = 0; i < this.actionMenuItem.length; i++)
		{
			this.menu[1].add(this.actionMenuItem[i]);
			this.actionMenuItem[i].addActionListener(new CommandListener(this.actionListenerId[i], paramObituaryFiler));
			if (i == 0) {
				this.menu[1].add(new MenuItem("-"));
			}
		}
		
		for (i = 0; i < this.helpMenuItem.length; i++)
		{
			this.menu[2].add(this.helpMenuItem[i]);
			this.helpMenuItem[i].addActionListener(new CommandListener(this.helpListenerId[i], paramObituaryFiler));
			if (i == 1) {
				this.menu[2].add(new MenuItem("-"));
			}
		}
		setHelpMenu(this.menu[HELP]);
		add(this.menu[FILE]);
		add(this.menu[ACTION]);
		add(this.menu[HELP]);
	}
  
	public void enableComponents(boolean paramBoolean)
	{
		this.actionMenuItem[0].setEnabled(paramBoolean);
		this.actionMenuItem[1].setEnabled(paramBoolean);
	}
}
