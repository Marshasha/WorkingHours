package com.example.workinghours.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.workinghours.database.entity.ActivityEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityListLiveData extends LiveData<List<ActivityEntity>> {

    private static final String TAG = "ActivityListLiveData";

    private final DatabaseReference reference;
    private final String projectId;
    private final MyValueEventListener listener = new MyValueEventListener();

    public ActivityListLiveData(DatabaseReference ref, String projectId) {
        reference = ref;
        this.projectId = projectId;
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
        public void onDataChange(@NonNull DataSnapshot snapshot) {
           setValue(toActivities(snapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, "Can't listen to query " + reference, error.toException());
        }
    }

    private List<ActivityEntity> toActivities (DataSnapshot snapshot){
        List<ActivityEntity> activities = new ArrayList<>();
        for(DataSnapshot childSnapshot : snapshot.getChildren()){
            ActivityEntity entity = childSnapshot.getValue(ActivityEntity.class);
            entity.setActivityId(childSnapshot.getKey());
            entity.setProjectId(projectId);
            activities.add(entity);
        }
        return activities;
    }
}
