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

    public Workspace() {
        this.name = "";
        this.description = "";
        this.category = "";
        this.expiration = null;
        this.activities = new ArrayList<Activity>() ;
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

    public void setActivities(ArrayList<Activity> acts){
        this.activities=acts;
    }

    public void addActivity(Activity act){
        activities.add(act);
    }

    public void updateActivities(Activity act){
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).getId().equals(act.getId())) {
                activities.remove(i);
            }
        }
        activities.add(act);
    }

    public String getName(){
        return this.name;
    }
}