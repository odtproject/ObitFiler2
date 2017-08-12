package odt.of.util;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import odt.of.main.ObituaryFiler;

public class CapitalizedTextField
  extends TextField
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 6222402268791672261L;
	
	boolean allowQuotes;
  
  public CapitalizedTextField(int paramInt1, boolean paramBoolean, int paramInt2, ObituaryFiler paramObituaryFiler)
  {
    super(paramInt1);
    this.allowQuotes = paramBoolean;
    addKeyListener(new CapitalizedTextField.CapitalizedTextKeyAdapter(paramObituaryFiler, paramInt2));
  }
  
  class CapitalizedTextKeyAdapter
    extends KeyAdapter
  {
    ObituaryFiler app;
    int id;
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
      if ((i < 65) || (i > 90)) {
    	  return;
      }
      char c2;
      if (paramKeyEvent.isControlDown())
      {
        if (paramKeyEvent.isShiftDown()) {
          c2 = (char)(i - 65 + 65);
        } else {
          c2 = (char)(i - 65 + 97);
        }
      }
      else {
        c2 = Character.toUpperCase(c1);
      }
      int j = CapitalizedTextField.this.getSelectionStart();
      int k = CapitalizedTextField.this.getSelectionEnd();
      String str1 = CapitalizedTextField.this.getText();
      Object localObject;
      
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
      localObject = str1.substring(0, j);

      String str2 = str1.substring(k, str1.length());
      String str3 = (String)localObject + c2 + str2;
      CapitalizedTextField.this.setText("");
      CapitalizedTextField.this.setText(str3);
//      CapitalizedTextField.this.setText((String)localObject + c2 + str2);
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
