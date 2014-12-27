package JSON_Tools;

public final class StringTools 
{	private final String INVERTED_COMMA = "\"";				// just to make code a bit more readable

	public StringTools() 
	{	// testStripQuotesFromString();
		// testReturnKeyFieldValue();
	}

	public boolean x_Contains_y(String x, String y)	
	{	try
		{	return (x.contains(y));
		}
		catch(Exception ex)
		{	return false;
		}
	}

	public boolean x_Contains_y(String x, String y, boolean b)	
	// b = true means surround y with inverted commas BUT this method is never called with False (no point)	
	{	String compStr = ("\"" + y + "\"");
		return (x.contains(compStr));
	}
	
	public String stripSpacesFromStr(String rawStr)
	{	return rawStr.replaceAll("\\s","");
	}

	public String returnFieldValue(String rawStr, String delimChar, int fieldInt)
	{	if (rawStr == null)	// simple error checking - bail if null is passed in.
		{	return "";
		}

		int subStringStart = 0;
		int subStringEnd = 1;
		String outputStr;
		
		String rawString = replaceQuotesInString(rawStr, "_");
		// 	this removes contents of any inverted comma
		//	Benefit is that any instance of the delim char within inverted commas is ignored.
		
			for (int i = 0; i < fieldInt-1; i++)	// here we find the fieldInt'th instance of the delim char:
			{	subStringStart = (rawString.indexOf(delimChar, subStringStart)+1);
			}
			subStringEnd  = rawString.indexOf(delimChar, subStringStart+1);
	//	if	(subStringEnd==-1) 	outputStr = rawString.substring(subStringStart);
	//	else					outputStr = rawString.substring(subStringStart, subStringEnd);
		
		// we've used rawString to find the delimiters... this has contents of inverted commas replaced with _
		// to ensure we don't count any commas inside these when looking for delimiters, BUT if there is a comma
		// then the likelihood is that the raw data is wrapped in inverted commas.  Given that we add
		// inverted commas when we print we need to check if outputStr has inverted commas before we return it:
			if	(subStringEnd==-1) 	outputStr = rawStr.substring(subStringStart);
			else					outputStr = rawStr.substring(subStringStart, subStringEnd);
		
			if(x_Contains_y(outputStr, INVERTED_COMMA)) outputStr = replaceQuotesWithBlank(outputStr); 
	
		return outputStr;
	}
	
	public String returnFieldValue(String rawString, int fieldInt)	// default delimiter is comma
	{	return returnFieldValue(rawString, ",", fieldInt);
	}

	public String returnKeyFieldValue(String rawString)		// default delimiter is comma
	{	return returnFieldValue(rawString, 1);				// default field is the first field
	}
	
	private String replaceQuotesInString(String rawStr, String replaceChar)
	// this method replaces any open / close inverted comma pair with the char passed in,
	// if "" is passed in the content is just stripped
	// Pass in a single char (e.g. "_") and the String length remains the same.
	{	if (rawStr == null)	// simple error checking - bail if null is passed in.
		{	
			return "";
		}
		
		String invertedComma = "\"";
		String bufString;
		
		int lastPosition = 0;		
		int firstInvertedComma = 0;
		int secondInvertedComma = 1; // = rawStr.indexOf(invertedComma, firstInvertedComma+1);
	
		bufString = new String("");
		while (firstInvertedComma > -1)
		{
			firstInvertedComma = rawStr.indexOf(invertedComma, lastPosition);
			
			if (firstInvertedComma == -1)			// no more inverted commas 	
			{	if (lastPosition<rawStr.length())	// checks for substring beyond last inverted commas
				{	bufString = bufString + rawStr.substring(lastPosition);
				}
			}
			else
			{	secondInvertedComma = rawStr.indexOf(invertedComma, firstInvertedComma+1);	
				if (secondInvertedComma > -1)
				{//	bufString = bufString + rawStr.substring(lastPosition, firstInvertedComma);
					bufString = bufString + rawStr.substring(lastPosition, firstInvertedComma);					

					for(int i = firstInvertedComma; i <= secondInvertedComma; i++)
					{	bufString = bufString + replaceChar;
					}	// replaces the inverted comma and contents with the specified char

					
					lastPosition = secondInvertedComma + 1;
				}
				else 	// this is reached if there is an imbalance of inverted commas
				{		firstInvertedComma= -1;		// here First > -1 (ie exists) but second = -1
				}									// there does not exist - so opening " is not closed
			}		
		}
		return bufString;	
	}
		
	private String replaceQuotesWithBlank(String rawS)
	{	return rawS.replaceAll(INVERTED_COMMA,"");
	}

	private String stripQuotesFromString(String rawStr)
	{	return replaceQuotesInString(rawStr, "_");		
	}
	
	private void testX_Contains_Y(String x, String y)	
	{	JU_main.writeToUI("Big Str = " + x + " ; small = " + y, false);
		JU_main.writeToUI(x_Contains_y(x, y) ? ("Contains"): ("NOT Contains"), false);
	}
	
	private void testStripQuotesFromString()	
	{
		JU_main.writeToUI(stripQuotesFromString("dummy\"other\""), false);
		JU_main.writeToUI(stripQuotesFromString("\"dummy\"other"), false);
		JU_main.writeToUI(stripQuotesFromString("dummy'other"), false);
		JU_main.writeToUI(stripQuotesFromString("\"dummy_othera"), false);
		JU_main.writeToUI(stripQuotesFromString("dummy\"other\"Something\"else\"over"), false);
		JU_main.writeToUI(stripQuotesFromString("dummy\"other\"Something\"else\"over\"and\"out"), false);
		JU_main.writeToUI(stripQuotesFromString("dummy\"other\"Something\"else\"over\"and\"out\""), false);
		// note that this last one is malformed - imbalance of quotes... this will create
		// problems - classic GIGO situation.
	}

	private void testReturnKeyFieldValue()
	{	JU_main.writeToUI(returnKeyFieldValue("111,2,3,4,5,6,7,8,9,10,11,12"), false);
		JU_main.writeToUI(returnFieldValue("1,2,3,4,5,6,7,8,9,10,11,12", 2), false);
		JU_main.writeToUI(returnFieldValue("1,2,3,4,5,6,7,8,9,10,11,12", 3), false);
		JU_main.writeToUI(returnFieldValue("111,222,333,444,555,6,7,8,9,10,11,12", 3), false);		
		JU_main.writeToUI(returnFieldValue("1,2,3,4,5,6,7,8,9,10,11,12", 4), false);	
		JU_main.writeToUI(returnFieldValue("1;2;3;4;5;6;7;8;9;10;11;12", ";", 3), false);	
	}
}