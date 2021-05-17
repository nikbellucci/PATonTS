package com.proginternet.ui.registration;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class FXMLRegistrationController {
	
    @FXML private Text loginMsg;
    @FXML private PasswordField passwordField;
	@FXML private TextField nameField;
	@FXML private TextField surnameField;
	@FXML private TextField usernameField;
	@FXML private DatePicker birthdayField;
    
    @FXML protected void signIn(ActionEvent event) throws Exception{
		
    		
    }

    @FXML protected void showHelp(ActionEvent event) throws IOException {
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("./../login/login.fxml"));

    	Parent login = loader.load();
    		
    	Scene loginScene = new Scene(login, 1280, 720);
    		
    	Stage stage = (Stage) loginMsg.getScene().getWindow();
    		
    	stage.setScene(loginScene);
    		
        stage.show();
    }
}
