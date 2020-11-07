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
    public String user;

    @Ignore
    public ProjectEntity() {
    }

    public ProjectEntity(@NonNull String projectName, String user) {

        this.projectName=projectName;
        this.user=user;
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
