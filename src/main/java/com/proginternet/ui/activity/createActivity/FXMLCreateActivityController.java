package com.proginternet.ui.activity.createActivity;


import java.io.IOException;
import java.util.ArrayList;

import com.proginternet.models.Activity;
import com.proginternet.models.Workspace;
import com.proginternet.utils.JsonParser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FXMLCreateActivityController {

    @FXML private Text creationMsg;
	@FXML private TextField nameField;
	@FXML private TextField whereField;
	@FXML private TextField usernameField;
    @FXML private TextField idField;
    @FXML private TextArea descriptionField;
    @FXML private Button backButton;
    @FXML private ChoiceBox<String> pickWorkspace;

    private ArrayList<Workspace> workspaces;

    @FXML public void initialize() {
        loadData();    
    }

    @FXML public void create(ActionEvent event) {
        String filename = "data/Workspace.json";
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Activity> activities = null;
        for (Workspace workspace : workspaces) {
            if(workspace.getName().equals(pickWorkspace.getValue())) {
                activities = workspaces.get(workspaces.indexOf(workspace)).getActivities();
                break;
            }
        }        
        
        Activity newActv = new Activity(idField.getText(), nameField.getText(), descriptionField.getText(), whereField.getText());
        activities.add(newActv);
        parser.writeOnJson(filename, workspaces);
		creationMsg.setText("Registration successfull!");
    	creationMsg.setFill(Color.GREEN);
    }

    @FXML protected void showWorkspace(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("./../../workspace/workspace.fxml"));
    	Parent workspace = loader.load();	
    	Scene workspaceScene = new Scene(workspace, 1280, 720);	
    	Stage stage = (Stage) backButton.getScene().getWindow();	
    	stage.setScene(workspaceScene);	
        stage.show();
    }

    public void loadData() {
        String filename = "data/Workspace.json";
		JsonParser<Workspace> parser = new JsonParser<Workspace>();
        this.workspaces = parser.readOnJson(filename, Workspace[].class);
        for (Workspace workspace : workspaces) {
            pickWorkspace.getItems().add(workspace.getName());
        }
    }
    
}