package com.example.workinghours.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.workinghours.database.entity.ActivityEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ActivityLiveData extends LiveData<ActivityEntity> {

    private static final String TAG = "ActivityLiveData";

    private final DatabaseReference reference;
    private final String projectId;
    private final ActivityLiveData.MyValueEventListener listener = new ActivityLiveData.MyValueEventListener();

    public ActivityLiveData(DatabaseReference ref) {
        reference = ref;
        projectId = ref.getParent().getParent().getKey();
        Log.i(TAG, "ProjectId " + projectId);
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
            ActivityEntity entity = snapshot.getValue(ActivityEntity.class);
            entity.setActivityId(snapshot.getKey());
            entity.setProjectId(projectId);
            setValue(entity);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, "Can't listen to query " + reference, error.toException());
        }
    }
}
