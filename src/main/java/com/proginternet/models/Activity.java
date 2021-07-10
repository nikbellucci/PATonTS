package com.proginternet.models;

import java.util.ArrayList;

public class Activity {
    
    private String id;
    private String name;
    private String description;
    private String where;
    private String when;
    private int availableSeats;
    private ArrayList<Preference> preferencies;

    public Activity(){
        id="";
        name="";
        description="";
        where="";
        when="";
        availableSeats=0;
        preferencies=new ArrayList<Preference>();
    }

    public Activity(String id, String name, String description, String where) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.where = where;
        this.preferencies = new ArrayList<Preference>();
    }

    public String getName(){
        return this.name;
    }

    public void addPref(Preference p){
        preferencies.add(p);
    }

    public ArrayList<Preference> getPreference() {
        return this.preferencies;
    }

    public String getId(){
        return this.id;
    }
}
