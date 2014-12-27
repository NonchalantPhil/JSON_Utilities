package JSON_Tools;

import javax.swing.*;

import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.Color;

public class UI_InputPanel extends JPanel 
{	// this panel will contain a variety of smaller panels... each smaller panel will
	// hold up to 3 items (typically label - input field - action button or
	// label - input field - text field (where the latter includes instructions)
	
	// When a parameters is set by user input it is saved to this object:
	JT_UserInputs userInputObj;
	
	// Below are a bunch of constants to make code more readable:
	private final int PANEL_TYPE_BUTTON = 1;
	private final int PANEL_TYPE_TEXT_INPUT = 2;
	private final int PANEL_TYPE_VALUE_PAIR = 3;
	
	private final String KEY_FIELD_LABEL = "Enter the name of the key field:";
		private final String KEY_FIELD_DESCRIPTION = "Mandatory for Convert and Merge";
	private final String KEY_COLUMN_BOUNDS_LABEL = "Key field start and end columns:";
		private final String KEY_COLUMN_BOUNDS_DESCRIPTION = "Mandory for Convert, not used in Merge";
	private final String LEVEL2_SET1_LABEL = "Enter name of the secondary data set:";
		private final String LEVEL2_SET1_DESCRIPTION = "Optional in Convert, Mandatory for Merge";
	private final String LEVEL2_SET1_COLUMN_BOUNDS_LABEL = "Secondary data field start and end cols:";
		private final String LEVEL2_SET1_COLUMN_BOUNDS_DESCRIPTION = "Mandory for Convert, not used in Merge";
	
	private final String MASTER_FILE_LOCATION_LABEL = "Source csv / master JSON file:";
		private final String MASTER_FILE_LOCATION_BUTTON_TXT = "Find source csv / master JSON file";
	private final String SUPPLY_FILE_LOCATION_LABEL = "Supply JSON file:";
		private final String SUPPLY_FILE_LOCATION_BUTTON_TXT = "Find supply JSON file";
	private final String SAVE_LOCATION_LABEL = "Folder to save JSON file to:";
		private final String SAVE_LOCATION_BUTTON_TXT = "Find output folder";
		
	private final String SAVE_FILE_NAME_LABEL = "JSON output file name:";
		private final String SAVE_FILE_NAME_DESCRIPTION = "Enter the name of the JSON output file";
	
	private final String LEVEL2_SET2_LABEL = "Second level (set 2) field title:";
	private final String LEVEL2_SET2_COLUMN_BOUNDS_LABEL = "Second level (set 2) field start and end cols:";
	
	private final String INTRO_COPY_LINE1 = 		"To Convert fill in all fields colour coded BLUE and RED";
	private final String INTRO_COPY_LINE2 = 		"To Merge fill in all fields colour coded BLUE and GREEN";
	
	private final String MASTER_FILE_HELP_COPY = 	"Mandatory for Convert AND Merge. " +
													"Convert: locate csv file to convert. " +
													"Merge: locate JSON file that supply data is added to.";
	
	private final String SUPPLY_FILE_HELP_COPY = 	"Mandatory for Merge ONLY. " +
													"Locate JSON file containing the data " +
													"that you want to merge into the master JSON file.";

	private final Color COLOR_BOTH_BLUE = new Color(0,0,152);
	private final Color COLOR_CONVERT_RED = new Color(152,0,0);
	private final Color COLOR_MERGE_GREEN = new Color(0,152,0);
	
	public UI_InputPanel() 
	{	// This method creates each widget panel and adds it and a partner to a panel. 
		userInputObj = JU_main.getJT_UserInputs();
	
		formatJPanel(this, true, 7,1);		// boolean = show border, ints are rows + cols
		setBorder((new UI_Border()).getOutsideBorder());

		makeAndAddContainingPanels
		(	new HelpPanel	(INTRO_COPY_LINE1, Color.DARK_GRAY),
			new HelpPanel	(INTRO_COPY_LINE2, Color.DARK_GRAY)
		);
		
		makeAndAddContainingPanels
		(	new WidgetPanel(SAVE_LOCATION_LABEL, "b", PANEL_TYPE_BUTTON, SAVE_LOCATION_BUTTON_TXT, COLOR_BOTH_BLUE),
			new WidgetPanel(SAVE_FILE_NAME_LABEL, SAVE_FILE_NAME_DESCRIPTION, PANEL_TYPE_TEXT_INPUT, "", COLOR_BOTH_BLUE)
		);
		
		makeAndAddContainingPanels	
		(	new WidgetPanel(KEY_FIELD_LABEL, KEY_FIELD_DESCRIPTION, PANEL_TYPE_TEXT_INPUT, "",COLOR_BOTH_BLUE), 
			new WidgetPanel(LEVEL2_SET1_LABEL, LEVEL2_SET1_DESCRIPTION, PANEL_TYPE_TEXT_INPUT, "", COLOR_BOTH_BLUE)
		);

		//makeAndAddContainingPanels
		//(		new WidgetPanel(LEVEL2_SET2_LABEL, "", PANEL_TYPE_TEXT_INPUT, ""),
		//		new WidgetPanel(LEVEL2_SET2_COLUMN_BOUNDS_LABEL, "", PANEL_TYPE_VALUE_PAIR, "")
		//);

		makeAndAddContainingPanels
		(	new HelpPanel	(MASTER_FILE_HELP_COPY, COLOR_BOTH_BLUE),
			new WidgetPanel	(MASTER_FILE_LOCATION_LABEL, "b", PANEL_TYPE_BUTTON, MASTER_FILE_LOCATION_BUTTON_TXT, COLOR_BOTH_BLUE)
		);
		makeAndAddContainingPanels	
		(	new WidgetPanel(KEY_COLUMN_BOUNDS_LABEL, KEY_COLUMN_BOUNDS_DESCRIPTION, PANEL_TYPE_VALUE_PAIR, "", COLOR_CONVERT_RED),
			new WidgetPanel(LEVEL2_SET1_COLUMN_BOUNDS_LABEL, LEVEL2_SET1_COLUMN_BOUNDS_DESCRIPTION, PANEL_TYPE_VALUE_PAIR, "", COLOR_CONVERT_RED)
		);
		makeAndAddContainingPanels
		(	new HelpPanel	(SUPPLY_FILE_HELP_COPY, COLOR_MERGE_GREEN),
			new WidgetPanel	(SUPPLY_FILE_LOCATION_LABEL, "b", PANEL_TYPE_BUTTON, SUPPLY_FILE_LOCATION_BUTTON_TXT,COLOR_MERGE_GREEN)
		);
		
		// finally a single button panel is added at the bottom of the uber panel (this):
		add(new ButtonPanel());
	}

	private void makeAndAddContainingPanels(JPanel pan1, JPanel pan2)
	{	// Here the 2 widget panels are added to a container which is added to the single uber panel (this):
		JPanel panelBuf = new JPanel();
			formatJPanel(panelBuf, false, 2,1);
			panelBuf.setBorder((new UI_Border()).getInsideBorder());
			panelBuf.add(pan1);
			panelBuf.add(pan2);
		add(panelBuf);
	}
	
	private void formatJPanel(JPanel pan, boolean showBorder, int x, int y)
	{		formatJPanel(pan, showBorder, x, y, new Color(255,255,255));	// default color is white
	}

	private void formatJPanel(JPanel pan, boolean showBorder, int x, int y, Color color)
	{	//	if (showBorder) pan.setBorder((new UI_Border()).getOutsideBorder());
			pan.setLayout(new GridLayout(x, y, 5, 5));
			pan.setBackground(color);
	}	
	
	private class HelpPanel extends JPanel
	{	// this class is used to add a JLabel to a JPanel - add additional copy to a widget panel to
		// provide additional info to the user.
		JLabel labelBuffer;
		
		public HelpPanel(String helpCopy, Color copyColour)
		{	labelBuffer = new JLabel(helpCopy);
				labelBuffer.setForeground(copyColour);
				labelBuffer.setHorizontalAlignment(SwingConstants.CENTER);
			add(labelBuffer);
			formatJPanel(this, true, 1, 1);
		}
	}
	
	private class ButtonPanel extends JPanel implements ActionListener
	{	JButton convertToCSV;
		JButton mergeJSON;
	//	JButton splitJSON;
		
		public ButtonPanel()
		{	JPanel buttonContainer = new JPanel();
				formatJPanel(this, true, 1, 1);
			
			convertToCSV = new JButton("Convert csv to JSON");
				convertToCSV.addActionListener(this);
				
			mergeJSON  = new JButton("Merge 2 JSON files");
				mergeJSON.addActionListener(this);
			
			buttonContainer.add(convertToCSV);
			buttonContainer.add(mergeJSON);
			
			add(buttonContainer);
		}
	
		public void actionPerformed(ActionEvent ae)
		{	String buttonStr  = ((JButton)(ae.getSource())).getText();

			if (buttonStr.equals("Convert csv to JSON"))
			{	// below we check validity of data sets before calling static convert method:
				if (userInputObj.isOKToConvert())
				{	ConvertToJSON convertToJSON = new ConvertToJSON();
					JU_main.writeToUI("Conversion successful.", false);
				}
				else
				{	JU_main.writeToUI("Unable to convert - incomplete input set - please try again ", false);					
				}
			}
						
			if (buttonStr.equals("Merge 2 JSON files"))	// WiP
			{
				if (userInputObj.isOKToMerge())
				{	MergeJSON mergeJSON = new MergeJSON();
					JU_main.writeToUI("Merge successful.", false);
				}
				else
				{	JU_main.writeToUI("Unable to merge - incomplete input set - please try again ", false);					
				}
			}
		}
	}
	
	private class WidgetPanel extends JPanel implements ActionListener, FocusListener
	{	// This class is the basic building block of the UI.  It is a 3x1 matrix containing 3
		// panels.  Each panel contains a UI unit (button, label, textfield).		
		
		JTextField secondLab;		// 	label that is updated when user selects a file 
									//	declared at class level for easy reference. 
		
		public WidgetPanel(String labelStr, String descriptionStr, int panelType, String buttonLabel, Color txtColor)
		{	formatJPanel(this, true, 1, 3);	// formatted panels are added rather than individual widgets
			
			if (panelType == PANEL_TYPE_BUTTON)
			{	makeWidgetButtonPanel(labelStr, buttonLabel, txtColor);	}
			else if (panelType == PANEL_TYPE_TEXT_INPUT)
			{	makeWidgetTextFieldPanel(labelStr, descriptionStr, txtColor);	}
			else if (panelType == PANEL_TYPE_VALUE_PAIR)
			{	makeWidgetValuePairPanel(labelStr, descriptionStr, txtColor);	}
		}
	
		private void makeWidgetValuePairPanel(String labelStr, String descriptionStr, Color txtCol)
		{	// used to allow user to specify the start and end cols containing a data set (in the csv input
			
			//	ITEM 1: A label shown, on left, describing the required input:
				JLabel labelBuf = new JLabel(labelStr);
					formatLabel(labelBuf, txtCol, SwingConstants.RIGHT);
					
			//	ITEM 2 has 2 parts.  We create container to put them:
				JPanel panBuf = new JPanel();
					formatJPanel(panBuf, false,1,1);	// formatting for the value entry fields	
				//	ITEM 2.1: first text input box (for first col containing data):
					JTextField	txt1 = new JTextField();
						txt1.addFocusListener(this);
						txt1.setName(labelStr + "_1");		// allows us to ID field / value pairs
			
				//	ITEM 2.2: second text input box (for last col containing data):	
					JTextField txt2 = new JTextField();
						txt2.addFocusListener(this);	
						txt2.setName(labelStr + "_2");		//  allows us to ID field / value pairs
						
					panBuf.add(txt1);
					panBuf.add(txt2);
				
			//	ITEM 3 is another label with more info about the field set (atm info on mandatory nature)
				JLabel descLab = new JLabel(descriptionStr);
					formatLabel(descLab, txtCol, SwingConstants.CENTER);
			
			// And finally add them all to the Widget Panel (i.e. this):
			add(labelBuf);			
			add(panBuf);
			add(descLab);	
		}
		
		private void makeWidgetButtonPanel(String lab, String butLabel, Color txtCol)
		{	// used to allow user to identify files or folders (ultimately open JFileChoosers)
			
			//	ITEM 1: A label shown, on left, describing the required input:
				JLabel firstLab = new JLabel(lab);
					formatLabel(firstLab, txtCol, SwingConstants.RIGHT);

			// ITEM 2: JPanel containing a text field (adding to JPanel ensures textbox sits within Panel (not flush)
				JPanel txtContainer = new JPanel();
					formatJPanel(txtContainer, false, 1,1);
					secondLab = new JTextField();
				txtContainer.add(secondLab);
			
			// ITEM 3: JPanel containing a button (adding to JPanel ensures button sits within Panel (not flush)
				JPanel buttonContainer = new JPanel();
					formatJPanel(buttonContainer, false, 1,1);
					JButton butt = new JButton(butLabel);
						butt.setText(butLabel);
						butt.addActionListener(this);
						butt.setForeground(txtCol);
					buttonContainer.add(butt);

			// And finally add them all to the Widget Panel (i.e. this):						
			add(firstLab);
			add(txtContainer);
			add(buttonContainer);			
			
		}
			
		private void makeWidgetTextFieldPanel(String labelStr, String descriptionStr, Color txtCol) 
		{	// used to allow user to identify files or folders (ultimately open JFileChoosers)
			
			//	ITEM 1: A label shown, on left, describing the required input:
			JLabel mainLabel = new JLabel(labelStr);			// TODO: run this through a formatter.
				formatLabel(mainLabel, txtCol, SwingConstants.RIGHT);

			// ITEM 2: JPanel containing a text field (adding to JPanel ensures textbox sits within Panel (not flush)
			JPanel panBuf = new JPanel();
				formatJPanel(panBuf, false,1,1);
				JTextField txt1 = new JTextField();
					txt1.addFocusListener(this);
					txt1.setName(labelStr);
					txt1.setText("organisationID services");	// TODO: remove in production (or have a better default)
				panBuf.add(txt1);
		
			//	ITEM 3 is another label with more info about the field set (atm info on mandatory nature)	
				JLabel descLabel = new JLabel(descriptionStr);
					formatLabel(descLabel, txtCol,SwingConstants.CENTER);
				
			// And finally add them all to the Widget Panel (i.e. this):	
			add(mainLabel);			
			add(panBuf);	
			add(descLabel);		
		}
		
		public void actionPerformed(ActionEvent ae)
		{	String buttonStr  = ((JButton)(ae.getSource())).getText();
			if (buttonStr.equals(MASTER_FILE_LOCATION_BUTTON_TXT))
			{	userInputObj.setMasterFilePath(getFilePathAndName());
				secondLab.setText(userInputObj.getMasterFilePath());		// Adds selected path to label2
			}
			if (buttonStr.equals(SAVE_LOCATION_BUTTON_TXT))
			{	userInputObj.setOutputFilePath(getFilePath());
				secondLab.setText(userInputObj.getOutputFilePath());		// Adds selected path to label2
			}
			if (buttonStr.equals(SUPPLY_FILE_LOCATION_BUTTON_TXT))
			{	userInputObj.setSupplyFilePath(getFilePathAndName());
				secondLab.setText(userInputObj.getSupplyFilePath());		// Adds selected path to label2
			}
		}
		
		private String getFilePath()					// returns just a folder location - used for output file
		{   try 	
			{	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch(Exception ex) 	
			{	JU_main.writeToUI("System error on folder choice: " + ex.toString(), false);
			}							// above code borrowed - makes file chooser look like OS one
		
			JFileChooser jfc = new JFileChooser(".");
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (jfc.showOpenDialog(this) == 0)	// cancel = 1, load = 0
			{	JU_main.writeToUI("Folder selected = " + jfc.getSelectedFile().getPath(), false);
				return (jfc.getSelectedFile().getPath());
			}
			else return "";		// BUT - this clears the field if file chooser is cancelled.
		}
		
		private String getFilePathAndName()		// returns full path and file name
		{   try 	
			{	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    	}
			catch(Exception ex) 	
			{	JU_main.writeToUI("System error (unable to format file chooser): " + ex.toString(), false);
			}							// above code borrowed - makes file chooser look like OS one
			
			JFileChooser jfc = new JFileChooser(".");
			if (jfc.showOpenDialog(this) == 0)	// cancel = 1, load = 0
			{	return (jfc.getSelectedFile().getPath());
			}
			else return "";		// BUT - this clears the field if file chooser is cancelled.
		}
	
		private void formatLabel(JLabel lab, Color txtColor, int txtAlign)
		{	lab.setHorizontalAlignment(txtAlign);
			lab.setForeground(txtColor);
		}
		
		public void focusLost(FocusEvent event) 
		{	JTextField txtField = ((JTextField)(event.getSource()));	// only JTFs have the listeners
			
			String inputStr  = txtField.getText();						// user entered values
			String valueNameLabel = txtField.getName();					// assigned name of the JTF
			int intBuf = 0;												// to cast user entered ints 

			try			// Note: String only allowed in Switch since 1.7 - avoiding for compatibility:	
			{					
				if (valueNameLabel.contains(KEY_FIELD_LABEL))
				{	userInputObj.setKeyFieldStr(inputStr);
				}
				else if (valueNameLabel.contains(LEVEL2_SET1_LABEL))
				{	userInputObj.setLevel21KeyFieldStr(inputStr);
				}
				else if (valueNameLabel.contains(SAVE_FILE_NAME_LABEL))
				{	userInputObj.setOutputFileName(inputStr);
				}

				else						// set params for the Column bounds (requires a cast)
				{	intBuf = userInputObj.castToInt(inputStr);					

					if 	(valueNameLabel.contains(KEY_COLUMN_BOUNDS_LABEL))
					{
						if 	((valueNameLabel.substring(valueNameLabel.length()-1)).equals("1"))
						{	userInputObj.setKeyFieldStartCol(intBuf);		}
						else
						{	userInputObj.setKeyFieldEndCol(intBuf);		}	
					}
					else if (valueNameLabel.contains(LEVEL2_SET1_COLUMN_BOUNDS_LABEL))
					{
						if ((valueNameLabel.substring(valueNameLabel.length()-1)).equals("1"))
						{	userInputObj.setLevel21FieldStartCol(intBuf);	}
						else
						{	userInputObj.setLevel21FieldEndCol(intBuf);	}
					}
				}
			}
			catch(NullPointerException np)
			{	// probably load error or suchlike - ignore!
			}
		}
		
		public void focusGained(FocusEvent event) {}

	}
}
