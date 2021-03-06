package odt.of.util;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class WordCapitalizedTextField
  extends TextField
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6460029389212698669L;

	public WordCapitalizedTextField(int paramInt)
	{
		super(paramInt);
		addKeyListener(new WordCapitalizedTextField.WordCapitalizedTextKeyAdapter());
	}
  
	class WordCapitalizedTextKeyAdapter
    	extends KeyAdapter
    {
		private boolean consumeKey;
		
		WordCapitalizedTextKeyAdapter() {}
		
		public void keyTyped(KeyEvent paramKeyEvent)
		{
			if ( isConsumeKey() )
			{
				paramKeyEvent.consume();
			}
		}
    
		public void keyPressed(KeyEvent paramKeyEvent)
		{
			setConsumeKey(false);
			
			String str1 = WordCapitalizedTextField.this.getText();
			int i = WordCapitalizedTextField.this.getSelectionStart();
			int j = paramKeyEvent.getKeyCode();
			
			/* If not a letter key, then do nothing */
			if ((j < 65) || (j > 90))
			{
				return;
			}
			
			char c1;

			/* If the control key is down, then the user wants it to be lowercase */
			if (paramKeyEvent.isControlDown() || paramKeyEvent.isMetaDown()) 
			{
				/* If the shift key is down, then they do want it uppercase */
				if (paramKeyEvent.isShiftDown()) {
					c1 = (char)(j - 65 + 65);
				} else {
					c1 = (char)(j - 65 + 97);					
				}
			}
			else 
			{
				/* If it is the first character in the word, it should be capitalized.
				 * Otherwise, leave it alone.
				 */
				char c2 = paramKeyEvent.getKeyChar();
				if ((i == 0) || (str1.charAt(i - 1) == ' ') || (str1.charAt(i - 1) == '>')) {
					c1 = Character.toUpperCase(c2);
				} else {
					c1 = c2;
				}
			}
			
			int k = WordCapitalizedTextField.this.getSelectionEnd();
			String str2 = str1.substring(0, i);
			String str3 = str1.substring(k, str1.length());
			WordCapitalizedTextField.this.setText(str2 + c1 + str3);
			WordCapitalizedTextField.this.setCaretPosition(i + 1);
			
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
