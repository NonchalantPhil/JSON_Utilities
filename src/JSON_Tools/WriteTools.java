package JSON_Tools;

//	Inherits from:	WriteToFile:
//		Refs:
//			String filePath
//		Methods:
//			WriteToFile(String path)					// default is true
//			WriteToFile(String path, boolean append)	// true = append, false = clear out file and write
//			void closeFiles()							// always call this at the end

public class WriteTools extends WriteToFile
{	// this class is inherited by the different tool controller classes (i.e. convert, merge and split)
	// and provides numerous tools used by each
	
	protected StringTools stringTools;
	protected JT_UserInputs userInputs;		// passed to UI so that user inputs can be added
	protected WriteToFile writeToFile;
	protected int currentIndentLevel = 0;
	
	protected final String INVERTED_COMMA = "\"";
	protected final String CARRIAGE_RETURN = "\n";
	protected final String OPEN_CURLY_BRACKET = "{";
	protected final String CLOSE_CURLY_BRACKET = "}";
	protected final String OPEN_SQUARE_BRACKET = "[";
	protected final String CLOSE_SQUARE_BRACKET = "]";
	protected final String EMPTY_STRING = "";
	protected final String COMMA = ",";
	
	private String indentStr = "   ";		// 3 spaces
	
	public WriteTools() 
	{	super	(	JU_main.getJT_UserInputs().getOutputFilePath(),
					JU_main.getJT_UserInputs().getOutputFileName()
				);		// we now have access to filePath
	
		stringTools = JU_main.getStringTools();
		userInputs = JU_main.getJT_UserInputs();
	}
	
	protected String getIndentString()		// provides some basic formatting for the output
	{	String strBuf = "";
		if(currentIndentLevel==0) return EMPTY_STRING;
		for(int i = 0; i < currentIndentLevel; i++) 
			strBuf+=indentStr;		
		return strBuf;
	}
	
	protected void printStandAloneComma()	// Used to print a comma - requires an additional indent level first
	{	currentIndentLevel++;				// NB: only used to print the commas that separate top level records
		writeString(getIndentString()+ COMMA);
		currentIndentLevel--;		// put it back to what it was
	}
	
	protected void openCurlyBracket()			// prints bracket and also tracks indent level
	{	currentIndentLevel++;	
		writeString(getIndentString() + OPEN_CURLY_BRACKET);
	}
	
	protected void closeCurlyBracket()
	{	writeString(getIndentString() + CLOSE_CURLY_BRACKET);
		currentIndentLevel--;
	}	
	
	protected void openSquareBracket()			// prints bracket and also tracks indent level
	{	currentIndentLevel++;	
		writeString(getIndentString() + OPEN_SQUARE_BRACKET);
	}
	
	protected void closeSquareBracket()
	{	writeString(getIndentString() + CLOSE_SQUARE_BRACKET);
		currentIndentLevel--;
	}
}
