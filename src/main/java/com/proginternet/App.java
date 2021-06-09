package com.proginternet;

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
        Seed.init();
        launch(args);
    }

    
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("./ui/registration/registration.fxml"));

        primaryStage.setTitle("Admin panel");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

}