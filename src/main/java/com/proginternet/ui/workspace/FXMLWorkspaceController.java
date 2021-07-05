package com.proginternet.ui.workspace;

import java.util.ArrayList;

import com.proginternet.models.User;
import com.proginternet.models.Workspace;
import com.proginternet.utils.JsonParser;
import com.proginternet.utils.Singleton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class FXMLWorkspaceController {

    @FXML private ListView<String> workspaceList;
    @FXML private TabPane activityTabs;
    @FXML private Text welcomeUser;
    ObservableList<String> observableList = FXCollections.observableArrayList();

    private User user = null;

    @FXML public void initialize() {
        receiveData();
        welcomeUser.setText("Benvenuto, " + this.user.getName() + " " + this.user.getSurname());
        loadData();
    }

    private void receiveData() {
        Singleton holder = Singleton.getInstance();
        this.user = holder.getUser();
    }

    private void loadData(){
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> workspaces = parser.readOnJson("data/Workspace.json", Workspace[].class);
        ArrayList<String> names = new ArrayList<String>();

        for (Workspace workspace : workspaces) {
            names.add(workspace.getName());
        }

        observableList.removeAll(observableList);
        observableList.addAll(names);
        workspaceList.getItems().addAll(observableList);

        /* 
            Tab activity da finire
        */
    }

    @FXML public void handleMouseClick(MouseEvent event) {
        System.out.println("clicked on " + workspaceList.getSelectionModel().getSelectedItem());
        
    }
    
}