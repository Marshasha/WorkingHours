package com.example.workinghours.database.entity;


import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ProjectEntity {

    private String projectId;
    private String projectName;
    private String user;

    public ProjectEntity() {
    }
    /*
    public ProjectEntity(@NonNull Long id, @NonNull String projectName, String user) {
        this.id = id;
        this.projectName=projectName; // Why we do not need the constructor?
        this.user=user;
    } */

    @Exclude
    public String getId() { return projectId; }

    public void setId(String id) { this.projectId = id; }

    public String getProjectName() { return projectName; }

    public void setProjectName(String projectName) { this.projectName = projectName; }

    @Exclude
    public String getUser() { return user; }

    public void setUser(String user) { this.user = user; }

    @Override
    public String toString() { return  projectName; }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("projectName", projectName);

        return result;
    }

}
