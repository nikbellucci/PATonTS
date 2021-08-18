package com.proginternet.ui.preference.answerPreference;


import java.io.IOException;
import java.util.ArrayList;

import com.proginternet.models.Activity;
import com.proginternet.models.Association;
import com.proginternet.models.Preference;
import com.proginternet.models.User;
import com.proginternet.models.Workspace;
import com.proginternet.utils.JsonParser;
import com.proginternet.utils.Singleton;

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

public class FXMLAnswerPreferenceController {

    @FXML private Text creationMsg;
	@FXML private TextField nameField;
    @FXML private TextArea preferenceField;
    @FXML private Button backButton;
    @FXML private ChoiceBox<String> pickWorkspace;
    @FXML private ChoiceBox<String> pickActivity;
    @FXML private ChoiceBox<String> pickPreference;
 
    private ArrayList<Workspace> workspaces;
    private ArrayList<Activity> activities;
    private ArrayList<Preference> preferencies;
    private ArrayList<Association> associations;
    private String worksapceId;
    private String activityId;
    private String preferenceId;
    private User user = null;

    @FXML public void initialize() {
        receiveData();
        loadDataNotAdmin();    
    }

    @FXML public void answer(ActionEvent event) {
        String filename = "data/Associations.json";
        JsonParser<Association> parser = new JsonParser<Association>();
        associations = parser.readOnJson(filename, Association[].class);
        selectedPreference();

        associations.add(new Association(user.getUsername(), worksapceId, activityId, preferenceId, preferenceField.getText()));

        parser.writeOnJson(filename, associations);
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

    // public void loadWorkspace() {
    //     String filename = "data/Workspace.json";
	// 	JsonParser<Workspace> parser = new JsonParser<Workspace>();
    //     this.workspaces = parser.readOnJson(filename, Workspace[].class);
    //     for (Workspace workspace : workspaces) {
    //         pickWorkspace.getItems().add(workspace.getName());
    //         worksapceId = workspace.getId();
    //     }
    // }

    private void loadDataNotAdmin(){
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        this.workspaces = parser.readOnJson("data/Workspace.json", Workspace[].class);

        for (Workspace workspace : workspaces) {
            for (String workspaceId : this.user.getWorkArray()) {
                if (workspaceId.equals(workspace.getId())) {
                    pickWorkspace.getItems().add(workspace.getName());
                }
            }
        }
    }

    public void loadActivities() {
        pickActivity.getItems().remove(0, pickActivity.getItems().size());
        activities = null;
        
        for (Workspace workspace : workspaces) {
            if(workspace.getName().equals(pickWorkspace.getValue())) {
                worksapceId = workspace.getId();
                activities = workspaces.get(workspaces.indexOf(workspace)).getActivities();
                break;
            }
        }
        
        for (Activity activity : activities) {
            pickActivity.getItems().add(activity.getName());
        }
    }

    public void loadPreferences() {
        pickPreference.getItems().remove(0, pickPreference.getItems().size());
        preferencies = null;

        for (Activity activity : activities) {
            if (activity.getName().equals(pickActivity.getValue())) {
                activityId = activity.getId();
                preferencies = activities.get(activities.indexOf(activity)).getPreference();
                break;
            }
        }

        for (Preference preference : preferencies) {
            pickPreference.getItems().add(preference.getName());
        }
    }

    public void selectedPreference() {
        for (Preference preference : preferencies) {
            if (preference.getName().equals(pickPreference.getValue())) {
                preferenceId = preference.getId();
                break;
            }
        }
    }

    @FXML public void selectedWorkspace() {
        loadActivities();
    }

    @FXML public void selectedActivity() {
        loadPreferences();
    }

    private void receiveData() {
        Singleton holder = Singleton.getInstance();
        this.user = holder.getUser();
    }
    
}