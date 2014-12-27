package JSON_Tools;

public class CSV_Headings
// this class reads the first line of the file that has been passed and breaks it into individual
// headings.  It stores these, and the number of headings, for use outside this class.
{
	private String[] fieldHeadings;
	private int numberOfFields = 0;

	public CSV_Headings(String cPath) 
	{	String strBuf;
		ReadFile readFile;

		readFile = new ReadFile(cPath);
			strBuf = readFile.getLineString();
			setHeadings(strBuf);
		readFile.closeStream();
	}

	private void setHeadings(String firstRow)
	{	String rawHeaderRow = firstRow;
		
		countFields(rawHeaderRow);		// which sets the global field count variable
		
		fieldHeadings = new String[numberOfFields];		// we know the # of fields so set the array
		populateTitlesArray(rawHeaderRow);
	}

	private void countFields(String rawHeader)
	{	int indexInt;
		indexInt = rawHeader.indexOf(",");

		while (indexInt > -1)
		{	numberOfFields++;
			indexInt = rawHeader.indexOf(",", indexInt+1);
		}
		numberOfFields++;		// One more field that there is delimiters
	}

	private void populateTitlesArray(String rawHeader)
	{	int fieldStartInt;
		int fieldFinishInt;

		fieldStartInt = 0;
		fieldFinishInt = rawHeader.indexOf(",");
	
		for (int i=0; i<numberOfFields; i++ )
		{	fieldHeadings[i] = rawHeader.substring(fieldStartInt, fieldFinishInt);
		
			fieldStartInt = (fieldFinishInt+1);
			if (i == (numberOfFields-2))
			{	fieldFinishInt = rawHeader.length();
			}
			else
			{	fieldFinishInt = rawHeader.indexOf(",", fieldStartInt+1);
			}
		}
	}
	
	// Below are public gets:
	//	
	public String getHeadingAt(int i)							// TODO - looks like we can break this
	{	if (fieldHeadings == null) return "";
		if (i > (fieldHeadings.length + 1)) return "";
		return fieldHeadings[i];
	}

	public int getNumberOfFields()
	{	return numberOfFields;
	}
	
}
