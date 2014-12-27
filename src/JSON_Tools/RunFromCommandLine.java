package JSON_Tools;

//	Inherits from:	ReadFile:
	//	Refs:
	//		String filePath
	//	Methods:
	//		boolean notEOF()			// true until EOF is reached (more readable code)
	//		String readLine()			// reads and returns next line
	//		String getLineString()		// used to return content of First line (which is read on construction)

public class RunFromCommandLine extends ReadFile
{	private static String DELIM_CHAR = ",";
	
	StringTools strTools;
	JT_UserInputs userInputObj;
	
	public RunFromCommandLine(String inputFilePath) 
	{	super(inputFilePath);
			strTools = JU_main.getStringTools();
			userInputObj = JU_main.getJT_UserInputs();
		
		System.out.println("Command line param passed in: " + inputFilePath);
		while (readInputFile()); 	// do nothing;
		closeStream();
			
	}

	private boolean readInputFile()
	{	String actionInstruction = getLineString();		// first line read in when file was opened
		String inputRowStrBuf = actionInstruction;

			while(notEOF())
			{	if(strTools.x_Contains_y(inputRowStrBuf, DELIM_CHAR))
				{	extractNameValuePair(inputRowStrBuf);
				}
				inputRowStrBuf = readLine();
				if(!(inputRowStrBuf == null))
				{
					if((inputRowStrBuf.equals("MERGE")) || (inputRowStrBuf.equals("CONVERT")))
					{	// 	this bit is only reached if a SECOND action instruction is found.
						// 	if so, we perform the action now... params read in apply to previous action instruction:
						performAction(actionInstruction);
						actionInstruction = inputRowStrBuf;	
					}
				}
			}
			performAction(actionInstruction);		// if there is only 1 action it is performed here.		
		return false;
	}
	
	private void performAction(String action)
	{	if		(action.equals("MERGE"))
		{	MergeJSON mergeJSON;
			mergeJSON = new MergeJSON();
		}
		else if	(action.equals("CONVERT"))
		{	ConvertToJSON convert;
			convert	= new ConvertToJSON();  
		}
	}
	
	private void extractNameValuePair(String fullStr)
	{	String nameStr;
		String valueStr;
		
		nameStr = strTools.returnFieldValue(fullStr, DELIM_CHAR, 1);
		valueStr = strTools.returnFieldValue(fullStr, DELIM_CHAR, 2);
		
		if(!valueStr.equals(""))								// ignore where param value is blank
			userInputObj.setFromCommand(nameStr, valueStr);
		//	The  'call your own methods' approach!  Passes an ID used by input class to assign value to right place
		
		//System.out.println("Line read in = : " + fullStr);
		//System.out.println("   Name =  : " + nameStr);
		//System.out.println("   Value = : " + valueStr);
	}
}
