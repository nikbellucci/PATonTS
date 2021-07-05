package com.proginternet.models;

import java.time.LocalDate;
import java.util.ArrayList;

import com.proginternet.utils.JsonParser;

public class User {

    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;
    private LocalDate birthday;
    private boolean isAdmin;
    private Long chatId;
    private ArrayList<String> workspace;

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

    public User(String name, String surname, String username, String password, LocalDate birthday) {
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

    public String getSurname() {
        return this.surname;
    }

    public String getUsername() {
        return this.username;
    }
    public ArrayList<String> getWorkArray() {
        return this.workspace;
    }

    public Long getChat() {
        return this.chatId;
    }

    public void setChat(Long cId) {
        this.chatId=cId;
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

    public ArrayList<Workspace> getWorkspace(){
        String filename = "data/Workspace.json";

        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> ws = parser.readOnJson(filename, Workspace[].class);
        ArrayList<Workspace> result = new ArrayList<Workspace>();

        for (int i = 0; i < this.workspace.size(); i++) {
            for (Workspace works : ws) {
                if (works.getId().equals(this.workspace.get(i))) {
                    result.add(works);
                    System.out.println("workspace: " + works.getName());
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "User[nome=" + this.name + ", cognome="+ this.surname + ", username=" + this.username + "]";
    }
    
}
