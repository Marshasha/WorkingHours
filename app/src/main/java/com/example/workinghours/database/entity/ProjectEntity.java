package com.example.workinghours.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName="projects", primaryKeys = {"projectName"})
public class ProjectEntity {

    @ColumnInfo(name = "projectName")
    @NonNull
    private String projectName;

    @Ignore
    public ProjectEntity() {
    }

    public ProjectEntity(@NonNull String projectName) {
        this.projectName=projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(@NonNull String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return  projectName;
    }
}
