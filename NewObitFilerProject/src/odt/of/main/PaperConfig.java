package odt.of.main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

import odt.of.util.ErrorLog;

public class PaperConfig
  implements Serializable
{
	static final long serialVersionUID = 218400003L;
	static final int FREQ_DAILY = 0;
	static final int FREQ_WEEKLY = 1;
	static final int FREQ_MONTHLY = 2;
	Date currentDay = new Date();
	String paperAbbreviation;
	String paperDirectory;
	String paperLocation;
	public Vector<String> autoCities;
	int frequency;
	int daysPrevious;
	int version;
  
	public PaperConfig(String paramString1, String paramString2, int paramInt1, int paramInt2, String[] paramArrayOfString)
	{
		this.paperAbbreviation = paramString1;
		this.paperDirectory = paramString1;
		this.paperLocation = paramString2;
		this.autoCities = new Vector<String>();
		this.frequency = paramInt1;
		this.daysPrevious = paramInt2;
		this.version = ObituaryFiler.VERSION;
		for (int i = 0; i < paramArrayOfString.length; i++) {
			this.autoCities.addElement(paramArrayOfString[i]);
		}
	}
  
	private void readObject(ObjectInputStream paramObjectInputStream)
			throws IOException
	{
		try
		{
			paramObjectInputStream.defaultReadObject();
			if (this.version > ObituaryFiler.VERSION)
			{
				new ErrorLog(null, "Data file created with a newer version, aborting.");
				System.exit(1);
			}
			this.version = ObituaryFiler.VERSION;
			return;
		}
		catch (ClassNotFoundException localClassNotFoundException)
		{
			new ErrorLog(localClassNotFoundException, "ClassNotFoundException in readObject");
		}
	}
  
	public void dump()
	{
		System.err.println("currentDay: " + this.currentDay);
		System.err.println("paperAbbreviation: " + this.paperAbbreviation);
		System.err.println("paperDirectory: " + this.paperDirectory);
		System.err.println("paperLocation: " + this.paperLocation);
		for (int i = 0; i < this.autoCities.size(); i++) {
			System.err.println("autoCities: " + this.autoCities.elementAt(i));
		}
		System.err.println("frequency: " + this.frequency);
		System.err.println("daysPrevious: " + this.daysPrevious);
		System.err.println("version: " + this.version);
	}
  
	public void updatePaper(String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString)
	{
		this.paperLocation = paramString;
		this.frequency = paramInt1;
		this.daysPrevious = paramInt2;
		this.autoCities = new Vector<String>();
		for (int i = 0; i < paramArrayOfString.length; i++) {
			this.autoCities.addElement(paramArrayOfString[i]);
		}
	}
  
	public String getPaperAbbreviation()
	{
		return this.paperAbbreviation;
	}
}
