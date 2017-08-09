package odt.of.main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import odt.of.util.ErrorLog;

public class ObitEntry
  implements Serializable, Cloneable
{
	static final long serialVersionUID = 218400001L;
	private String lastName;
	private String firstName;
	private String maidenName;
	private String nickName;
	private Vector<String> otherNames;
	private String age;
	private String birthCity;
	private String birthState;
	private String city;
	private String state;
	String paperAbbreviation;
	String paperLocation;
	String tagname;
	Date date;
	boolean dirty;
	int version;
  
	public String format(ObitEntryConfig paramObitEntryConfig)
	{
		String str = new String("");
		str = str + this.getLastName();
		str = str + ", " + this.getFirstName();
		
		/* In getting the nickname, we'll allow the user to specify
		 * multiple nicknames by using a comma.
		 */
		if (!this.getNickName().equals("")) {
			String nickName = this.getNickName();
			
			if ( nickName.contains("/") ) {
				String[] nickParts = nickName.split("/");
				for ( int n=0; n<nickParts.length; n++ ) {
					str = str + " \"" + nickParts[n].trim() + "\"";
				}
			}
			else {
				str = str + " \"" + this.getNickName() + "\"";
			}
		} /* end of if the nickname is non-blank */
		
		/* If the maiden name field has a question mark, then just put
		 * a blank in the maiden name in parentheses.
		 */
		if (this.getMaidenName().equals("?")) {
			str = str + " ( )";
		} else if (!this.getMaidenName().equals("")) {
			str = str + " (" + this.getMaidenName() + ")";
		}
		
		/* Get the 'other' names and put each of them in a set of square brackets. */
		Enumeration<String> localEnumeration = this.getOtherNames().elements();
		while (localEnumeration.hasMoreElements()) {
			str = str + " [" + localEnumeration.nextElement() + "]";
		}
		
		/* Get the age field */
		str = str + "; ";
		if (!this.getAge().equals("")) {
			str = str + this.getAge().toString();
		}
		str = str + "; ";
		
		String deathPlace = "";
		String deathState = this.getState();
		
		/* If the death place is non-blank, then get it.
		 * We only want to add the city if there is a state.
		 */
		if (!deathState.equals("")) {		
			
			/* If the death city is non-blank, put both the
			 * city and the state in the death Place.
			 * Otherwise, just put in the state.
			 */
			if (!this.getCity().equals("")) {
				deathPlace = this.getCity() + " " + deathState;
			}
			else {
				deathPlace = deathState;
			}
			
		} /* end of if there is a death state */
		
		/* Likewise, we only include a birth city if there is a 
		 * birth state and it is not equal to the death state.
		 */
		String birthPlace = "";
		String birthState = this.getBirthState();
		if (!birthState.equals("") && !birthState.equals(deathState))
		{
			if (!this.getBirthCity().equals("")) {
				birthPlace = this.getBirthCity() + " " + birthState;
			}
			else {
				birthPlace = birthState;
			}
		}
		
		/* If there is a death place, we can add to the string.
		 * If there is a birthplace, add the birthplace, plus the
		 * ">" delimiter and the death place.
		 * Otherwise, just add the death place.
		 */
		if ( ! deathPlace.equals("") ) {
			
			if (! birthPlace.equals("") ) {
				str = str + birthPlace + ">" + deathPlace;
			}
			else {
				str = str + deathPlace;
			}
		}		

		/* Add the paper abbreviation and if appropriate, its location */
		str = str + "; " + this.paperAbbreviation;
		if (!this.getState().equals(this.paperLocation)) {
			str = str + " (" + this.paperLocation + ")";
		}
		
		/* Add the tagname */
		str = str + "; ";
		str = str + paramObitEntryConfig.getDateFORMAT(this.date) + "; " + this.tagname;
		return str;
	}
  
	public ObitEntry()
	{
		new ErrorLog(null, "calling empty constructor for ObitEntry");
	}
  
	public ObitEntry(Vector<String> paramVector, Date paramDate, String paramString1, String paramString2, String paramString3)
	{
		this.dirty = true;
		this.version = 100;
		this.date = paramDate;
		this.paperAbbreviation = paramString1;
		this.paperLocation = paramString2;
		this.tagname = paramString3;
		this.setLastName(paramVector.elementAt(0).trim());
		this.setFirstName(paramVector.elementAt(1).trim());
		this.setMaidenName(paramVector.elementAt(2).trim());
		this.setNickName(paramVector.elementAt(4).trim());
		this.setAge(paramVector.elementAt(5).trim());
		this.setBirthCity(paramVector.elementAt(6).trim());
		this.setBirthState(paramVector.elementAt(7).trim());
		this.setCity(paramVector.elementAt(8).trim());
		this.setState(paramVector.elementAt(9).trim());
		this.setOtherNames(new Vector<String>());
		String str1 = paramVector.elementAt(3);
		if (!str1.equals(""))
		{
			String str2 = new String("");
			int i = 0;
			for (int j = 0; j < str1.length(); j++)
			{
				char c = str1.charAt(j);
				if (c == '"') {
					i++;
				} else if ((i % 2 == 0) && (c == ' ')) {
					str2 = str2 + ',';
				} else {
					str2 = str2 + c;
				}
			}
			
			Vector<String> localVector = parseStringList(str2);
			Enumeration<String> localEnumeration = localVector.elements();
			while (localEnumeration.hasMoreElements()) {
				this.getOtherNames().addElement(localEnumeration.nextElement());
			}
		}
	}
  
	private Vector<String> parseStringList(String paramString)
	{
		Vector<String> localVector = new Vector<String>();
		StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ",");
		while (localStringTokenizer.hasMoreTokens()) {
			localVector.addElement(localStringTokenizer.nextToken());
		}
		return localVector;
	}
  
	public boolean isDirty()
	{
		return this.dirty;
	}
  
	private void readObject(ObjectInputStream paramObjectInputStream)
			throws IOException
	{
		try
		{
			paramObjectInputStream.defaultReadObject();
			if (this.version > 100)
			{
				new ErrorLog(null, "Data file created with a newer version, exitting.");
				System.exit(1);
			}
			this.version = 100;
			return;
		}
		catch (ClassNotFoundException localClassNotFoundException)
		{
			new ErrorLog(localClassNotFoundException, "ClassNotFoundException in readObject");
		}
	}
  
	public String sortKey()
	{
		return this.getLastName() + this.getFirstName();
	}
  
	public void dump(String paramString)
	{
		System.err.println("---- " + paramString + " ----");
		System.err.println("lastName: " + this.getLastName());
		System.err.println("firstName: " + this.getFirstName());
		System.err.println("maidenName: " + this.getMaidenName());
		System.err.println("nickName: " + this.getNickName());
		System.err.println("otherNames: " + this.getOtherNames());
		System.err.println("age: " + this.getAge());
		System.err.println("birthCity: " + this.getBirthCity());
		System.err.println("birthState: " + this.getBirthState());
		System.err.println("city: " + this.getCity());
		System.err.println("state: " + this.getState());
		System.err.println("paperAbbreviation: " + this.paperAbbreviation);
		System.err.println("paperLocation: " + this.paperLocation);
		System.err.println("tagname: " + this.tagname);
		System.err.println("date: " + this.date);
		System.err.println("dirty: " + this.dirty);
		System.err.println("version: " + this.version);
	}
  
	public Object getClone()
	{
		try
		{
			return clone();
		}
		catch (CloneNotSupportedException localCloneNotSupportedException)
		{
			new ErrorLog(localCloneNotSupportedException, "clone not supported in getClone");
		}
		return null;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMaidenName() {
		return maidenName;
	}

	public void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBirthCity() {
		return birthCity;
	}

	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
	}

	public String getBirthState() {
		return birthState;
	}

	public void setBirthState(String birthState) {
		this.birthState = birthState;
	}

	public Vector<String> getOtherNames() {
		return otherNames;
	}

	public void setOtherNames(Vector<String> otherNames) {
		this.otherNames = otherNames;
	}
}
