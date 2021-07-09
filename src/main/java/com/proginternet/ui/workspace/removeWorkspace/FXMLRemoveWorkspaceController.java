package com.proginternet.ui.workspace.removeWorkspace;


import java.io.IOException;
import java.util.ArrayList;

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
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FXMLRemoveWorkspaceController {

    @FXML private Text removeMsg;
    @FXML private ListView<String> workspaceList;
    @FXML private Button removeButton;

    private ArrayList<Workspace> workspaces;
    private ObservableList<String> observableList = FXCollections.observableArrayList();

    @FXML public void initialize() {
        loadData();
        removeButton.setDisable(true);
    }

    @FXML public void remove(ActionEvent event) {
        String filename = "data/Workspace.json";
		JsonParser<Workspace> parser = new JsonParser<Workspace>();

        for (int i = 0; i < workspaces.size(); i++) {
            if (workspaceList.getSelectionModel().getSelectedItem().equals(workspaces.get(i).getName())) {
                workspaces.remove(i);
                workspaceList.getItems().remove(i);
                parser.writeOnJson(filename, workspaces);
		        removeMsg.setText("Cancellazione avvenuta con successo");
    	        removeMsg.setFill(Color.GREEN);
            }
        }
        
    }

    @FXML protected void showWorkspace(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("./../workspace.fxml"));
    	Parent workspace = loader.load();	
    	Scene workspaceScene = new Scene(workspace, 1280, 720);	
    	Stage stage = (Stage) removeMsg.getScene().getWindow();	
    	stage.setScene(workspaceScene);	
        stage.show();
    }

    private void loadData(){
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        workspaces = parser.readOnJson("data/Workspace.json", Workspace[].class);
        ArrayList<String> names = new ArrayList<String>();

        for (Workspace workspace : workspaces) {
            names.add(workspace.getName());
        }

        observableList.removeAll(observableList);
        observableList.addAll(names);
        workspaceList.getItems().addAll(observableList);
    }

    @FXML public void handleMouseClick(MouseEvent event) {
        removeButton.setDisable(false);
    }
    
}