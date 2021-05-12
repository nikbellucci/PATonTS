package com.proginternet.models;

import java.sql.Date;
import java.util.ArrayList;

import com.proginternet.utils.JsonParser;

public class User {
    private String name;
    private String surname;
    private String username;
    private String password;
    private Date birthday;
    private boolean isAdmin;

    public User() {
        //must be empty for Gson lib
    }

    public User(String name, String surname, String username, String password, boolean isAdmin) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public User(String name, String surname, String username, String password, Date birthday) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }

    // public boolean checkUsername(String username) {

    //     String filename = "data/Users.json";

    //     boolean usernameExist = false;

    //     JsonParser<User> parser = new JsonParser<User>();
    //     ArrayList<User> users = parser.readOnJson(filename);

    //     for (int i = 0; i < users.size(); i++) {
    //         // if (users.get(i).getUsername() == username) {
    //         //     usernameExist = true;
    //         //     System.out.println("User exist");
    //         // } else {
    //         //     usernameExist = false;
    //         // }   
    //     }
        
    //     return usernameExist;

    // }
}
