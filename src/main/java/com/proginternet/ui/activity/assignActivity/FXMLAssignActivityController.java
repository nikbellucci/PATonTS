package com.proginternet.ui.activity.assignActivity;

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
import javafx.stage.Stage;

public class FXMLAssignActivityController {
    
    @FXML private Button backButton;
    @FXML private ChoiceBox<String> pickWorkspace;
    // @FXML private ChoiceBox<String> pickActivity;
    @FXML private ChoiceBox<String> pickUser;

    private ArrayList<Activity> activities;
    private ArrayList<Workspace> workspaces;
    private ArrayList<User> users;
    private ArrayList<Association> associations;

    @FXML public void initialize() {
        loadUser();
        loadWorkspace();
    }

    // @FXML public void linkActivity() {
    //     JsonParser<Association> parser = new JsonParser<Association>();
    //     associations = parser.readOnJson("data/Associations.json", Association[].class);
    //     String workspaceId = "";
    //     for (Workspace workspace : workspaces) {
    //         if(pickWorkspace.getValue().equals(workspace.getName())) {
    //             workspaceId = workspace.getId();
    //             break;
    //         }
    //     }

    //     boolean bool = true;

    //     for (Association association : associations) {
            // if (association.getUser().equals(pickUser.getValue())) {
            //     if (association.getWorkspaceId().equals(workspaceId)) {
            //         bool = false;
            //         break;
            //     }else{
            //         bool = true;
            //     }
            // }
    //     }

    //     if (bool) {
    //         associations.add(new Association(pickUser.getValue(), workspaceId));
    //         parser.writeOnJson("data/Associations.json", associations);
    //     }
    // }

    @FXML public void linkActivity() {
        ArrayList<String> workspaceOnUser = new ArrayList<>();

        String workspaceId = "";
        for (Workspace workspace : workspaces) {
            if(pickWorkspace.getValue().equals(workspace.getName())) {
                workspaceId = workspace.getId();
            }
        }

        boolean bool = false;
        String username = "";
        for (User user : users ) {
            if (user.getUsername().equals(pickUser.getValue())) {
                workspaceOnUser = user.getWorkArray();
                if (workspaceOnUser.isEmpty()) {
                    bool = true;
                    username = user.getUsername();
                    break;
                }
                for (String workspaceString : workspaceOnUser) {
                    if (workspaceString.equals(workspaceId)) {
                        bool = false;
                        break;
                    }else{
                        bool = true;
                        username = user.getUsername();
                    }
                }
            }
        }
        

        if (bool) {
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    JsonParser<User> parser = new JsonParser<User>();
                    workspaceOnUser.add(workspaceId);
                    user.setWorkArray(workspaceOnUser);
                    parser.writeOnJson("data/Users.json", users);
                }
                
            }
        }
    }

    private void loadUser(){
        pickUser.getItems().remove(0, pickUser.getItems().size());
        JsonParser<User> parser = new JsonParser<User>();
        this.users = parser.readOnJson("data/Users.json", User[].class);
        
        for (User user : users) {
            pickUser.getItems().add(user.getUsername());
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

    public void loadWorkspace() {
        String filename = "data/Workspace.json";
		JsonParser<Workspace> parser = new JsonParser<Workspace>();
        this.workspaces = parser.readOnJson(filename, Workspace[].class);
        for (Workspace workspace : workspaces) {
            pickWorkspace.getItems().add(workspace.getName());
        }
    }

    // public void loadActivities() {
    //     pickActivity.getItems().remove(0, pickActivity.getItems().size());
        
    //     for (Workspace workspace : workspaces) {
    //         if(workspace.getName().equals(pickWorkspace.getValue())) {
    //             activities = workspaces.get(workspaces.indexOf(workspace)).getActivities();
    //             break;
    //         }
    //     }
        
    //     for (Activity activity : activities) {
    //         pickActivity.getItems().add(activity.getName());
    //     }
    // }

    @FXML public void selectedWorkspace() {
        // loadActivities();
    }

}





// 