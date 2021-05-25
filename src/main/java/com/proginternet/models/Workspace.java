package com.proginternet.models;

import java.util.ArrayList;

public class Workspace {
    
    private String id;
    private String name;
    private String description;
    private String category;
    private ArrayList<Activity> activities;


    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }
}
