package com.proginternet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.proginternet.models.User;
import com.proginternet.utils.Auth;
import com.proginternet.utils.JsonParser;

public final class App extends Application{
    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException , FileNotFoundException{
        // launch(args);

        User person = new User("Leonardo", "Di Caprio", "test", "", true);
        User person2 = new User("Pinco", "Pallo", "test2", "", false);
        String filename = "data/Users.json";

        String generatedSecuredPasswordHash = Auth.generateStorngPasswordHash("ciao");
        person.setPassword(generatedSecuredPasswordHash);
        person2.setPassword(generatedSecuredPasswordHash);

        User[] users = {person, person2};

        JsonParser<User> parser = new JsonParser<User>();

        parser.writeOnJson(filename, users);
        User person3 = new User("Pincos", "Pallo", "test2", "", false);
        ArrayList<User> test = parser.readOnJson(filename, User[].class);
        
        if (User.checkUsername(person3.getUsername()))
            test.add(person3);
    }

    
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("./ui/login/admin.fxml"));
        primaryStage.setTitle("Admin panel");
        primaryStage.setScene(new Scene(root, 1920, 1080));
        primaryStage.show();
    }

}