package com.proginternet.ui.registration;

import java.io.IOException;
import java.util.ArrayList;

import com.proginternet.models.User;
import com.proginternet.utils.Auth;
import com.proginternet.utils.JsonParser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class FXMLRegistrationController {
	
    @FXML private Text registrationMsg;
    @FXML private PasswordField passwordField;
	@FXML private TextField nameField;
	@FXML private TextField surnameField;
	@FXML private TextField usernameField;
	@FXML private DatePicker birthdayField;
	@FXML private CheckBox isAdminCheck;
    
    @FXML protected void signIn(ActionEvent event) throws Exception {

		String filename = "data/Users.json";
		JsonParser<User> parser = new JsonParser<User>();
		ArrayList<User> users = parser.readOnJson(filename, User[].class);

		if(!User.userExist(usernameField.getText())) {
			User newUser = new User(nameField.getText(), surnameField.getText(), usernameField.getText(), Auth.generateStorngPasswordHash(passwordField.getText()), birthdayField.getValue(), isAdminCheck.isSelected());
			users.add(newUser);
			System.out.println(newUser);
			parser.writeOnJson(filename, users);
			registrationMsg.setText("Registration successfull!");
    		registrationMsg.setFill(Color.GREEN);
		} else {
			registrationMsg.setText("Username already exist, change your username and retry.");
    		registrationMsg.setFill(Color.RED);
		}
    		
    }

    @FXML protected void showWorkspace(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("./../workspace/workspace.fxml"));
    	Parent workspace = loader.load();	
    	Scene workspaceScene = new Scene(workspace, 1280, 720);	
    	Stage stage = (Stage) registrationMsg.getScene().getWindow();	
    	stage.setScene(workspaceScene);	
        stage.show();
    }
}
