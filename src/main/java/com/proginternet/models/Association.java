package com.proginternet.models;

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
    }
    
}
