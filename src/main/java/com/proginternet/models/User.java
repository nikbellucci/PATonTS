package com.proginternet.models;

import java.sql.Date;
import java.util.ArrayList;

import com.proginternet.utils.JsonParser;

public class User {
    private String name;
    private String surname;
    private String email;
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

    public static User checkUsername(String username) {

        String filename = "data/Users.json";

        User object = null;

        JsonParser<User> parser = new JsonParser<User>();
        ArrayList<User> users = parser.readOnJson(filename, User[].class);

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                object = user;
                break;
            }
        }
        
        return object;

    }

    public static boolean userExist(String username) {
        String filename = "data/Users.json";
        boolean usernameExist = false;

        JsonParser<User> parser = new JsonParser<User>();
        ArrayList<User> users = parser.readOnJson(filename, User[].class);

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                usernameExist = true;
                System.out.println("User exist: " + username);
                break;
            }
        }

        return usernameExist;
    }
}
