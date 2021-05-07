package com.proginternet;

public class User {
    String name;
    String surname;
    String password;

    public User() {
        //must be empty for Gson lib
    }

    public User(String name, String surname, String password){
        this.name = name;
        this.surname = surname;
        this.password = password;
    }
}
