package com.proginternet;

import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.proginternet.utils.Auth;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class App /*extends Application*/ {
    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // launch();

        User person = new User("Leonardo", "Di Caprio", "");
        User person2 = new User("Pinco", "Pallo", "");

        String generatedSecuredPasswordHash = Auth.generateStorngPasswordHash("ciao");
        person.password = generatedSecuredPasswordHash;
        person2.password = generatedSecuredPasswordHash;
         
        boolean matched = Auth.validatePassword("ciao", person.password);
        System.out.println(matched);
         
        matched = Auth.validatePassword("password1", generatedSecuredPasswordHash);
        System.out.println(matched);

        User[] persons = {person, person2};
        
        //prettyPriting imposta il json su piu righe
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String fileName = "data/Users.json";
          
        try {
            FileWriter writer = new FileWriter(fileName);
            gson.toJson(persons, writer);
            writer.close();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }
}
