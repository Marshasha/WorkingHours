package com.example.workinghours.database.async.users;

import android.app.Application;
import android.os.AsyncTask;

import com.example.workinghours.database.entity.UserEntity;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.BaseApp;

public class DeleteUser extends AsyncTask<UserEntity, Void, Void> {

    private Application application;
    private OnAsyncEventListener callback;
    private Exception exception;

    public DeleteUser(Application app, OnAsyncEventListener listener){
        this.application=app;
        this.callback=listener;
    }

    protected Void doInBackground(UserEntity...params){
        try{
            for(UserEntity user : params)
                ((BaseApp) application).getDatabase().userDao().delete(user);

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
