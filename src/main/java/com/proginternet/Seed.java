package com.proginternet;

import java.time.LocalDate;
import java.util.ArrayList;

import com.proginternet.models.Association;
import com.proginternet.models.User;
import com.proginternet.models.Workspace;
import com.proginternet.utils.Auth;
import com.proginternet.utils.JsonParser;

public class Seed {

    public static void init() {
        try {
            User person = new User("Leonardo", "Di Caprio", "test", "", true);
            User person2 = new User("Pinco", "Pallo", "test2", "", false);
            Workspace workspace = new Workspace("1","Test workspace", "test descrizione", "test categoria", LocalDate.parse("2020-01-08"));
            Workspace workspace1 = new Workspace("2","Test workspace1", "test descrizione", "test categoria", LocalDate.parse("2020-01-08"));
            Workspace workspace2 = new Workspace("3","Test workspace2", "test descrizione", "test categoria", LocalDate.parse("2020-01-08"));
            Association association = new Association("test", "test", "test");

            String generatedSecuredPasswordHash = Auth.generateStorngPasswordHash("test");

            person.setPassword(generatedSecuredPasswordHash);
            person2.setPassword(Auth.generateStorngPasswordHash("test2"));

            ArrayList<User> users = new ArrayList<User>();
            users.add(person);
            users.add(person2);

            ArrayList<Workspace> workspaces = new ArrayList<Workspace>();
            workspaces.add(workspace);
            workspaces.add(workspace1);
            workspaces.add(workspace2);

            ArrayList<Association> associations = new ArrayList<Association>();
            associations.add(association);

            JsonParser<Workspace> parserWK = new JsonParser<Workspace>();
            parserWK.writeOnJson("data/Workspace.json", workspaces);

            JsonParser<User> parser = new JsonParser<User>();
            parser.writeOnJson("data/Users.json", users);

            JsonParser<Association> parserAs = new JsonParser<Association>();
            parserAs.writeOnJson("data/Associations.json", associations);

            User person3 = new User("Pincos", "Pallo", "test3", "", false);
            ArrayList<User> test = parser.readOnJson("data/Users.json", User[].class);
            
            if (!User.userExist(person3.getUsername())){
                test.add(person3);

                parser.writeOnJson("data/Users.json", test);
            }

            for (User user : test) {
                System.out.println(user.getUsername());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
