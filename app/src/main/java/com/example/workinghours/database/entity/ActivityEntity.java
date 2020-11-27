package com.example.workinghours.database.entity;

import androidx.room.TypeConverters;

import com.example.workinghours.database.converters.DateConverter;
import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ActivityEntity {

    public String activityId;
    public String activityName;
    @TypeConverters({DateConverter.class})
    public Date dateStart;
    @TypeConverters({DateConverter.class})
    public Date dateFinish;
    public String duration;
    public String projectId;

    public ActivityEntity(){}

    @Exclude
    public String getActivityId() { return activityId; }

    public void setActivityId(String id){this.activityId = id;}

    public String getDuration(){
       return duration;
    }

    public void setDuration(String duration){
        this.duration=duration;
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

    @Exclude
    public String getProjectId(){
        return projectId;
    }

    public void setProjectId(String projectId){
        this.projectId =projectId;
    }

    @Override
    public String toString() {
        return dateStart + " - " + dateFinish + " " + activityName + " " + duration;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActivityEntity)) return false;
        ActivityEntity o = (ActivityEntity) obj;
        return o.getActivityId().equals(this.getActivityId());
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("activityName", activityName);
        result.put("dataStart", dateStart);
        result.put("dataEnd", dateFinish);
        result.put("duration", duration);

        return result;
    }
}