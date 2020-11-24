package com.example.workinghours.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.workinghours.database.entity.ProjectEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProjectListLiveData extends LiveData<List<ProjectEntity>> {

    private static final String TAG = "ProjectListLiveData";

    private final DatabaseReference reference;
    private final String user;
    private final MyValueEventListener listener = new MyValueEventListener();

    public ProjectListLiveData(DatabaseReference ref, String user){
        reference = ref;
        this.user=user;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toProjects(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<ProjectEntity> toProjects(DataSnapshot snapshot){
        List<ProjectEntity> projects = new ArrayList<>();
        for(DataSnapshot childSnapshot : snapshot.getChildren()){
            ProjectEntity entity = childSnapshot.getValue(ProjectEntity.class);
            entity.setId(childSnapshot.getKey());
            entity.setUser(user);
            projects.add(entity);
        }

        return projects;
    }
}
