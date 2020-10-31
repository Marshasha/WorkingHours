package com.example.workinghours.viewmodel;

import android.app.Application;

import com.example.workinghours.database.AppDataBase;
import com.example.workinghours.database.repository.ActivityRepository;
import com.example.workinghours.database.repository.ProjectRepository;

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppDataBase getDatabase() {
        return AppDataBase.getInstance(this);
    }

    public ProjectRepository getProjectRepository() {
        return ProjectRepository.getInstance();
    }

    public ActivityRepository getActivityRepository() {
        return ActivityRepository.getInstance();
    }
}
