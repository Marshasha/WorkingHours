package com.example.workinghours.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.workinghours.database.entity.ActivityEntity;
import com.example.workinghours.database.firebase.ActivityListLiveData;
import com.example.workinghours.database.firebase.ActivityLiveData;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.BaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ActivityRepository {

    private static ActivityRepository instance;

    public static ActivityRepository getInstance() {
        if (instance == null) {
            synchronized (ActivityRepository.class) {
                if (instance == null) {
                    instance = new ActivityRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<ActivityEntity> getActivity(final String activityId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("projects")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()) // Is it valide???
                .child("activities")
                .child(activityId);
        return new ActivityLiveData(reference);
    }

    public LiveData<List<ActivityEntity>> getByProject(final String projectId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("projects")
                .child(projectId)
                .child("activities");
        return new ActivityListLiveData(reference, projectId);
    }

    public void insert(final ActivityEntity activity, OnAsyncEventListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("projects")
                .child(activity.getProjectId())
                .child("activities");
        String key = reference.push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("projects")
                .child(activity.getProjectId())
                .child("activities")
                .child(key)
                .setValue(activity, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final ActivityEntity activity, OnAsyncEventListener callback,
                       Application application) {
        new UpdateActivity(application, callback).execute(activity);
    }

    public void delete(final ActivityEntity activity, OnAsyncEventListener callback,
                       Application application) {
        new DeleteActivity(application, callback).execute(activity);
    }


}
