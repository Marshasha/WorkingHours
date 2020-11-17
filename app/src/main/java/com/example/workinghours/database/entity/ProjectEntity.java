package com.example.workinghours.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "projects",
        foreignKeys =
        @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "email",
                childColumns = "user",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(
                        value = {"user"}
                )}
)
public class ProjectEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "projectName")
    @NonNull
    private String projectName;
    private String user;

    @Ignore
    public ProjectEntity() {
    }

    public ProjectEntity(@NonNull String projectName, String user) {

        this.projectName=projectName;
        this.user=user;
    }

    @Ignore
    public ProjectEntity(@NonNull Long id, @NonNull String projectName, String user) {
        this.id = id;
        this.projectName=projectName;
        this.user=user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(@NonNull String projectName) {
        this.projectName = projectName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return  projectName;
    }

    public void setName(String projectName) {
        this.projectName=projectName;
    }
}
