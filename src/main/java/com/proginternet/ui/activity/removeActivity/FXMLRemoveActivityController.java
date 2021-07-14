package com.proginternet.ui.activity.removeActivity;


import java.io.IOException;
import java.util.ArrayList;

import com.proginternet.models.Activity;
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

public class FXMLRemoveActivityController {

    @FXML private Text removeMsg;
    @FXML private ListView<String> activityList;
    @FXML private Button removeButton;
    @FXML private ChoiceBox<String> pickWorkspace;
    @FXML private Button backButton;

    private ArrayList<Workspace> workspaces;
    private ArrayList<Activity> activities;
    private ObservableList<String> observableList = FXCollections.observableArrayList();

    @FXML public void initialize() {
        loadChoiceBox();
        removeButton.setDisable(true);
    }

    @FXML public void remove(ActionEvent event) {
        String filename = "data/Workspace.json";
		JsonParser<Workspace> parser = new JsonParser<Workspace>();

        for (int i = 0; i < activities.size(); i++) {
            if (activityList.getSelectionModel().getSelectedItem().equals(activities.get(i).getName())) {
                activities.remove(i);
                activityList.getItems().remove(i);
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

    private void loadChoiceBox(){
        String filename = "data/Workspace.json";
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        workspaces = parser.readOnJson(filename, Workspace[].class);
        for (Workspace workspace : workspaces) {
            pickWorkspace.getItems().add(workspace.getName());
        }
    }

    public void loadListView() {
        activityList.getItems().remove(0, activityList.getItems().size());
        activities = null;
        
        for (Workspace workspace : workspaces) {
            if(workspace.getName().equals(pickWorkspace.getValue())) {
                activities = workspaces.get(workspaces.indexOf(workspace)).getActivities();
                break;
            }
        }

        ArrayList<String> names = new ArrayList<String>();
        
        for (Activity activity : activities) {
            names.add(activity.getName());
        }

        observableList.removeAll(observableList);
        observableList.addAll(names);
        activityList.getItems().addAll(observableList);
    }

    @FXML public void selectedWorkspace(ActionEvent event) {
        if (workspaces != null) {
            loadListView();
        }
    }

    @FXML public void handleMouseClick(MouseEvent event) {
        removeButton.setDisable(false);
    }
    
}