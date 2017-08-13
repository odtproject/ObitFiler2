package odt.of.util;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import odt.of.main.ObituaryFiler;

public class CapitalizedTextField extends TextField
{
	private static final long serialVersionUID = 6222402268791672261L;
	
	boolean allowQuotes;
  
  public CapitalizedTextField(int paramInt1, boolean paramBoolean, int paramInt2, ObituaryFiler paramObituaryFiler)
  {
    super(paramInt1);
    this.allowQuotes = paramBoolean;
    addKeyListener(new CapitalizedTextField.CapitalizedTextKeyAdapter(paramObituaryFiler, paramInt2));
  }
  
  class CapitalizedTextKeyAdapter extends KeyAdapter
  {
    ObituaryFiler app;
    int id;
    
    /* keep track of whether to consume the key entered */
	private boolean consumeKey;
    
    public CapitalizedTextKeyAdapter(ObituaryFiler paramObituaryFiler, int paramInt)
    {
      this.app = paramObituaryFiler;
      this.id = paramInt;
    }
    
	public void keyTyped(KeyEvent paramKeyEvent)
	{
		if ( isConsumeKey() )
		{
			paramKeyEvent.consume();
		}
	}
 
	public void keyReleased(KeyEvent paramKeyEvent)
	{
		if ( isConsumeKey() )
		{
			paramKeyEvent.consume();
		}
	}

    public void keyPressed(KeyEvent paramKeyEvent)
    {
		setConsumeKey(false);

      char c1 = paramKeyEvent.getKeyChar();
      if ((c1 == ',') || (c1 == ';') || (c1 == '.') || ((c1 == '"') && (!CapitalizedTextField.this.allowQuotes)))
      {
    	  setConsumeKey(true);
    	  paramKeyEvent.consume();
    	  return;
      }
      int i = paramKeyEvent.getKeyCode();
      
      /* if it's not a letter key, do nothing */
      if ((i < 65) || (i > 90)) {
    	  return;
      }
      char c2;
      
      /* if the control or command key are down, the user wants it lowercase */
      if (paramKeyEvent.isControlDown() || paramKeyEvent.isMetaDown())
      {
    	  /* If shift is down, it should be uppercase */
        if (paramKeyEvent.isShiftDown()) 
        {
          c2 = (char)(i - 65 + 65);
        }
        else 
        {
          c2 = (char)(i - 65 + 97);
        }
      }
      else 
      {
        c2 = Character.toUpperCase(c1);
      }
      
      /* Get the beginning and end of the current string in this
       * field.
       */
      int j = CapitalizedTextField.this.getSelectionStart();
      int k = CapitalizedTextField.this.getSelectionEnd();
      String str1 = CapitalizedTextField.this.getText();
      Object localObject;
      
      /* If this is the Last Name field, display the closest match 
       * to this entry in the list of previous obits.
       */
      if (this.id == 0)
      {
        localObject = ObituaryFiler.gui.previousPanel.obitList;
        for (int m = 0; m < ((List)localObject).getItemCount(); m++)
        {
          String str3 = str1.substring(0, j) + c2;
          String str4 = ((List)localObject).getItem(m);
          if (str3.compareTo(str4.substring(0, str3.length())) == 0)
          {
            ((List)localObject).makeVisible(m);
            break;
          }
        }
      }
      
      /* The following code seems to allow the user to fix something
       * in the current string, so we have marked the selection
       * positions and also have the whole string.  This requires
       * some reassembly of the string to include the new character
       * that we are processing.
       */
      localObject = str1.substring(0, j);

      String str2 = str1.substring(k, str1.length());
      String str3 = (String)localObject + c2 + str2;
      CapitalizedTextField.this.setText("");
      CapitalizedTextField.this.setText(str3);
      CapitalizedTextField.this.setCaretPosition(j + 1);
      
		setConsumeKey(true);
		paramKeyEvent.consume();
    }

	public boolean isConsumeKey() {
		return consumeKey;
	}

	public void setConsumeKey(boolean consumeKey) {
		this.consumeKey = consumeKey;
	}
  }
}
