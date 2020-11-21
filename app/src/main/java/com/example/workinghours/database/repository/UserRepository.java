package com.example.workinghours.database.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.workinghours.database.firebase.UserLiveData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.util.Log;

import com.example.workinghours.database.AppDataBase;
import com.example.workinghours.database.entity.UserEntity;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.BaseApp;


import java.util.List;

public class UserRepository {

    private static final String TAG = "UserRepository";

    private static UserRepository instance;

    private UserRepository() {}

    public static UserRepository getInstance() {
        if (instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new UserRepository();
                }
            }
        }
        return instance;
    }

    public void signIn(final String email, final String password,
                       final OnCompleteListener<AuthResult> listener){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public LiveData<UserEntity> getUser(final String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId);
        return new UserLiveData(reference);
    }

    public void register(final UserEntity user, final OnAsyncEventListener callback){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                user.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                insert(user, callback);
            }else{
                callback.onFailure(task.getException());
            }
        });
    }

    public void insert(final UserEntity userEntity, OnAsyncEventListener callback){
             FirebaseDatabase.getInstance()
             .getReference("clients")
             .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
             .setValue(userEntity, (databaseError, databaseReference) -> {
                 if(databaseError != null){
                     callback.onFailure(databaseError.toException());
                     FirebaseAuth.getInstance().getCurrentUser().delete()
                             .addOnCompleteListener(task -> {
                                 if(task.isSuccessful()){
                                     callback.onFailure(null);
                                     Log.d(TAG, "Rollback successful: User account deleted");
                                 }else{
                                     callback.onFailure(task.getException());
                                     Log.d(TAG, "Rollback failed: signInWithEmail:failure",
                                             task.getException());
                                 }
                             });
                 }else{
                     callback.onSuccess();
                 }
             }) ;
    }

    public void update(final UserEntity userEntity, OnAsyncEventListener callback){
            FirebaseDatabase.getInstance()
            .getReference("users")
            .child(userEntity.getId())
            .updateChildren(userEntity.toMap(), (databaseError, databaseReference) -> {
                if (databaseError != null) {
                    callback.onFailure(databaseError.toException());
                } else {
                    callback.onSuccess();
                }
            });

            FirebaseAuth.getInstance().getCurrentUser().updatePassword(userEntity.getPassword())
                    .addOnFailureListener(e -> Log.d(TAG, "updatePassword failure!", e));
    }

    public void delete(final UserEntity userEntity, OnAsyncEventListener callback){
          FirebaseDatabase.getInstance()
          .getReference("users")
          .child(userEntity.getId())
          .removeValue((databaseError, databaseReference) -> {
              if (databaseError != null) {
                  callback.onFailure(databaseError.toException());
              } else {
                  callback.onSuccess();
              }
          });
    }
}
