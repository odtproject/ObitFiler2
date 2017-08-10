package odt.of.util;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class DigitOnlyTextField
  extends JTextField
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -8435603276349000089L;

	public DigitOnlyTextField(int paramInt)
	{
		super(paramInt);
		addKeyListener(new DigitOnlyTextField.DigitOnlyTextKeyAdapter());
	}
  
  class DigitOnlyTextKeyAdapter
    extends KeyAdapter
  {
    DigitOnlyTextKeyAdapter() {}
    
    public void keyPressed(KeyEvent paramKeyEvent)
    {
      char c = paramKeyEvent.getKeyChar();
      if ((!Character.isDigit(c)) && (Character.getType(c) != Character.CONTROL)) {
        paramKeyEvent.consume();
      }
    }
    
    public void keyTyped(KeyEvent paramKeyEvent)
    {
      char c = paramKeyEvent.getKeyChar();
      if ((!Character.isDigit(c)) && (Character.getType(c) != Character.CONTROL)) {
        paramKeyEvent.consume();
      }
    }

  }
}
