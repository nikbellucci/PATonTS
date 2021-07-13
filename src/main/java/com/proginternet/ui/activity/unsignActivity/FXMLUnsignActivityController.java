package com.proginternet.ui.activity.unsignActivity;

import java.io.IOException;
import java.util.ArrayList;

import com.proginternet.models.Activity;
import com.proginternet.models.Association;
import com.proginternet.models.User;
import com.proginternet.models.Workspace;
import com.proginternet.utils.JsonParser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FXMLUnsignActivityController {
    
    @FXML private ListView<String> userList;
    @FXML private Button backButton;
    @FXML private ChoiceBox<String> pickWorkspace;
    @FXML private ListView<String> activityList;

    private ArrayList<Activity> activities;
    private ArrayList<Workspace> workspaces;
    private ArrayList<User> users;
    private ArrayList<Association> associations;

    @FXML public void initialize() {
        loadUser();
        loadWorkspace();
    }

    private void loadUser(){
        userList.getItems().remove(0, userList.getItems().size());
        JsonParser<User> parser = new JsonParser<User>();
        this.users = parser.readOnJson("data/Users.json", User[].class);
        
        for (User user : users) {
            userList.getItems().add(user.getUsername());
        }

        // observableList.removeAll(observableList);
        // observableList.addAll(names);
        // userList.getItems().addAll(observableList);
    }

    public void loadListView() {
        activityList.getItems().remove(0, activityList.getItems().size());
        
        for (Workspace workspace : workspaces) {
            if(workspace.getName().equals(pickWorkspace.getValue())) {
                activities = workspaces.get(workspaces.indexOf(workspace)).getActivities();
                break;
            }
        }
        
        for (Activity activity : activities) {
            activityList.getItems().add(activity.getName());
        }

        // observableList.removeAll(observableList);
        // observableList.addAll(names);
        // activityList.getItems().addAll(observableList);
    }

    private void loadWorkspace(){
        String filename = "data/Workspace.json";
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        workspaces = parser.readOnJson(filename, Workspace[].class);
        for (Workspace workspace : workspaces) {
            pickWorkspace.getItems().add(workspace.getName());
        }
    }

    @FXML protected void showWorkspace(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../../workspace/workspace.fxml"));
    	Parent workspace = loader.load();	
    	Scene workspaceScene = new Scene(workspace, 1280, 720);	
    	Stage stage = (Stage) backButton.getScene().getWindow();	
    	stage.setScene(workspaceScene);	
        stage.show();
    }

    @FXML public void selectedWorkspace(ActionEvent event) {
        loadListView();
        // da finire vista activity user
    }

    @FXML public void handleMouseClick(MouseEvent event) {

    }
    
}





// 