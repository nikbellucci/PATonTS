package com.proginternet.ui.login;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.proginternet.models.User;
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
    
    @FXML protected void checkLogin(ActionEvent event) {
		this.trials++;

		User user = User.checkUsername(usernameField.getText());

		try {
			this.password = user.getPassword();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        try {
			if (Auth.validatePassword(passwordField.getText(), this.password)) {
				loginMsg.setText("Login successfull!");
				loginMsg.setFill(Color.GREEN);
			}
			else {
				loginMsg.setText("Forgot your password? You tried "  + this.trials + " times...");
				loginMsg.setFill(Color.RED);
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
    		
    }
	// dopo login apre workspace

    /***
	 * 
	 * View/Controller multipli nella stessa applicazione
	 * 
	 * Metodo che carica un'altra vista e trasferisce dati
	 * 
	 */
    @FXML protected void showRegistration(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("./../registration/registration.fxml"));
    	Parent login = loader.load();	
    	Scene loginScene = new Scene(login, 1280, 720);	
    	Stage stage = (Stage) loginMsg.getScene().getWindow();
    	stage.setScene(loginScene);
        stage.show();
    }
}
