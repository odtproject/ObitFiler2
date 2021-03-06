package odt.of.util;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import odt.of.main.ObituaryFiler;

public class AutoCityTextField
  extends TextField
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 2532319213667664654L;

	public AutoCityTextField(int paramInt, ObituaryFiler paramObituaryFiler)
	{
		super(paramInt);
		addKeyListener(new AutoCityTextField.AutoCityWordCapitalizedTextKeyAdapter(paramObituaryFiler));
	}
  
	class AutoCityWordCapitalizedTextKeyAdapter
    	extends KeyAdapter
    {
		ObituaryFiler app;
		private boolean consumeKey;
    
		public AutoCityWordCapitalizedTextKeyAdapter(ObituaryFiler paramObituaryFiler)
		{
			this.app = paramObituaryFiler;
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
			String str1 = AutoCityTextField.this.getText();
			int i = AutoCityTextField.this.getSelectionStart();
			int j = paramKeyEvent.getKeyCode();
			
			/* If not a letter, do nothing */
			if ((j < 65) || (j > 90)) {
				return;
			}
			char c1;
			
			/* If the control or command key is down, the user wants it
			 * to be lowercase.
			 */
			if (paramKeyEvent.isControlDown() || paramKeyEvent.isMetaDown())
			{
				if (paramKeyEvent.isShiftDown()) {
					c1 = (char)(j - 65 + 65);
				} else {
					c1 = (char)(j - 65 + 97);
				}
			}
			else
			{
				char c2 = paramKeyEvent.getKeyChar();
				if ((i == 0) || (str1.charAt(i - 1) == ' ') || (str1.charAt(i - 1) == '>')) {
					c1 = Character.toUpperCase(c2);
				} else {
					c1 = c2;
				}
			}
			String str2 = str1.substring(0, i) + c1;
			Object localObject = "";
			Vector<String> localVector = this.app.config.getCurrentPaper().autoCities;
			String str3;
			int k;
			for (k = 0; k < localVector.size(); k++)
			{
				str3 = localVector.elementAt(k);
				if ((str2.length() < str3.length()) && (str2.compareTo(str3.substring(0, str2.length())) == 0))
				{
					localObject = str3;
					break;
				}
			}
			if (((String)localObject).compareTo("") != 0)
			{
				AutoCityTextField.this.setText((String)localObject);
				AutoCityTextField.this.select(str2.length(), ((String)localObject).length());
			}
			else
			{
				k = AutoCityTextField.this.getSelectionEnd();
				str3 = str1.substring(0, i);
				String str4 = str1.substring(k, str1.length());
				AutoCityTextField.this.setText(str3 + c1 + str4);
				AutoCityTextField.this.setCaretPosition(i + 1);
			}
			
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
