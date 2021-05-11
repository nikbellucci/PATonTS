package com.proginternet.ui.help;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
 
public class FXMLHelpController {
	
    @FXML private Label trialsMsg;
    
	public void copyTrials(Integer trials){
		trialsMsg.setText("You tried " + trials + " times in the login window...");
	}
}
