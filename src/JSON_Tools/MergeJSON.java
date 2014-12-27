package JSON_Tools;

import java.util.ArrayList;

//	Inherits from:	WriteToFile:
//		Refs:
//			String filePath
//		Methods:
//			WriteToFile(String path)					// default is true
//			WriteToFile(String path, boolean append)	// true = append, false = clear out file and write
//			void closeFiles()							// always call this at the end

//	Inherits from WriteTools:
//		Refs:
//			StringTools stringTools
//			JT_userInputs userInputs
//			Sundry String representations of key chars (e.g. INVERTED_COMMA)		
//		Methods:
//			String getIndentString()
//			void printStandAloneComma()
//			void openCurlyBracket()
//			void closeCurlyBracket()
//			openSquareBracket()
//			closeSquareBracket()

public class MergeJSON	extends WriteTools 
{	private ReadFile masterFile;
	private ReadFile supplyFile;
	
	private ArrayList<String> supplyFileContents;	// ArrayList is uni dimensional so content and index (key field
	private ArrayList<String> supplyFileTitles;		// values) are in separate arrays (which must have same size)
	
	// below are for more easy to read code:
	private static int KEY_TITLE = 0;				// Used to ensure we increment the right counters		
	private static int SUPPLY_TITLE = 1;
	private static boolean ADD_QUOTES = true;
	private boolean writeAfterReading = true;
	
	private int countKeyTitleOpeningBrackets = 0;		// 	these count brackets to ID when the end of a data 
	private int countSupplyChunkOpeningBrackets = 0;	//	chunk is found. Declared here for neater code 
	private int countKeyTitleClosingBrackets = 0;
	private	int countSupplyChunkClosingBrackets = 0;

	private String keyTitleStr;						// used extensively so available throughout the class		
		
	public MergeJSON() 
	{	setArrays();
		keyTitleStr = userInputs.getKeyFieldStr();		

			if(dissectSupplyFile())						// ensure supply file has been stripped before moving on to:
				readMasterAndMerge();
			
		closeFiles();
	}
	
	private void setArrays()
	{	supplyFileContents = new ArrayList<String>();	// stores verbatim rows of content from supply data chunks
		supplyFileTitles = new ArrayList<String>();		// stores the key field row associated with the data chunk
	}
	
	private boolean dissectSupplyFile()				//	breaks supply file into data chunks 	
	{	String supplyTitleBuf = userInputs.getLevel21KeyFieldStr();
		writeAfterReading = false;					// when we read lines they are NOT written to the output file.
		
		supplyFile = new ReadFile(userInputs.getSupplyFilePath());
		
			while(supplyFile.notEOF())
				setNextDataChunk(keyTitleStr, supplyTitleBuf);	// does the real work of grabbing data chunk strings

			supplyFile.closeStream();							// done with supplyFile so close it.
		
		return true;
	}		
	
	private void readMasterAndMerge()
	// Here we open the master JSON and start reading it in.  Each line read in is written to output file.  
	// We look for key title and when we find it we check the index array to see if there is supply data.
	{	String nextKeyStr = EMPTY_STRING; 
		String readStrBuf = EMPTY_STRING;
		String matchingSupplyDataBuf = "";
		
		writeAfterReading = true;		// means that every non-empty line read in is written to the output file 
		masterFile = new ReadFile(userInputs.getMasterFilePath());
		
			openSquareBracket();						// writes first [ at the beginning of the file.
			
			while(masterFile.notEOF())
			{	countKeyTitleOpeningBrackets = 0;		// reset each time (strictly speaking not needed)
				countKeyTitleClosingBrackets = 0;		// makes me nervous to omit though
				
				nextKeyStr = getNextKeyTitleString(masterFile, keyTitleStr);
				// this has found the next instance of key title in master and written all rows that were 
				// read in in the course of finding it to output (including the line containing the key title)

				if(!nextKeyStr.equals(EMPTY_STRING))	
				{							// An instance of key field has been found in master.  Before
											// we check whether it is in supply we need to read and write up
											// to the closing bracket (we write just before this)
											// NB: we can't use readBuffer - we need to find the last line - the 
											// closing } and insert our supply data before that.

					readStrBuf = EMPTY_STRING;		// initialise for the first iteration below (avoids using OR
													// for NULL in the if statement).
					do
					{	if(!readStrBuf.equals(EMPTY_STRING)) writeString(readStrBuf);			
						readStrBuf = getNextNonNullLine(masterFile);
						checkLineForBrackets(readStrBuf, KEY_TITLE);
					}
					while(	(countKeyTitleClosingBrackets < countKeyTitleOpeningBrackets)||
							(countKeyTitleClosingBrackets == 0));
					// while there is an imbalance of brackets (after at least 1 opening bracket is found:

					// now we are in the right place to write the supply data.  First check there is some:
					matchingSupplyDataBuf = returnMatchingSupplyData(nextKeyStr);
					if(matchingSupplyDataBuf.equals("NO_MATCHING_SUPPLY_DATA"))
					{	// Do nothing - no matching data found so nothing to insert.						
					}
					else
					{	writeString(COMMA);
						writeString(matchingSupplyDataBuf);
					}
					writeString(readStrBuf);		// write closing bracket (& anything else on the row) to output
					// TODO: sort out indentation - remove from imported in dissect then add in when writing.
				}
			}
			masterFile.closeStream();
	}
	
	private String returnMatchingSupplyData(String masterKey)
	{	try
		{	int keyIndex = supplyFileTitles.indexOf(masterKey);
			String returnStr = supplyFileContents.get(keyIndex);
		
			return returnStr;				//	alternately we could do this all in one (which is much less readable):
		}										//	return (supplyFileContents.get(supplyFileTitles.indexOf(masterKey)));
		catch(ArrayIndexOutOfBoundsException ex)
		{ 	return "NO_MATCHING_SUPPLY_DATA";
			
		}
	}
	
	private void setNextDataChunk(String keyFieldTitleStr, String supplyTitleStr)
	{	// Adds 1 complete data chunk (Str) from the JSON input file to supplyFileContents...
		// First we have to find the next instance of the key field: getKeyFieldLabelStr().
		// We strip the white spaces and store the string we find (as an index for supplyFileContents).
		// Then we find the next instance of the supply title and concatenate with every subsequent row 
		// read in until the appropriate closing bracket.  a square bracket: ]
		// Of course the supply title might not exist for this key field record, so we also need to count
		// brackets so that we know when the end of the key field record is reached. a curry bracket: }
		// If supply title is found we extract the full content and then add this string to supplyFileContents.  
		
		countKeyTitleOpeningBrackets = 0;			// reset each time
		countKeyTitleClosingBrackets = 0;

		boolean endOfDataChunk = false;
		String dataChunkStr = EMPTY_STRING;			// this string we build up with data chunk and return

		// Below 2 lines do the following:
			// find the next key field title and read it into inputFileLineBuf, 
			// start counting opening and closing brackets.
			// put inputFileLineBuf into the index array.
	  	// OR we identify EOF and pass execution back to the calling method
		String inputFileLineBuf = getNextKeyTitleString(supplyFile, keyFieldTitleStr);
		
			if(!inputFileLineBuf.equals(EMPTY_STRING)) 	supplyFileTitles.add(inputFileLineBuf);
			else										return;	// must be EOF (no more key titles)

		// We now need to look for the supply title:
		while(supplyFile.notEOF())			// loop until the supply title is found			
		{	if(stringTools.x_Contains_y(inputFileLineBuf, supplyTitleStr, ADD_QUOTES))
			// NOTE: adding the quotes above mitigates against the risk that the string will be used
			// elsewhere in the file (e.g. "services" in: "serviceName" : "Gastrointestinal and Liver services")
			{	dataChunkStr = (inputFileLineBuf + CARRIAGE_RETURN);
			
				while(!endOfDataChunk)
				{	inputFileLineBuf = getNextNonNullLine(supplyFile);
					dataChunkStr += (inputFileLineBuf + CARRIAGE_RETURN);
					checkLineForBrackets(inputFileLineBuf, SUPPLY_TITLE);		// checks brackets in line & increments counts
					endOfDataChunk = (countSupplyChunkOpeningBrackets == countSupplyChunkClosingBrackets);
				}	// if we have a balance of opening and closing brackets end of the data chunk has been reached.
				
				supplyFileContents.add(dataChunkStr);
				return;
			}
			else		// SupplyTitle not found - check for bracketsthen read the next line in:
			{	checkLineForBrackets(inputFileLineBuf, KEY_TITLE);		// checks brackets in line & increments counts
				if (countKeyTitleOpeningBrackets == countKeyTitleClosingBrackets)
				{	supplyFileContents.add(EMPTY_STRING);
					return;
				}	// this bit bails us out if no supply title is found up until the end of the key field record
				inputFileLineBuf = getNextNonNullLine(supplyFile);
			}
		}
		// if we reach here the key title wasn't found in supply file (must be eof)
	}
	
	private String readBuffer(ReadFile readFile)
	// reads come through this... it is set to either print every line it encounters or not
	// this is necessary because the master file needs to be written as it is read, but supply does not.
	// we do this so that we can share functions (getNextKeyTitleString)
	{	String readFileLineStr = getNextNonNullLine(readFile);
			if(writeAfterReading)	writeString(readFileLineStr);
		return readFileLineStr;
	}
	
	private String getNextKeyTitleString(ReadFile targetFile, String keyFieldTitle)
	{	String inputFileLineStr = readBuffer(targetFile);
		String returnStr = EMPTY_STRING;
		
		while(targetFile.notEOF())			// loop until the key field title (or EOF) is found
		{	if(stringTools.x_Contains_y(inputFileLineStr, keyFieldTitle, ADD_QUOTES))
			{	countKeyTitleOpeningBrackets = 1;			// inferred, not actually found.  Is OK though. TODO	
				return (stringTools.stripSpacesFromStr(inputFileLineStr));		
			}
			else	inputFileLineStr = readBuffer(targetFile);	// read the next line (and write, if appropriate).
		}
		return returnStr;
	}
	
	private void checkLineForBrackets(String lineToCheck, int checkType)
	// checks the String passed in for opening and closing brackets (square or curly depends on checkType).
	// where either is found the count is incremented (count incremented also depends on checkType)
	// By using CheckType we reuse this method for key title and supply chunk title.
	// NB: only checks for 1 instance (of each type) per line.
	{
		if (checkType == KEY_TITLE)			// check for curly brackets:
			{	if(stringTools.x_Contains_y(lineToCheck, OPEN_CURLY_BRACKET))		countKeyTitleOpeningBrackets++;
				if(stringTools.x_Contains_y(lineToCheck, CLOSE_CURLY_BRACKET))		countKeyTitleClosingBrackets++;
			}

		if (checkType == SUPPLY_TITLE)
			{	if(stringTools.x_Contains_y(lineToCheck, OPEN_SQUARE_BRACKET))	countSupplyChunkOpeningBrackets++;	
				if(stringTools.x_Contains_y(lineToCheck, CLOSE_SQUARE_BRACKET))	countSupplyChunkClosingBrackets++;
			}
	}

	private String getNextNonNullLine(ReadFile fileType)
	{	String inputFileStrBuf = fileType.readLine();		// read the next line, but is it NULL...
		
			while((inputFileStrBuf == null) && (fileType.notEOF())) 
				inputFileStrBuf = fileType.readLine();				// ensures we do not use a null input string
		
		return inputFileStrBuf;
	}
	
	private void testArrays()
	{	int supplyContentSize = 0;
		int supplyTitlesSize = 0;
		
		supplyContentSize = supplyFileContents.size();
			System.out.println("Number of items in supplyFileContents = " + supplyContentSize);
		
		supplyTitlesSize = supplyFileTitles.size();
			System.out.println("Number of items in supplyFileContents = " + supplyTitlesSize);

		if(supplyContentSize == supplyTitlesSize)
		{	for(int i = 0; i <supplyContentSize; i++)
			{	System.out.println("Title at " + i + " = " + supplyFileTitles.get(i));
				System.out.println("Content at " +  + i + " = " + supplyFileContents.get(i));
			}
		}
		else
		{	System.out.println("Problem... different array sizes detected");
		}
	}
}
