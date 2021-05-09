package com.proginternet.models;

import java.sql.Date;

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

    public User(String name, String surname, String password, boolean isAdmin) {
        this.name = name;
        this.surname = surname;
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
        return password;
    }

    public String getName() {
        return name;
    }
}
