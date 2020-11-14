package com.example.workinghours.database.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.workinghours.database.AppDataBase;
import com.example.workinghours.database.async.users.CreateUser;
import com.example.workinghours.database.async.users.DeleteUser;
import com.example.workinghours.database.async.users.UpdateUser;
import com.example.workinghours.database.entity.UserEntity;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.BaseApp;

import java.util.List;

public class UserRepository {

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

    public LiveData<UserEntity> getUser(final String email, Application app) {
        return ((BaseApp) app).getDatabase().userDao().getById(email);
    }

    public LiveData<List<UserEntity>> getAllUsers(Context context) {
        return AppDataBase.getInstance(context).userDao().getAll();
    }

    public void insert(final UserEntity userEntity, OnAsyncEventListener callback,
                       Application application) {
        new CreateUser(application, callback).execute(userEntity);
    }

    public void update(final UserEntity userEntity, OnAsyncEventListener callback,
                       Application application) {
        new UpdateUser(application, callback).execute(userEntity);
    }

    public void delete(final UserEntity userEntity, OnAsyncEventListener callback,
                       Application application) {
        new DeleteUser(application, callback).execute(userEntity);
    }
}
