package odt.of.util;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DigitOnlyTextField extends TextField
{
	private static final long serialVersionUID = -8435603276349000089L;

	public DigitOnlyTextField(int paramInt)
	{
		super(paramInt);
		addKeyListener(new DigitOnlyTextField.DigitOnlyTextKeyAdapter());
	}
	
	class DigitOnlyTextKeyAdapter extends KeyAdapter
    {
		/* use to keep track of whether to consume the key */
		private boolean consumeKey;
		
		DigitOnlyTextKeyAdapter() {}
		
		public void keyPressed(KeyEvent paramKeyEvent)
		{
			setConsumeKey(false);
			char c = paramKeyEvent.getKeyChar();
			
			/* if this is not a digit or a control character, then just consume it */
			if ((!Character.isDigit(c)) && (Character.getType(c) != Character.CONTROL)) 
			{
				setConsumeKey(true);
				paramKeyEvent.consume();
			}
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
  		
  		public boolean isConsumeKey() 
  		{
  			return consumeKey;
  		}
  		
  		public void setConsumeKey(boolean consumeKey) 
  		{
  			this.consumeKey = consumeKey;
  		}
  		
  	} /* end of DigitOnlyTextKeyAdapter class */
	
} /* end of DigitOnlyTextField class */
