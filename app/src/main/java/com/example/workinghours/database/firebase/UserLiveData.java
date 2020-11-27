package com.example.workinghours.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.workinghours.database.entity.UserEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UserLiveData extends LiveData<UserEntity> {

    private static final String TAG = "UserLiveData";

    private final DatabaseReference reference;
    private final UserLiveData.MyValueEventListener listener = new UserLiveData.MyValueEventListener();

    public UserLiveData(DatabaseReference reference) {
        this.reference = reference;
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

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            UserEntity entity = dataSnapshot.getValue(UserEntity.class);
            entity.setId(dataSnapshot.getKey());
            setValue(entity);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }
}
