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

public class ConvertToJSON 	extends WriteTools 
{	private CSV_Headings csvHeadings;
	private ReadFile readFile;
	private ArrayList<String> recordResultSet;
		
	public ConvertToJSON()				// no messing - everything triggered from constructor
	{	setObjects();
		if (writeRecordsToFile()); // JU_main.writeToUI("File successfully created!", true);
	}
	
	private void setObjects() 
	{	String csvPath = userInputs.getMasterFilePath();
		csvHeadings = new CSV_Headings(csvPath);
		readFile = new ReadFile(csvPath);
	}
	
	private boolean writeRecordsToFile() 
	{	boolean notReachedEOF = true;

			recordResultSet = new ArrayList<String>();
		
			openSquareBracket();	// start the file.
		
			while (notReachedEOF)
			{	extractAndWriteRecordRows();		// add the number of rows contained within the record - we 
													// have read in this many + 1 (first row of the next record)
				notReachedEOF = readFile.notEOF();	// no idea why this is necessary - using readFile.notEOF()
			}										// in the while causes it the app to hang???
			closeSquareBracket();

		return true;
	}
	
	private void extractAndWriteRecordRows() 
	{	// here we will iterate through a specific RECORD's rows & put them in the arraylist
		//  - rem 1 or many rows per record.  Here we identify the last row of the record
		// BUT this necessarily means we have read in the first line of the next record
		// we must cater for this:
		boolean isLastRecord = false;
		int keyFieldLocation = userInputs.getKeyFieldStartCol();
		String currentKeyFieldValue = EMPTY_STRING;
		String nextKeyFieldValue = EMPTY_STRING;
		String currentRecordStrBuf = EMPTY_STRING;
		String nextRecordStrBuf;			// holds the first row of the next record

			if (recordResultSet.size() == 0)				// special case for the first row read in
			{	currentRecordStrBuf = readFile.readLine();	
				recordResultSet.add(currentRecordStrBuf);
			}
			else currentRecordStrBuf = recordResultSet.get(recordResultSet.size()-1);
			// here the 1st row was added in the last iteration of this method (or above)

			currentKeyFieldValue = stringTools.returnFieldValue(currentRecordStrBuf, (keyFieldLocation+1)); //	Extract key field value
			// 	Note - above assumes that the key field is the first one in the KeyField block.
		
			nextRecordStrBuf = readFile.readLine(); 	// read in the next row, to compare with current
			//	nextKeyFieldValue = stringTools.returnKeyFieldValue(nextRecordStrBuf); //	Extract key field value
			nextKeyFieldValue = stringTools.returnFieldValue(nextRecordStrBuf, (keyFieldLocation+1)); //	Extract key field value		
			
			if (!nextKeyFieldValue.equals(currentKeyFieldValue))		// when new key is found write array
			{	// we need to tell writeRecord when we detect that the next record is empty:
				isLastRecord = (nextKeyFieldValue.equals(""));	// means that first blank row terminates execution 
				
				// at this point we have the full set of rows making up a distinct record - all in recordResultSet
				// we will now print it, then clear it out for the new record
				if (writeRecord(isLastRecord)) recordResultSet.clear();		// clear is only called when write returns true.
			}
			// Finally we add the row we read in earlier from the next record and being populating the array again:
			recordResultSet.add(nextRecordStrBuf);
	}
	
	private boolean writeRecord(boolean lastRecord)
	// 	Writes the content of the recordResultSet to output.  Boolean lastRecord records whether it is the 
	//	last record - if so we don't print a trailing comma.   
	{	boolean boolBuf = true;
				
			openCurlyBracket();		// opens the braces for the full data record
			
			for(int i = 0; i < recordResultSet.size(); i++)			// iterates through each record in recordResultSet
			{	
				// Firstly we need to write first level data once per recordResultSet instance:
				if(i == 0)
				{	printData
					(	userInputs.getKeyFieldStartCol(), 
						userInputs.getKeyFieldEndCol(),
						0,
						userInputs.hasValidLevel21DataSet()	
					);							// last param is boolean; false = do not print last comma
					
					// Cue up the level 21 data while we are at it:
					if(userInputs.hasValidLevel21DataSet())	
					{	writeString
						(	getIndentString()+
							INVERTED_COMMA +
							userInputs.getLevel21KeyFieldStr() +
							INVERTED_COMMA +
							" : "
						);
						openSquareBracket();
					}
				}
				// next print second level data:
				if(userInputs.hasValidLevel21DataSet())
				{	openCurlyBracket();
					printData
					(	userInputs.getLevel21FieldStartCol(), 
						userInputs.getLevel21FieldEndCol(),
						i,
						false	
						);	
					closeCurlyBracket();
					
					if(!(i==(recordResultSet.size()-1))) writeString(getIndentString()+ COMMA);
					// for all but the last set of data we need to separate with a comma
				}
			}

			if(userInputs.hasValidLevel21DataSet())	closeSquareBracket();		// closes the braces for the 2nd level data			
			closeCurlyBracket();		// closes the braces for the full data record
			
			// Next we have to separate records with a comma - bear in mind this means we have to insert
			// a comma every time EXCEPT for the very last record.
			if(!lastRecord)	printStandAloneComma();
			
		return boolBuf;
	}
		
	private void printData(int startCol, int endCol, int rowNumber, boolean printLastComma)	// rowNumber is a reference to recordResultSet
	{	String strValueBuf;
		String strPrintBuf;

		for(int i = startCol; i < endCol; i++)
		{	// below we extract the value we are going to print in this iteration of i:
			strValueBuf = stringTools.returnFieldValue(recordResultSet.get(rowNumber),i+1); //returnFieldValue
			// next we construct the string we want to write to the output file:
			strPrintBuf =		getIndentString()+
								INVERTED_COMMA + 
								csvHeadings.getHeadingAt(i) +
								INVERTED_COMMA + 
								" : " + 
								INVERTED_COMMA + 
								strValueBuf +
								INVERTED_COMMA;
			
			// commas always added to all rows except the last one:
			if(i < (endCol -1))	strPrintBuf += COMMA;
			// in some cases we need the last comma to print (when there is second level data:
			if((i==(endCol-1) && printLastComma))	strPrintBuf += COMMA;
			
			writeString(strPrintBuf);				// ... and the string is printed.
		}		
	}
}
