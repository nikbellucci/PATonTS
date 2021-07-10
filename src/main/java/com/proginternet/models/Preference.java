package com.proginternet.models;

public class Preference {
    
    private String name;
    private String description;

    public Preference(){

    }

    public Preference(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
}
