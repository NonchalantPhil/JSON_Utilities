package JSON_Tools;

import javax.swing.*;
import java.awt.BorderLayout;
	
class UI_MainFrame extends JFrame						// MainFrame is a container for mainPan, which contains Inputpanel and ImagePanel, 
{	JPanel mainPanel;
		UI_InputPanel inputPanel;					// where all user inputs are gathered
		UI_ManagementPanel managementPanel;			// shows the status of the app and has Exit button

	public UI_MainFrame()
	{	super("JSON Utilities");					// title for the frame
		mainPanel = new JPanel();					// will contain all other panels
			inputPanel = new UI_InputPanel();
			managementPanel = new UI_ManagementPanel();
				
			mainPanel.setLayout(new BorderLayout(5,5));
			mainPanel.add(inputPanel, BorderLayout.CENTER);
			mainPanel.add(managementPanel, BorderLayout.SOUTH);
			
		getContentPane().add(mainPanel);
	
		setLocation(0,0); 	
		setSize(780,720);			// sets size for when OS window resize button is clicked - app starts fullscreen:
		//setSize(800,580);			// NOTE: add 70 to second param for every new JPanel added
		setResizable(false);
	}
	
	public UI_ManagementPanel getManagementPanel()
	{	return managementPanel;	
	}

}