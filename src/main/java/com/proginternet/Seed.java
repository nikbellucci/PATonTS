package com.proginternet;

import java.util.ArrayList;

import com.proginternet.models.User;
import com.proginternet.utils.Auth;
import com.proginternet.utils.JsonParser;

public class Seed {

    public static void init() {
        try {
            User person = new User("Leonardo", "Di Caprio", "test", "", true);
            User person2 = new User("Pinco", "Pallo", "test2", "", false);
            String filename = "data/Users.json";

            String generatedSecuredPasswordHash = Auth.generateStorngPasswordHash("test");

            person.setPassword(generatedSecuredPasswordHash);
            person2.setPassword(Auth.generateStorngPasswordHash("test2"));

            ArrayList<User> users = new ArrayList<User>();
            users.add(person);
            users.add(person2);

            JsonParser<User> parser = new JsonParser<User>();

            parser.writeOnJson(filename, users);
            User person3 = new User("Pincos", "Pallo", "test3", "", false);
            ArrayList<User> test = parser.readOnJson(filename, User[].class);
            
            if (!User.userExist(person3.getUsername())){
                test.add(person3);

                parser.writeOnJson(filename, test);
            }

            for (User user : test) {
                System.out.println(user.getUsername());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
