package com.proginternet.ui.preference.removePreference;


import java.io.IOException;
import java.util.ArrayList;

import com.proginternet.models.Activity;
import com.proginternet.models.Preference;
import com.proginternet.models.Workspace;
import com.proginternet.utils.JsonParser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FXMLRemovePreferenceController {

    @FXML private Text removeMsg;
    @FXML private ListView<String> preferenceList;
    @FXML private Button removeButton;
    @FXML private ChoiceBox<String> pickWorkspace;
    @FXML private ChoiceBox<String> pickActivity;
    @FXML private Button backButton;

    private ArrayList<Workspace> workspaces;
    private ArrayList<Activity> activities;
    private ArrayList<Preference> preferencies;
    private ObservableList<String> observableList = FXCollections.observableArrayList();

    @FXML public void initialize() {
        loadWorkspace();
        removeButton.setDisable(true);
    }

    @FXML public void remove(ActionEvent event) {
        String filename = "data/Workspace.json";
		JsonParser<Workspace> parser = new JsonParser<Workspace>();

        for (int i = 0; i < preferencies.size(); i++) {
            if (preferenceList.getSelectionModel().getSelectedItem().equals(preferencies.get(i).getName())) {
                preferencies.remove(i);
                preferenceList.getItems().remove(i);
                parser.writeOnJson(filename, workspaces);
		        removeMsg.setText("Cancellazione avvenuta con successo");
    	        removeMsg.setFill(Color.GREEN);
            }
        }
        
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

    public void loadListView() {
        preferenceList.getItems().remove(0, preferenceList.getItems().size());
        preferencies = null;
        
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

        ArrayList<String> names = new ArrayList<String>();

        for (Preference preference : preferencies) {
            names.add(preference.getName());
        }

        observableList.removeAll(observableList);
        observableList.addAll(names);
        preferenceList.getItems().addAll(observableList);
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

    @FXML public void handleMouseClick(MouseEvent event) {
        removeButton.setDisable(false);
    }
    
}