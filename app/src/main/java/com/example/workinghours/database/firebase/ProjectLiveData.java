package com.example.workinghours.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.workinghours.database.entity.ProjectEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ProjectLiveData extends LiveData<ProjectEntity> {

    private static final String TAG = "ProjectLiveData";

    private final DatabaseReference reference;
    private final String user;
    private final ProjectLiveData.MyValueEventListener listener = new ProjectLiveData.MyValueEventListener();

    public ProjectLiveData(DatabaseReference ref){
        reference = ref;
        user = ref.getParent().getParent().getKey();
    }

    @Override
    protected void onActive(){
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private class MyValueEventListener implements ValueEventListener{
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ProjectEntity entity = dataSnapshot.getValue(ProjectEntity.class);
            entity.setId(dataSnapshot.getKey());
            entity.setUser(user);
            setValue(entity);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }
}
