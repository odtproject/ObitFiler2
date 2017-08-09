package odt.of.gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import odt.of.main.ObituaryFiler;
import odt.of.util.ErrorLog;

class CommandListener
  implements ActionListener, ItemListener, WindowListener
{
  static final int BUTTON_ADD = 0;
  static final int BUTTON_EDIT = 1;
  static final int BUTTON_DELETE = 2;
  static final int BUTTON_CLEAR = 3;
  static final int BUTTON_PREV = 4;
  static final int BUTTON_NEXT = 5;
  static final int BUTTON_DONE = 6;
  static final int MENU_FILE_EXIT = 90;
  static final int MENU_ACTION_EXPORT = 100;
  static final int MENU_ACTION_SET_DATE = 101;
  static final int MENU_ACTION_CONFIGURE = 102;
  static final int MENU_HELP_CONTENTS = 110;
  static final int MENU_HELP_ABBREV = 111;
  static final int MENU_HELP_ABOUT = 112;
  static final int CHOICE_PAPER = 200;
  static final int PREV_LIST = 300;
  static final int TODAY_LIST = 301;
  int id;
  ObituaryFiler app;
  
  public CommandListener(int paramInt, ObituaryFiler paramObituaryFiler)
  {
    this.id = paramInt;
    this.app = paramObituaryFiler;
  }
  
  public void itemStateChanged(ItemEvent paramItemEvent)
  {
	  String str;
	  
    switch (this.id)
    {
    case CHOICE_PAPER: 
      if (paramItemEvent.getStateChange() == 1)
      {
        this.app.choicePaperChange((String)paramItemEvent.getItem());
        return;
      }
      str = "Paper selection not handled " + paramItemEvent.paramString();
      new ErrorLog(null, str);
      return;
    case PREV_LIST: 
      if (paramItemEvent.getStateChange() == 1)
      {
        this.app.selectedPreviousItem((Integer)paramItemEvent.getItem());
        return;
      }
      str = "Prev List event not handled " + paramItemEvent.paramString();
      new ErrorLog(null, str);
      return;
    case TODAY_LIST: 
      if (paramItemEvent.getStateChange() == 1)
      {
        this.app.selectedTodayItem((Integer)paramItemEvent.getItem());
        return;
      }
      str = "Today List event not handled " + paramItemEvent.paramString();
      new ErrorLog(null, str);
      return;
    }
    str = "itemStateChanged hit default " + paramItemEvent.paramString();
    new ErrorLog(null, str);
  }
  
  public void windowDeactivated(WindowEvent paramWindowEvent) {}
  
  public void windowClosing(WindowEvent paramWindowEvent)
  {
    this.app.menuFileExit();
  }
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    switch (this.id)
    {
    case BUTTON_ADD: 
      this.app.buttonAdd();
      return;
    case BUTTON_EDIT: 
      this.app.buttonEdit();
      return;
    case BUTTON_DELETE: 
      this.app.buttonDelete();
      return;
    case BUTTON_CLEAR: 
      this.app.buttonClear();
      return;
    case BUTTON_PREV: 
      this.app.buttonPrev();
      return;
    case BUTTON_NEXT: 
      this.app.buttonNext();
      return;
    case BUTTON_DONE: 
      this.app.menuFileExit();
      return;
    case MENU_FILE_EXIT: 
      this.app.menuFileExit();
      return;
    case MENU_ACTION_EXPORT: 
      this.app.menuActionExport();
      return;
    case MENU_ACTION_SET_DATE: 
      this.app.menuActionSetDate();
      return;
    case MENU_ACTION_CONFIGURE: 
      this.app.MenuActionConfigure();
      return;
    case MENU_HELP_CONTENTS: 
      this.app.menuHelpContents();
      return;
    case MENU_HELP_ABBREV: 
      this.app.menuHelpAbbreviations();
      return;
    case MENU_HELP_ABOUT: 
      this.app.menuHelpAbout();
      return;
    case PREV_LIST: 
      this.app.doubleClickItem(paramActionEvent.getActionCommand());
      return;
    default: 
      new ErrorLog(null, "actionPerformed hit default");
      break;
    }
  }
  
  public void windowOpened(WindowEvent paramWindowEvent) {}
  
  public void windowClosed(WindowEvent paramWindowEvent) {}
  
  public void windowDeiconified(WindowEvent paramWindowEvent) {}
  
  public void windowActivated(WindowEvent paramWindowEvent) {}
  
  public void windowIconified(WindowEvent paramWindowEvent) {}
}
