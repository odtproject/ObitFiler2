package odt.of.gui;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MessageBox
  extends Dialog
  implements ActionListener, WindowListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -6169268719670595309L;
	TextArea text;
	Button bOk;
	Frame frame;
  
	public MessageBox(Frame paramFrame)
	{
		super(paramFrame, "Obituary Filer Message", true);
		this.frame = paramFrame;
		this.text = new TextArea("", 0, 0, 1);
		this.text.setEditable(false);
		add("Center", this.text);
		Panel localPanel = new Panel();
		localPanel.add(this.bOk = new Button("Ok"));
		this.bOk.addActionListener(this);
		add("South", localPanel);
		addWindowListener(this);
	}
  
	public void windowDeactivated(WindowEvent paramWindowEvent) {}
  
	public void windowClosing(WindowEvent paramWindowEvent)
	{
		setVisible(false);
	}
  
	void packAndMove(Frame paramFrame)
	{
		pack();
		Point localPoint = paramFrame.getLocation();
		Dimension localDimension1 = paramFrame.getSize();
		Dimension localDimension2 = getSize();
		localPoint.translate((localDimension1.width - localDimension2.width) / 2, (localDimension1.height - localDimension2.height) / 2);
		setLocation(localPoint);
	}
  
	public void actionPerformed(ActionEvent paramActionEvent)
	{
		setVisible(false);
	}
  
	public void windowOpened(WindowEvent paramWindowEvent) {}
  
	public void windowClosed(WindowEvent paramWindowEvent) {}
  
	public void windowDeiconified(WindowEvent paramWindowEvent) {}
  
	public void windowActivated(WindowEvent paramWindowEvent) {}
  
	public void windowIconified(WindowEvent paramWindowEvent) {}
  
	public void showIt(String paramString)
	{
		this.text.setText(" ");
		this.text.setText("");
		this.text.append(paramString);
		packAndMove(this.frame);
		setVisible(true);
	}
}
