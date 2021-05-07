package com.proginternet;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.proginternet.utils.Auth;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class App {
    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // launch();
        String  originalPassword = "password";
        String generatedSecuredPasswordHash = Auth.generateStorngPasswordHash(originalPassword);
        System.out.println(generatedSecuredPasswordHash);
         
        boolean matched = Auth.validatePassword("password", generatedSecuredPasswordHash);
        System.out.println(matched);
         
        matched = Auth.validatePassword("password1", generatedSecuredPasswordHash);
        System.out.println(matched);
        
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
