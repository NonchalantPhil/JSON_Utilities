package JSON_Tools;

public class JT_UserInputs {
// mostly just  a container for user inputs - makes for easier reading code (rather than passing a 
//	shedload of parameters between the UI panel and main.
// In addition, this class is responsible for checking validity of the data it serves up...
// the validity is served up through public methods.
	

	private String masterFilePath = "";
	private String csvSupplyFilePath = "";
	private String outputFilePath = "";			// Note - for output file only the name is captured separately
		private String outputFileName = "";		// from the path. 
	private String keyFieldStr = "";
	private String level21KeyFieldStr = "";

	
	private int keyFieldStartCol = 0;
	private int keyFieldEndCol = 0;
	private int level21FieldStartCol = 0;
	private int level21FieldEndCol = 0;
	
	public JT_UserInputs() 
	{	// to much work writing constructors, and they may be a bit messy when called
	}	// really does make sense to just use the accessors and mutators (much more readable client side)

	public String getMasterFilePath()
	{	return masterFilePath;
	}
	public void setMasterFilePath(String str)
	{	masterFilePath = str;
	}
	public String getSupplyFilePath()
	{	return csvSupplyFilePath;
	}
	public void setSupplyFilePath(String str)
	{	csvSupplyFilePath = str;
	}
	
	public String getOutputFileName()
	{	return outputFileName;
	}
	public void setOutputFileName(String str)
	{	outputFileName = str;
	}
	
	public String getOutputFilePath()
	{	return outputFilePath;
	}
	public void setOutputFilePath(String str)
	{	outputFilePath = str;
	}
	
	public String getOutputFilePathAndName()			// output folder and file name are entered separately
	{	return (outputFilePath + "/" + outputFileName +".JSON");		// TODO: 
	}
	
	public String getKeyFieldStr()
	{	return keyFieldStr;
	}
	public void setKeyFieldStr(String str)
	{	keyFieldStr = str;
	}
	public String getLevel21KeyFieldStr()	
	{	return level21KeyFieldStr;
	}
	public void setLevel21KeyFieldStr(String str)
	{	level21KeyFieldStr = str;
	}
	
	public int getKeyFieldStartCol()
	{	// REM - value entered by human - to turn into value for reading arrays we subtract 1
		return (keyFieldStartCol - 1);
	}
	public void setKeyFieldStartCol(int intBuf)
	{	keyFieldStartCol = intBuf;
	}
	public int getKeyFieldEndCol()
	{	// here we do not subtract 1 - arrays use < so no need to.
		return keyFieldEndCol;
	}
	public void setKeyFieldEndCol(int intBuf)
	{	keyFieldEndCol = intBuf;
	}
	public int getLevel21FieldStartCol()
	{	// REM - value entered by human - to turn into value for reading arrays we subtract 1
		return (level21FieldStartCol - 1);
	}
	public void setLevel21FieldStartCol(int intBuf)
	{	level21FieldStartCol = intBuf;
	}
	public int getLevel21FieldEndCol()
	{	return level21FieldEndCol;
	}
	public void setLevel21FieldEndCol(int intBuf)
	{	level21FieldEndCol = intBuf;
	}

	public boolean hasValidHeaderFile(int fileID)					// used in InputPanel to check before trying to load headers
	{	// fileID==1 means check the Master file
		if (fileID==1)		
			return checkIsValidPath(masterFilePath);
		
		// fileID==2 means check the Supply file
		if (fileID==2)		
			return checkIsValidPath(csvSupplyFilePath);
		
		return false;
	}
	
	public void setFromCommand(String methodID, String paramValue)
	{	// when the app is ran from the command line, name-value pairs are read in from the 
		// config file.  Pairs are passed here and, based on the name, the appropriate param here
		// is set to the value passed in:
		
		if		(methodID.equals("OutputFilePath"))			setOutputFilePath(paramValue);
		else if	(methodID.equals("OutputFileName"))			setOutputFileName(paramValue);
		
		else if	(methodID.equals("KeyFieldName"))			setKeyFieldStr(paramValue);
		else if	(methodID.equals("SupplyFieldName"))		setLevel21KeyFieldStr(paramValue);
		
		else if	(methodID.equals("MasterInputPath"))		setMasterFilePath(paramValue);
		else if	(methodID.equals("SupplyInputPath"))		setSupplyFilePath(paramValue);	

		else if	(methodID.equals("KeyFieldStartCol"))		setKeyFieldStartCol(castToInt(paramValue));	
		else if	(methodID.equals("KeyFieldEndCol"))			setKeyFieldEndCol(castToInt(paramValue));	

		else if	(methodID.equals("SecondLevelFieldStart"))	setLevel21FieldStartCol(castToInt(paramValue));	
		else if	(methodID.equals("SecondLevelFieldEnd"))	setLevel21FieldEndCol(castToInt(paramValue));
		
		//testContent();
	}
	
	private boolean hasValidPathData()		//	used in Convert to check input and output files
	{	if (!checkIsValidPath(masterFilePath)) return false;
		if (!checkIsValidPath(getOutputFilePathAndName())) return false;
		
		return true;
	}
	
	private boolean hasValidMergePathData()	//	used in Merge to check master and supply files
	{	if (!checkIsValidPath(masterFilePath)) return false;
		if (!checkIsValidPath(getOutputFilePathAndName())) return false;
		if (!checkIsValidPath(csvSupplyFilePath)) return false;
		
		return true;
	}
	
	public boolean hasValidKeyFieldDataSet()
	{	return checkDataSetValid(keyFieldStr, keyFieldStartCol, keyFieldEndCol);
	}
	public boolean hasValidLevel21DataSet()
	{	return checkDataSetValid(level21KeyFieldStr, level21FieldStartCol, level21FieldEndCol);
	}
	
	public boolean isOKToConvert()
	{	boolean boolBuf = true;
		
		if (!hasValidPathData()) boolBuf = false;			// must have valid input and output paths
		if (!hasValidKeyFieldDataSet()) boolBuf = false;	// must have at least 1 pair of valid ints 
	
		return boolBuf;
	}

	public boolean isOKToMerge()
	{	boolean boolBuf = hasValidMergePathData();		// ensures all 3 paths are set
	
		// TODO: check if output file = input file (will break things)
		
		if 	(	(keyFieldStr.equals("")) ||			// ensures key field label is set
				(keyFieldStr == null)
			)
				boolBuf = false;

		if 	(	(level21KeyFieldStr.equals("")) ||			// ensure supply data field label is set
				(level21KeyFieldStr == null)
			)
				boolBuf = false;
		
		return boolBuf;
	}
	
	private boolean checkIsValidPath(String strPath)
	{	boolean boolBuf = true;
		if (strPath.length() < 5) boolBuf = false;			// must be at least 5 chars long
		if (!(JU_main.getStringTools().x_Contains_y(strPath, "."))) boolBuf = false;	// must be a full stop
		return boolBuf;
	}
	
	private boolean checkDataSetValid(String titleStr, int start, int end)
	{	boolean boolBuf = true;
		if (titleStr.equals("")) boolBuf = false;	// no title supplied invalidates data set
		if (start < 1) boolBuf = false;				// start column of data not valid
		if (end < 1) boolBuf = false;				// end column of data not valid
		if (end < start) boolBuf = false;			// end column cannot be before start col
		return boolBuf;
	}

	public int castToInt(String str)				// tries to cast - returns -1 if fails.
	{	int intBuf = 0;
		try
		{	intBuf = Integer.parseInt(str);
		}
		catch (NumberFormatException nfe)
		{	intBuf = -1;
		}
		return intBuf;
	}
	
	public void testContent()		// False means overwrite existing content in UI
	{	//JU_main.writeToUI("Path data valid = " + hasValidPathData(), false);
		//JU_main.writeToUI("Input Path  = " + csvMasterFilePath, false);
		//JU_main.writeToUI("Output Path = " + outputFilePath, false);
		//JU_main.writeToUI("Key data set valid = " + hasValidKeyFieldDataSet(), false);
	
		//JU_main.writeToUI("Key data field = " + keyFieldStr, false);
		//JU_main.writeToUI("Key data start col = " + keyFieldStartCol, false);
		//JU_main.writeToUI("Key data start col = " + keyFieldEndCol, false);
	
		//JU_main.writeToUI("Level21 data set valid = " + hasValidLevel21DataSet(), false);
		//JU_main.writeToUI("Level22 data set valid = " + hasValidLevel22DataSet(), false);
	

		System.out.println("Master Input Path  = " + masterFilePath);
		System.out.println("Supply Input Path  = " + csvSupplyFilePath);		

		System.out.println("Output Path = " + outputFilePath);
		System.out.println("Output Name = " + outputFileName);
		
		System.out.println("Key data field = " + keyFieldStr);
		System.out.println("Supply data field = " + level21KeyFieldStr);		

		System.out.println("Key data start col = " + keyFieldStartCol);
		System.out.println("Key data start col = " + keyFieldEndCol);

		System.out.println("Secondary data start col = " + level21FieldStartCol);
		System.out.println("Secondary data start col = " + level21FieldEndCol);
		
		//System.out.println("Path data valid = " + hasValidPathData());		
		//System.out.println("Key data set valid = " + hasValidKeyFieldDataSet());
		//System.out.println("Level21 data set valid = " + hasValidLevel21DataSet());
		//System.out.println("Level22 data set valid = " + hasValidLevel22DataSet());

	}

	
}
