package com.proginternet.ui.login;

import java.io.IOException;

import com.proginternet.models.User;
import com.proginternet.ui.help.FXMLHelpController;
import com.proginternet.utils.Auth;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class FXMLLoginController {
	
    @FXML private Text loginMsg;
    @FXML private PasswordField passwordField;
	@FXML private TextField usernameField;
    
    private String password = "";
    private Integer trials = 0;
    
    @FXML protected void checkLogin(ActionEvent event) throws Exception{
		this.trials++;

		User user = User.checkUsername(usernameField.getText());

		this.password = user.getPassword();
        
    	if (Auth.validatePassword(passwordField.getText(), this.password)) {
    		loginMsg.setText("Login successfull!");
    		loginMsg.setFill(Color.GREEN);
    	}
    	else {
    		loginMsg.setText("Forgot your password? You tried "  + this.trials + " times...");
    		loginMsg.setFill(Color.RED);
		}
    		
    }

    /***
	 * 
	 * View/Controller multipli nella stessa applicazione
	 * 
	 * Metodo che carica un'altra vista e trasferisce dati
	 * 
	 */
    @FXML protected void showHelp(ActionEvent event) throws IOException {
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("./../help/help.fxml"));

    	Parent help = loader.load();
    		             
		FXMLHelpController helpController = loader.getController();
    		
    	Scene helpScene = new Scene(help, 300, 300);
    		
    	helpController.copyTrials(trials);
    		
    	Stage stage = (Stage) loginMsg.getScene().getWindow();
    		
    	stage.setScene(helpScene);
    		
        stage.show();
    }
}
