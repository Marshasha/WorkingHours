package com.example.workinghours;

import android.app.Application;

import com.example.workinghours.database.AppDataBase;
import com.example.workinghours.database.repository.ActivityRepository;
import com.example.workinghours.database.repository.ProjectRepository;
import com.example.workinghours.database.repository.UserRepository;

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppDataBase getDatabase() {
        return AppDataBase.getInstance(this);
    }

    public UserRepository getUserRepository(){ return UserRepository.getInstance();}

    public ProjectRepository getProjectRepository() {
        return ProjectRepository.getInstance();
    }

    public ActivityRepository getActivityRepository() {
        return ActivityRepository.getInstance();
    }
}
