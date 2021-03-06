package com.proginternet.ui.login;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.proginternet.models.User;
import com.proginternet.utils.Auth;
import com.proginternet.utils.Singleton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class FXMLLoginController {
	
    @FXML private Text loginMsg;
    @FXML private PasswordField passwordField;
	@FXML private TextField usernameField;
	@FXML private Button loginButton;
    
    private String password = "";
    private Integer trials = 0;
    
    @FXML protected void checkLogin(ActionEvent event) {
		this.trials++;
		User user = User.checkUsername(usernameField.getText());

		try {
			if (user != null) {
				this.password = user.getPassword();

				if (Auth.validatePassword(passwordField.getText(), this.password)) {
					sendData(user);
					FXMLLoader loader = new FXMLLoader(getClass().getResource("./../workspace/workspace.fxml"));
					Parent home = loader.load();	
					Scene homeScene = new Scene(home, 1280, 720);	
					Stage stage = (Stage) loginButton.getScene().getWindow();
					stage.setScene(homeScene);
					stage.show();
				}
				else {
					loginMsg.setText("Forgot your password? You tried "  + this.trials + " times...");
					loginMsg.setFill(Color.RED);
				}

			} else {
				loginMsg.setText("Forgot your password? You tried "  + this.trials + " times...");
				loginMsg.setFill(Color.RED);
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
			e.printStackTrace();
		}

	}

	private void sendData(User user) {
		Singleton holder = Singleton.getInstance();
		holder.setUser(user);
	}
}