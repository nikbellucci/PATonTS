package com.proginternet.utils;

import java.util.ArrayList;

import com.proginternet.models.User;

public class Singleton {
    
    private User user;
    private final static Singleton INSTANCE = new Singleton();
  
    private Singleton() {}
    
    public static Singleton getInstance() {
        return INSTANCE;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public User getUser() {
        return this.user;
    }

    public void removeUser() {
        this.user = null;
    }
}
