package com.proginternet;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class App extends Application{
    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */

    public static void main(String[] args) {
        //Seed.init();
        File user = new File("data/Users.json");
        File workspace = new File("data/Workspace.json");
        File preferences = new File("data/Preference.json");
        try {
            user.createNewFile();
            workspace.createNewFile();
            preferences.createNewFile();           
        } catch (IOException e) {
            e.printStackTrace();
        }
        launch(args);
    }

    
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("./ui/login/login.fxml"));

        primaryStage.setTitle("PaTonTS");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

}