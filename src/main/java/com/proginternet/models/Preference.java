package com.proginternet.models;

public class Preference {
    
    private String id;
    private String name;
    private String description;

    public Preference(){

    }

    public Preference(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Preference(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getDescrizione(){
        return this.description;
    }
}
