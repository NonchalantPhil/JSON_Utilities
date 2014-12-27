package JSON_Tools;

import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.lang.NullPointerException;

public class WriteToFile 
{	
	protected String filePath;
	private FileWriter fileWriter;
	private boolean appendCopy = true;
    private BufferedWriter bufWriter;
	private PrintWriter printWriter;
	
	public WriteToFile(String path, String name)
	{	appendCopy = false;							// true means lines are added to file (false clears out file before it is written to)
		filePath = (combinePathAndName(path, name));
		
		if (!openFile()) closeFiles();
	}

	private String combinePathAndName(String path, String name)
	{	return (path + "/" + name + ".JSON");		
	}
	
	protected void writeString(String rowStr)
	{	if(!(rowStr == null))
			printWriter.println(rowStr);
	}
	
	private boolean openFile()									// TODO: clear out file if already exists
	{	try
		{	fileWriter = new FileWriter(filePath, appendCopy);
			bufWriter = new BufferedWriter(fileWriter);
			printWriter = new PrintWriter(bufWriter, true);

			return true;
		}
		catch(IOException io)
		{	return false;
		}
	}
	
	public void closeFiles()
	{	try
		{	fileWriter.close();
			printWriter.close();
		}
		catch(IOException io)
		{	JU_main.writeToUI("Unable to close write file objects - terminating", false);
			System.exit(0);
		}
	}
}
