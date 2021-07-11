package com.proginternet.ui.preference.createPreference;


import java.io.IOException;
import java.util.ArrayList;

import com.proginternet.models.Activity;
import com.proginternet.models.Preference;
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

public class FXMLCreatePreferenceController {

    @FXML private Text creationMsg;
	@FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private Button backButton;
    @FXML private ChoiceBox<String> pickWorkspace;
    @FXML private ChoiceBox<String> pickActivity;
 
    private ArrayList<Workspace> workspaces;
    private ArrayList<Activity> activities;
    private ArrayList<Preference> preferencies;

    @FXML public void initialize() {
        loadWorkspace();    
    }

    @FXML public void create(ActionEvent event) {
        String filename = "data/Workspace.json";
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        for (Workspace workspace : workspaces) {
            if(workspace.getName().equals(pickWorkspace.getValue())) {
                activities = workspaces.get(workspaces.indexOf(workspace)).getActivities();
                for (Activity activity : activities) {
                    if (activity.getName().equals(pickActivity.getValue())) {
                        preferencies = activities.get(activities.indexOf(activity)).getPreference();
                        break;
                    }
                }
                break;
            }
        }
        
        preferencies.add(new Preference(nameField.getText(), descriptionField.getText()));
        
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

    public void loadWorkspace() {
        String filename = "data/Workspace.json";
		JsonParser<Workspace> parser = new JsonParser<Workspace>();
        this.workspaces = parser.readOnJson(filename, Workspace[].class);
        for (Workspace workspace : workspaces) {
            pickWorkspace.getItems().add(workspace.getName());
        }
    }

    public void loadActivities() {
        pickActivity.getItems().remove(0, pickActivity.getItems().size());
        activities = null;
        
        for (Workspace workspace : workspaces) {
            if(workspace.getName().equals(pickWorkspace.getValue())) {
                activities = workspaces.get(workspaces.indexOf(workspace)).getActivities();
                break;
            }
        }
        
        for (Activity activity : activities) {
            pickActivity.getItems().add(activity.getName());
        }
    }

    @FXML public void selectedWorkspace() {
        loadActivities();
    }
    
}