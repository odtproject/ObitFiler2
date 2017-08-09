package odt.of.main;
import java.util.Enumeration;

@SuppressWarnings("rawtypes")
class PaperEnumeration
  implements Enumeration
{
	int index = 0;
	ObitEntryConfig c;
  
	public Object nextElement()
	{
		PaperConfig localPaperConfig = (PaperConfig)this.c.papers.elementAt(this.index++);
		return localPaperConfig.getPaperAbbreviation();
	}
  
	public PaperEnumeration(ObitEntryConfig paramObitEntryConfig)
	{
		this.c = paramObitEntryConfig;
	}
  
	public boolean hasMoreElements()
	{
		return this.c.papers.size() > this.index;
	}
}
