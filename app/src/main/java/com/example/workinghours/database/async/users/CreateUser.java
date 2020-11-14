package com.example.workinghours.database.async.users;

import android.app.Application;
import android.os.AsyncTask;

import com.example.workinghours.database.entity.UserEntity;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.BaseApp;

public class CreateUser extends AsyncTask<UserEntity, Void, Void> {

    private Application application;
    private OnAsyncEventListener callback;
    private Exception exception;

    public CreateUser(Application app, OnAsyncEventListener listener){
        this.application=app;
        this.callback=listener;
    }

    @Override
    protected Void doInBackground(UserEntity... users) {
        try{
            for(UserEntity userEntity : users)
                ((BaseApp) application).getDatabase().userDao().insert(userEntity);

        }catch(Exception e){
            exception = e;
        }

        return null;
    }

    protected void onPostExecute(Void aVoid){
        if(callback != null){
            if(exception == null){
                callback.onSuccess();
            }else{
                callback.onFailure(exception);
            }
        }
    }
}
