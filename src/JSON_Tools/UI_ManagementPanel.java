package JSON_Tools;

import javax.swing.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.Color;


public class UI_ManagementPanel extends JPanel implements ActionListener
{	int buttonClicked;			// Records which measure is being taken.
	JTextArea textArea;			// Records the session activities.
	
	private static String BUT_LABEL_EXIT = "Exit";
	private static String BUT_LABEL_RESET = "Reset watch window";
	private static String BUT_LABEL_SHOW_MASTER_HEADINGS = "Show master headings";
	private static String BUT_LABEL_SHOW_SUPPLY_HEADINGS = "Show supply headings";
	
	private static int SHOW_MASTER_HEADINGS = 1;
	private static int SHOW_SUPPLY_HEADINGS = 2;
	
	public UI_ManagementPanel()
	{	setBorder((new UI_Border()).getOutsideBorder());
		setLayout(new GridLayout(1,2,5,5));	// formatted panels are added rather than individual widgets
		setBackground(new Color(255,255,255));

		add(formatPanels(makeMiddlePanel(new JPanel()), 2, 1));	// the 2 panels.
		add(formatPanels(makeBottomPanel(new JPanel()), 1, 1));
	}


	private JPanel makeBottomPanel(JPanel bottomPanel)
	{	textArea = new JTextArea("SESSION RECORD");	
			JScrollPane scrollPane = new JScrollPane(textArea);
				textArea.setLineWrap(true);
				textArea.setEditable(false);	// only application can write to this widget
		bottomPanel.add(scrollPane );
		return bottomPanel;
	}

	private JPanel makeMiddlePanel(JPanel middlePanel)
	{	JPanel buttonContainer = new JPanel();
			buttonContainer.add(makeNewButton(BUT_LABEL_SHOW_MASTER_HEADINGS));
			buttonContainer.add(makeNewButton(BUT_LABEL_SHOW_SUPPLY_HEADINGS));
			buttonContainer.add(makeNewButton(BUT_LABEL_RESET));
			buttonContainer.add(makeNewButton(BUT_LABEL_EXIT));
			middlePanel.add(formatPanels(buttonContainer, 2, 2));
		return middlePanel;
	}

	private JButton makeNewButton(String buttonLabel)
	{	JButton butBuf = new JButton(buttonLabel);
			butBuf.addActionListener(this);
		return butBuf;
	}

	
	private JPanel formatPanels(JPanel rawPanel, int rows, int cols)		// used for the 3 main panels added here
	{	rawPanel.setBorder(new UI_Border().getInsideBorder());
		rawPanel.setLayout(new GridLayout(rows,cols,2,2));
		rawPanel.setBackground(new Color(200,200,200));
		return rawPanel;
	}

	public void updateSessionRecord(String actionStr)
	{//	textArea.setText(textArea.getText() + "\n" + actionStr);
		textArea.append(textArea.getText() + "\n" + actionStr);
	}

	public void actionPerformed(ActionEvent ae)
	{	String buttonStr  = ((JButton)(ae.getSource())).getText();

		if (buttonStr.equals(BUT_LABEL_EXIT))	System.exit(0);
		if (buttonStr.equals(BUT_LABEL_RESET)) resetSessionRecord();
		if (buttonStr.equals(BUT_LABEL_SHOW_MASTER_HEADINGS)) showHeadings(SHOW_MASTER_HEADINGS);
		if (buttonStr.equals(BUT_LABEL_SHOW_SUPPLY_HEADINGS)) showHeadings(SHOW_SUPPLY_HEADINGS);
	}
	
	private void showHeadings(int showWhich)
	{	String pathBuffer = ""; 
	
		if (showWhich == SHOW_MASTER_HEADINGS)
			pathBuffer = JU_main.getJT_UserInputs().getMasterFilePath();
		
		if (showWhich == SHOW_SUPPLY_HEADINGS)
			pathBuffer = JU_main.getJT_UserInputs().getSupplyFilePath();
		
			if (JU_main.getJT_UserInputs().hasValidHeaderFile(showWhich)) 
			{	showFileHeadings(pathBuffer);
			}
			else
			{	writeToManagementOutput("Input file path not specified", false);
			}
	}
	
	
	private void showFileHeadings(String pathBuffer)
	{	CSV_Headings csv_Headings = new CSV_Headings(pathBuffer);
		
		String headingStrBuf;
		int printInt;
		
		resetSessionRecord();
		
		if (csv_Headings.getNumberOfFields() < 2)
		{
			textArea.append("Input file invalid");
		}
		else
		{
			for (int i = 0; i < csv_Headings.getNumberOfFields(); i++)
			{	headingStrBuf = csv_Headings.getHeadingAt(i);
			
				printInt = i + 1;
				textArea.append("Field " + printInt + " = " + headingStrBuf + "\n");
			}
		}
	}
	
	public void writeToManagementOutput(String str, boolean bool)
	{	if (bool == true) resetSessionRecord();
		textArea.append("\n" + str);
	}
	
	private void resetSessionRecord()
	{	textArea.setText("");
	}
}

