package com.example.workinghours.database.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.workinghours.database.AppDataBase;
import com.example.workinghours.database.async.project.CreateProject;
import com.example.workinghours.database.async.project.DeleteProject;
import com.example.workinghours.database.async.project.UpdateProject;
import com.example.workinghours.database.entity.ProjectEntity;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.BaseApp;

import java.util.List;

public class ProjectRepository {

    private static ProjectRepository instance;

    private ProjectRepository() {}

    public static ProjectRepository getInstance() {
        if (instance == null) {
            synchronized (ProjectRepository.class) {
                if (instance == null) {
                    instance = new ProjectRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<List<ProjectEntity>> getProjectsByUser(String user, Application app){
        return ((BaseApp) app).getDatabase().projectDao().getProjectsByUser(user);
    }

    public LiveData<ProjectEntity> getProjectByName(final String projectName, Application app) {
        return ((BaseApp) app).getDatabase().projectDao().getByName(projectName);
    }

    public LiveData<ProjectEntity> getProjectById(final Long id, Application app){
        return ((BaseApp)app).getDatabase().projectDao().getById(id);
    }

    public LiveData<List<ProjectEntity>> getAllProjects(Context context) {
        return AppDataBase.getInstance(context).projectDao().getAll();
    }

    public void insert(final ProjectEntity project, OnAsyncEventListener callback,
                       Application application) {
        new CreateProject(application, callback).execute(project);
    }

    public void update(final ProjectEntity project, OnAsyncEventListener callback,
                       Application application) {
        new UpdateProject(application, callback).execute(project);
    }

    public void delete(final ProjectEntity project, OnAsyncEventListener callback,
                       Application application) {
        new DeleteProject(application, callback).execute(project);
    }
}
