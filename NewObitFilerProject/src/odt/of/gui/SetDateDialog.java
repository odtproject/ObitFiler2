package odt.of.gui;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import odt.of.main.ObituaryFiler;

class SetDateDialog
  extends Dialog
  implements ActionListener, WindowListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -8072892294334645912L;
	Button bOk;
	Button bCancel;
	TextField field;
	Label lab;
	Date theDate;
	Frame mainFrame;
	ObituaryFiler app;
  
	public SetDateDialog(ObituaryFiler paramObituaryFiler, Frame paramFrame, String paramString)
	{
		super(paramFrame, paramString, true);
		this.app = paramObituaryFiler;
		this.mainFrame = paramFrame;
		setLayout(new BorderLayout());
		Panel localPanel1 = new Panel();
		localPanel1.setLayout(new GridLayout(2, 1));
		String str = paramObituaryFiler.config.getCurrentDateDISPLAY();
		localPanel1.add(this.lab = new Label("Enter Date in this format: " + str, 1));
		localPanel1.add(this.field = new TextField(str, str.length() + 5));
		addWindowListener(this);
		add("Center", localPanel1);
		Panel localPanel2 = new Panel();
		localPanel2.setLayout(new GridLayout(1, 2, 2, 2));
		localPanel2.add(this.bOk = new Button("Ok"));
		this.bOk.addActionListener(this);
		localPanel2.add(this.bCancel = new Button("Cancel"));
		this.bCancel.addActionListener(this);
		Panel localPanel3 = new Panel();
		localPanel3.add(localPanel2);
		add("South", localPanel3);
	}
  
	public void windowDeactivated(WindowEvent paramWindowEvent) {}
  
	public void windowClosing(WindowEvent paramWindowEvent)
	{
		this.theDate = null;
		setVisible(false);
	}
  
	public void packAndMove()
	{
		pack();
		Point localPoint = this.mainFrame.getLocation();
		Dimension localDimension1 = this.mainFrame.getSize();
		Dimension localDimension2 = getSize();
		localPoint.translate((localDimension1.width - localDimension2.width) / 2, (localDimension1.height - localDimension2.height) / 2);
		setLocation(localPoint);
	}
  
	public void updateFonts()
	{
		Font localFont = this.app.config.getFont();
		this.bOk.setFont(localFont);
		this.bOk.invalidate();
		this.bCancel.setFont(localFont);
		this.bCancel.invalidate();
		this.field.setFont(localFont);
		this.field.invalidate();
		this.lab.setFont(localFont);
		this.lab.invalidate();
	}
  
	public void actionPerformed(ActionEvent paramActionEvent)
	{
		Object localObject = paramActionEvent.getSource();
		if (localObject == this.bOk) {
			try
			{
				this.theDate = DateFormat.getDateInstance(1).parse(this.field.getText());
			}
			catch (ParseException localParseException)
			{
				Toolkit.getDefaultToolkit().beep();
				this.field.selectAll();
				this.field.requestFocus();
				return;
			}
		}
		
		if (localObject == this.bCancel) {
			this.theDate = null;
		}
		setVisible(false);
	}
  
	public void windowOpened(WindowEvent paramWindowEvent) {}
  
	public void windowClosed(WindowEvent paramWindowEvent) {}
  
	public void windowDeiconified(WindowEvent paramWindowEvent) {}
  
	public void windowActivated(WindowEvent paramWindowEvent)
	{
		this.field.requestFocus();
	}
  
	public void selectAll()
	{
		this.field.selectAll();
	}
  
	public void windowIconified(WindowEvent paramWindowEvent) {}
  
	public void showIt()
	{
		updateFonts();
		packAndMove();
		selectAll();
		setVisible(true);
	}
}
