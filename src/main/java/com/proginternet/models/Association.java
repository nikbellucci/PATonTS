package com.proginternet.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Association {

//     oggetto associazione

// -utente
// -id workspace
// -id attività
// -id preferenza
// -risposta preferenza

    private String user;
    private String workspaceId;
    private String activityId;
    private String preferenceId;
    private String preferenceAnswer;

    public Association(String user, String workspaceId, String activityId) {
        this.user = user;
        this.workspaceId = workspaceId;
        this.activityId = activityId;
        this.preferenceId = "";
        this.preferenceAnswer = "";
    }

    public Association(String user, String workspaceId, String activityId, String preferenceId, String preferenceAnswer) {
        this.user = user;
        this.workspaceId = workspaceId;
        this.activityId = activityId;
        this.preferenceId = preferenceId;
        this.preferenceAnswer = preferenceAnswer;
    }

    public String getActivityId() {
        return this.activityId;
    }

    public String getUser() {
        return user;
    }

    public String getPreferenceId() {
        return preferenceId;
    }

    public String getPreferenceAnswer() {
        return preferenceAnswer;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public Association(){}
    
    
}
