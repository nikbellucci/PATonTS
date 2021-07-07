package com.proginternet.models;

public class Activity {
    
    private String id;
    private String name;
    private String description;
    private String where;
    private String when;
    private int availableSeats;
    private Preference preferencies;

    public String getName(){
        return this.name;
    }
}
