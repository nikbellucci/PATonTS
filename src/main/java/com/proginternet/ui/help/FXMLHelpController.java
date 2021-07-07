package com.proginternet.ui.help;

import java.io.IOException;

import com.proginternet.models.User;
import com.proginternet.ui.login.FXMLLoginController;
import com.proginternet.utils.Singleton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
 
public class FXMLHelpController {
	
    @FXML private Label trialsMsg;
	private User user = null;
    
	public void copyTrials(Integer trials){
		trialsMsg.setText("You tried " + trials + " times in the login window...");
	}

	@FXML protected void showLogin(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("./../login/login.fxml"));

		Parent login = loader.load();
		FXMLLoginController loginController = loader.getController();
		Scene loginScene = new Scene(login, 1280, 720);
		Stage stage = (Stage) trialsMsg.getScene().getWindow();
		stage.setScene(loginScene);
		stage.show();
	}

	@FXML private void receiveData(ActionEvent event) {
        Singleton holder = Singleton.getInstance();
        this.user = holder.getUser();
		System.out.println(this.user);
    }
}
