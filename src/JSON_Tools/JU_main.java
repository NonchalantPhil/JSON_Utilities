package JSON_Tools;

public class JU_main 
{	static StringTools stringTools;			// needed for various operations throughout
	static JT_UserInputs userInputs;		// passed to UI so that user inputs can be added
	static UI_MainFrame userInterface;		// from this we will have access to user inputs
	
	public static int CONVERT_ID = 1;		// for consistent and easily readabke code
	public static int MERGE_ID = 2;
	
	public JU_main() 
	{	//
	}

	public static void main(String[] args) 			
	{	stringTools = new StringTools();
		userInputs = new JT_UserInputs();
		
		// next we see if the user wants a UI (args empty) or is passing in a command line parameter set:
		if(args.length == 0)
		{	userInterface = new UI_MainFrame();
			userInterface.setVisible(true);					// show the UI and effectively pass control there
		}
		else
		{	// args non empty means app has been called from command line.
			// due to the plethora of inputs required to get this running the only parameter passed in is a full
			// path file and name pointing to a file.  All required inputs are in the file.
			RunFromCommandLine runFromCommandLine = new RunFromCommandLine(args[0]);
		}
	}
	
	public static StringTools getStringTools()			// 	JU_main.getStringTools()
	{	return stringTools;		
	}
	
	public static JT_UserInputs getJT_UserInputs()		//	JU_main.getJT_UserInputs()
	{	return userInputs;		
	}
	
	public static void writeToUI(String str, boolean b)	// 	JU_main.writeToUI(str, false)	
	// by locating this here anywhere can write to UI through this static method.
	{	userInterface.getManagementPanel().writeToManagementOutput(str, b);		
	}
}
