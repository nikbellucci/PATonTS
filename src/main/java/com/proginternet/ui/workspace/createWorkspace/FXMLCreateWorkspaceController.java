package com.proginternet.ui.workspace.createWorkspace;


import java.io.IOException;
import java.util.ArrayList;

import com.proginternet.models.Workspace;
import com.proginternet.utils.JsonParser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FXMLCreateWorkspaceController {

    @FXML private Text creationMsg;
	@FXML private TextField nameField;
	@FXML private TextField categoryField;
	@FXML private TextField usernameField;
    @FXML private TextField idField;
	@FXML private DatePicker expirationField;
    @FXML private TextArea descriptionField;
    @FXML private Button backButton;

    @FXML public void initialize() {
        
    }

    @FXML public void create(ActionEvent event) {
        String filename = "data/Workspace.json";
		JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> workspaces = parser.readOnJson(filename, Workspace[].class);
        Workspace newWksp = new Workspace(idField.getText(), nameField.getText(), descriptionField.getText(), categoryField.getText(), expirationField.getValue());
        workspaces.add(newWksp);
        parser.writeOnJson(filename, workspaces);
		creationMsg.setText("Registration successfull!");
    	creationMsg.setFill(Color.GREEN);
    }

    @FXML protected void showWorkspace(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("./../workspace.fxml"));
    	Parent workspace = loader.load();	
    	Scene workspaceScene = new Scene(workspace, 1280, 720);	
    	Stage stage = (Stage) backButton.getScene().getWindow();	
    	stage.setScene(workspaceScene);	
        stage.show();
    }
    
}