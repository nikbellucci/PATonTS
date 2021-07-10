package com.proginternet.ui.activity.assignActivity;

import java.io.IOException;
import java.util.ArrayList;

import com.proginternet.models.Activity;
import com.proginternet.models.User;
import com.proginternet.models.Workspace;
import com.proginternet.utils.JsonParser;
import com.sun.prism.paint.Color;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.stage.Stage;
import javafx.util.Callback;

public class FXMLAssignActivityController {
    
    @FXML private ListView<String> userList;
    @FXML private Button backButton;
    @FXML private ChoiceBox<String> pickWorkspace;
    @FXML private ListView<String> activityList;

    private ArrayList<Activity> activities;
    private ArrayList<Workspace> workspaces;
    private ArrayList<User> users;
    private ObservableList<String> observableList = FXCollections.observableArrayList();

    @FXML public void initialize() {
        loadUser();
        loadCheckBox();
    }

    private void loadUser(){
        JsonParser<User> parser = new JsonParser<User>();
        this.users = parser.readOnJson("data/Users.json", User[].class);
        ArrayList<String> names = new ArrayList<String>();

        for (User user : users) {
            names.add(user.getUsername());
        }

        observableList.removeAll(observableList);
        observableList.addAll(names);
        userList.getItems().addAll(observableList);

        userList.setCellFactory(CheckBoxListCell.forListView(new Callback<String,ObservableValue<Boolean>>(){
            public ObservableValue<Boolean> call(String item) {
                BooleanProperty observable = new SimpleBooleanProperty();

                for (Workspace workspace : workspaces) {
                    if(workspace.getName().equals(pickWorkspace.getValue())) {
                        activities = workspaces.get(workspaces.indexOf(workspace)).getActivities();
                        break;
                    }
                }

                observable.addListener((obs, wasSelected, isNowSelected) -> {
                    System.out.println("Check box for "+item+" changed from "+wasSelected+" to "+isNowSelected);

                }                   
                );
                return observable ;
            }
        }));
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

    private void loadCheckBox(){
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
    
}
