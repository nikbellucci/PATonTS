package com.proginternet.ui.workspace;

import java.util.ArrayList;

import com.proginternet.models.User;
import com.proginternet.models.Workspace;
import com.proginternet.utils.JsonParser;
import com.proginternet.utils.Singleton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FXMLWorkspaceController {

    @FXML private ListView<String> workspaceList;
    @FXML private TabPane activityTabs;
    @FXML private Text welcomeUser;
    @FXML private Button modifyBtn;
    @FXML private MenuButton dropdownMenu;

    ObservableList<String> observableList = FXCollections.observableArrayList();

    private User user = null;

    @FXML public void initialize() {
        receiveData();
        welcomeUser.setText("Benvenuto, " + this.user.getName() + " " + this.user.getSurname());
        loadData();

        if (!this.user.getAdminPermission()) {
            modifyBtn.setDisable(true);
            modifyBtn.setVisible(false);
        }

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

    @FXML public void logout(ActionEvent event) {

        try {
            Singleton holder = Singleton.getInstance();
            holder.removeUser();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./../login/login.fxml"));
            Parent login = loader.load();
            Scene loginScene = new Scene(login, 1280, 720);
            Stage stage = (Stage) welcomeUser.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void createUser() {

    }
    
}