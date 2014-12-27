package JSON_Tools;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class ReadFile 
{	protected String filePath;
	FileReader fileReader;
	BufferedReader lineReader;
	String lineStringBuf;
	boolean readSuccessful;
	boolean fileNotEOF = true;

	public ReadFile(String path)
	{	readSuccessful = true;
		filePath = new String(path);
		if (!openSourceFile()) 
		{	readSuccessful = false;
		}
		readLine();
	}

	public boolean notEOF()		// returns true until EOF hit, then false
	{	return fileNotEOF;
	}
	
	public String readLine()
	{
		try
		{	lineStringBuf = lineReader.readLine();
			fileNotEOF = !(lineStringBuf == null);
		}
		catch (IOException io)
		{	readSuccessful = false;
			lineStringBuf = "ERROR";
		}

		return lineStringBuf;
	}
	
	private boolean openSourceFile()
	{	try
		{	fileReader = new FileReader(filePath);
			lineReader = new BufferedReader(fileReader);
			return true;
		}	
		catch (IOException io)
		{	readSuccessful = false;
			closeStream();
			return false;
		}
	}
	
	public void closeStream()
	{	try
		{	fileReader.close();
			lineReader.close();
		}
		catch (IOException io)
		{	//
		}
	}

	public String getLineString()		// note - this one doesn't instigate a read (unline ReadLine)
	{	return lineStringBuf;
	}
	
	public boolean getReadSuccess()
	{	return readSuccessful;		
	}
	
	public String getFilePath()
	{	return filePath;
	}
	
}
