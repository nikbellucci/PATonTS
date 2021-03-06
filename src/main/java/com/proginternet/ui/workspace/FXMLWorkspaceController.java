package com.proginternet.ui.workspace;

import java.io.File;
import java.util.ArrayList;

import com.proginternet.models.Association;
import com.proginternet.models.Preference;
import com.proginternet.models.User;
import com.proginternet.models.Workspace;
import com.proginternet.utils.JsonParser;
import com.proginternet.utils.Singleton;
import com.proginternet.utils.ZipTool;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class FXMLWorkspaceController {

    @FXML private ListView<String> workspaceList;
    @FXML private TabPane activityTabs;
    @FXML private Text welcomeUser;
    @FXML private MenuButton dropdownMenu;
    @FXML private Menu userMenu;
    @FXML private Menu activityMenu;
    @FXML private Menu workspaceMenu;
    @FXML private MenuItem createPreference;
    @FXML private MenuItem removePreference;

    private ArrayList<Workspace> workspaces;
    private ArrayList<Preference> preferencies;
    private ArrayList<Association> associations;
    private ObservableList<String> observableList = FXCollections.observableArrayList();
    private User user = null;
    private ListView<String> preferenceList = new ListView<>();
    private String workspaceID;
    private String activityID;
    private String preferenceID;

    @FXML public void initialize() {
        receiveData();
        welcomeUser.setText("Benvenuto, " + this.user.getName() + " " + this.user.getSurname());

        JsonParser<Association> parser = new JsonParser<>();
        this.associations = parser.readOnJson("data/Associations.json", Association[].class);
        
        if (!this.user.getAdminPermission()) {
            userMenu.setDisable(true);
            userMenu.setVisible(false);
            workspaceMenu.setDisable(true);
            workspaceMenu.setVisible(false);
            activityMenu.setDisable(true);
            activityMenu.setVisible(false);
            createPreference.setDisable(true);
            createPreference.setVisible(false);
            removePreference.setDisable(true);
            removePreference.setVisible(false);
            loadDataNotAdmin();
        }else {
            loadDataAdmin();
        }

    }

    private void receiveData() {
        Singleton holder = Singleton.getInstance();
        this.user = holder.getUser();
    }

    private void loadDataAdmin(){
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        this.workspaces = parser.readOnJson("data/Workspace.json", Workspace[].class);
        ArrayList<String> names = new ArrayList<String>();

        for (Workspace workspace : workspaces) {
            names.add(workspace.getName());
        }

        observableList.removeAll(observableList);
        observableList.addAll(names);
        workspaceList.getItems().addAll(observableList);
    }

    private void loadDataNotAdmin(){
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        this.workspaces = parser.readOnJson("data/Workspace.json", Workspace[].class);
        ArrayList<String> names = new ArrayList<String>();

        for (Workspace workspace : workspaces) {
            for (String workspaceId : this.user.getWorkArray()) {
                if (workspaceId.equals(workspace.getId())) {
                    names.add(workspace.getName());
                }
            }
        }

        observableList.removeAll(observableList);
        observableList.addAll(names);
        workspaceList.getItems().addAll(observableList);
    }

    @FXML public void handleMouseClick(MouseEvent event) {
        activityTabs.getTabs().remove(0, activityTabs.getTabs().size());

        for (int i = 0; i < workspaces.size(); i++) {
            if (workspaceList.getSelectionModel().getSelectedItem().equals(workspaces.get(i).getName())) {
                for (int j = 0; j < workspaces.get(i).getActivities().size(); j++) {
                    activityTabs.getTabs().add(new Tab(workspaces.get(i).getActivities().get(j).getName()));
                    // tabs.clear();
                    // tabs.add(new Tab(workspaces.get(i).getActivities().get(j).getName()));
                    // activityTabs.getTabs().addAll(tabs);
                } 
            }
        }

    }

    @FXML public void populateListView(MouseEvent event) {
        preferenceList.getItems().remove(0, preferenceList.getItems().size());
        ArrayList<String> preferenceNames = new ArrayList<String>();

        for (int i = 0; i < workspaces.size(); i++) {
            if (workspaceList.getSelectionModel().getSelectedItem().equals(workspaces.get(i).getName())) {
                for (int j = 0; j < workspaces.get(i).getActivities().size(); j++) {
                    if (activityTabs.getSelectionModel().getSelectedItem().getText().equals(workspaces.get(i).getActivities().get(j).getName())) {
                        preferencies = workspaces.get(i).getActivities().get(j).getPreference();
                        workspaceID = workspaces.get(i).getId();
                        activityID = workspaces.get(i).getActivities().get(j).getId();
                    }
                } 
            }
        }

        if (!preferencies.isEmpty()) {
            for (Preference preference : preferencies) {
                preferenceNames.add(preference.getName());
            }
    
            observableList.removeAll(observableList);
            observableList.addAll(preferenceNames);
            preferenceList.getItems().addAll(observableList);
    
            activityTabs.getSelectionModel().getSelectedItem().setContent(preferenceList);
            showPopUp();
        }
    }

    public void showPopUp() {
        Alert alert = new Alert(AlertType.INFORMATION);
        
        alert.setTitle("Risposta");
        alert.setHeaderText(null);
        

        preferenceList.setOnMouseClicked(event -> {

            for (Preference preference : preferencies) {
                if (preferenceList.getSelectionModel().getSelectedItem().equals(preference.getName())) {
                    preferenceID = preference.getId();
                }
            }

            for (Association association : associations) {
                if (association.getUser().equals(user.getUsername()) && association.getWorkspaceId().equals(workspaceID) && association.getActivityId().equals(activityID) && association.getPreferenceId().equals(preferenceID)) {
                    alert.setContentText(association.getPreferenceAnswer());
                }
            }

            // System.out.println(preferenceList.getSelectionModel().getSelectedItem().toString());
            // System.out.println(workspaceID);
            // System.out.println(activityID);
            // System.out.println(preferenceID);
            
            alert.showAndWait();
        });
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./../registration/registration.fxml"));
            Parent login = loader.load();
            Scene loginScene = new Scene(login, 1280, 720);
            Stage stage = (Stage) welcomeUser.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void createWorkspace() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./../workspace/createWorkspace/create-workspace.fxml"));
            Parent login = loader.load();
            Scene loginScene = new Scene(login, 1280, 720);
            Stage stage = (Stage) welcomeUser.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void removeWorkspace() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./../workspace/removeWorkspace/remove-workspace.fxml"));
            Parent login = loader.load();
            Scene loginScene = new Scene(login, 1280, 720);
            Stage stage = (Stage) welcomeUser.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void importFile(ActionEvent event) {
        FileChooser file = new FileChooser();
        ExtensionFilter filter = new ExtensionFilter("ZIP Files", "*.zip");
        file.getExtensionFilters().add(filter);
        file.setSelectedExtensionFilter(filter);
        file.setTitle("test");
        Stage stage = (Stage) welcomeUser.getScene().getWindow();
		File response = file.showOpenDialog(stage);
        if (response != null) {
            ZipTool.unzip(response.toString(), "data");
        }
    }

    @FXML public void exportFile() {
        File dir = new File("data");
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Scegli una directory");
        Stage stage = (Stage) welcomeUser.getScene().getWindow();
        File selectedDirectory = chooser.showDialog(stage);
        String path = selectedDirectory.getAbsolutePath() + File.separator + "data.zip";

        System.out.println(path);

        ZipTool.zipDirectory(dir, path);
    }

    @FXML public void createActivity() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./../activity/createActivity/create-activity.fxml"));
            Parent login = loader.load();
            Scene loginScene = new Scene(login, 1280, 720);
            Stage stage = (Stage) welcomeUser.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML public void removeActivity() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./../activity/removeActivity/remove-activity.fxml"));
            Parent login = loader.load();
            Scene loginScene = new Scene(login, 1280, 720);
            Stage stage = (Stage) welcomeUser.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void assignActivity() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./../activity/assignActivity/assign-activity.fxml"));
            Parent login = loader.load();
            Scene loginScene = new Scene(login, 1280, 720);
            Stage stage = (Stage) welcomeUser.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void unsignActivity() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./../activity/unsignActivity/unsign-activity.fxml"));
            Parent login = loader.load();
            Scene loginScene = new Scene(login, 1280, 720);
            Stage stage = (Stage) welcomeUser.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void createPreference() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./../preference/createPreference/create-preference.fxml"));
            Parent login = loader.load();
            Scene loginScene = new Scene(login, 1280, 720);
            Stage stage = (Stage) welcomeUser.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void removePreference() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./../preference/removePreference/remove-preference.fxml"));
            Parent login = loader.load();
            Scene loginScene = new Scene(login, 1280, 720);
            Stage stage = (Stage) welcomeUser.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void addAnswerPreference() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./../preference/answerPreference/answer-preference.fxml"));
            Parent login = loader.load();
            Scene loginScene = new Scene(login, 1280, 720);
            Stage stage = (Stage) welcomeUser.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}