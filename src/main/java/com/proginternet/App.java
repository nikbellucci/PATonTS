package com.proginternet;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import com.proginternet.models.User;
import com.proginternet.ui.LoginActivity;
import com.proginternet.utils.Auth;
import com.proginternet.utils.JsonParser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class App {
    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // launch(args);

        // User person = new User("Leonardo", "Di Caprio", "", true);
        // User person2 = new User("Pinco", "Pallo", "", false);
        // User person3 = new User("Pincos", "Pallo", "", false);
        // String filename = "data/Users.json";

        // String generatedSecuredPasswordHash = Auth.generateStorngPasswordHash("ciao");
        // person.setPassword(generatedSecuredPasswordHash);
        // person2.setPassword(generatedSecuredPasswordHash);

        // ArrayList<User> users = new ArrayList<User>();

        // users.add(person);
        // users.add(person2);

        // JsonParser<User> parser = new JsonParser<User>();

        // parser.writeOnJson(users, filename);
        // ArrayList<User> utenti = parser.readOnJson(filename);

        // utenti.add(person3);

        // parser.writeOnJson(utenti, filename);
        
    }

    // public void start(Stage stage) {
    //     String javaVersion = System.getProperty("java.version");
    //     String javafxVersion = System.getProperty("javafx.version");
    //     Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
    //     Scene scene = new Scene(new StackPane(l), 640, 480);
    //     stage.setScene(scene);
    //     stage.show();
    // }
}
