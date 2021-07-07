package com.proginternet.models;

import java.time.LocalDate;
import java.util.ArrayList;

public class Workspace {
    
    private String id;
    private String name;
    private String description;
    private String category;
    private LocalDate expiration;
    private ArrayList<Activity> activities;

    public Workspace(String name, String description, String category, LocalDate expiration) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.expiration = expiration;
    }

    public String getId(){
        return this.id;
    }

    public String getDescrizione(){
        return this.description;
    }

    public String getExpiration(){
        return this.expiration.toString();
    }

    public ArrayList<Activity> getActivities(){
        return this.activities;
    }

    public String getName(){
        return this.name;
    }
}