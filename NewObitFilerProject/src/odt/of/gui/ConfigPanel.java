package odt.of.gui;
import java.awt.Panel;

abstract class ConfigPanel
  extends Panel
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 5161183424123170472L;

	public abstract boolean isValidForm(boolean paramBoolean);
  
	public abstract String cardType();
}
