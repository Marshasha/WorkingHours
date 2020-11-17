package com.example.workinghours.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.workinghours.database.converters.DateConverter;

import java.util.Date;

@Entity(tableName="activities", foreignKeys=@ForeignKey(entity=ProjectEntity.class,
                                            parentColumns="id",
                                            childColumns="projectId",
                                            onDelete = ForeignKey.CASCADE),
                                            indices = {@Index(value = {"projectId"})})
public class ActivityEntity {

    @PrimaryKey(autoGenerate = true)
    public int activityId;
    public String activityName;
    @TypeConverters({DateConverter.class})
    public Date dateStart;
    @TypeConverters({DateConverter.class})
    public Date dateFinish;
    public String duration;
    public Long projectId;

    public ActivityEntity(){}

    public ActivityEntity(@NonNull String activityName, Date dateStart, Date dateFinish, String duration, Long projectId) {
        this.activityName = activityName;
        this.dateStart=dateStart;
        this.dateFinish=dateFinish;
        this.duration=duration;
        this.projectId = projectId;

    }


    public String getDuration(){
       return duration;
    }

    public void setDuration(String duration){
        this.duration=duration;
    }

    public int getId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getDateStart(){
        return dateStart;
    }

    public void setDateStart(Date start){
        this.dateStart=start;
    }

    public Date getDateFinish(){
        return dateFinish;
    }

    public void setDateFinish(Date finish){
        this.dateFinish=finish;
    }

    public Long getProjectId(){
        return projectId;
    }

    public void setProjectId(Long ownerName){
        this.projectId =ownerName;
    }

    @Override
    public String toString() {
        return dateStart + " - " + dateFinish + " " + activityName + " " + duration;
    }

}