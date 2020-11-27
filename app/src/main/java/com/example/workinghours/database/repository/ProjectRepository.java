package com.example.workinghours.database.repository;

import androidx.lifecycle.LiveData;

import com.example.workinghours.database.entity.ProjectEntity;
import com.example.workinghours.database.firebase.ProjectListLiveData;
import com.example.workinghours.database.firebase.ProjectLiveData;
import com.example.workinghours.util.OnAsyncEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public LiveData<List<ProjectEntity>> getProjectsByUser(final String user){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(user)
                .child("projects");
        return new ProjectListLiveData(reference, user);
    }

    /*
    public LiveData<ProjectEntity> getProjectByName(final String projectName) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("projects")
                .child(projectName);
        return new ProjectLiveData(reference); */

    public LiveData<ProjectEntity> getProjectById(final String projectId){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("projects")
                .child(projectId);
        return new ProjectLiveData(reference);
    }


    public void insert(final ProjectEntity project, final OnAsyncEventListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(project.getUser())
                .child("projects");
        String key = reference.push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(project.getUser())
                .child("projects")
                .child(key)
                .setValue(project, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final ProjectEntity project, OnAsyncEventListener callback){
           FirebaseDatabase.getInstance()
           .getReference("users")
           .child(project.getUser())
           .child("projects")
           .child(project.getId())
           .updateChildren(project.toMap(), (databaseError, databaseReference) -> {
               if (databaseError != null) {
                   callback.onFailure(databaseError.toException());
               } else {
                   callback.onSuccess();
               }
           });
    }

    public void delete(final ProjectEntity project, OnAsyncEventListener callback){
           FirebaseDatabase.getInstance()
           .getReference("users")
           .child(project.getUser())
           .child("projects")
           .child(project.getId())
           .removeValue((databaseError, databaseReference) -> {
               if (databaseError != null) {
                   callback.onFailure(databaseError.toException());
               } else {
                   callback.onSuccess();
               }
           }) ;
    }
}
