package com.example.workinghours.database.async.activity;

import android.app.Application;
import android.os.AsyncTask;

import com.example.workinghours.database.entity.ActivityEntity;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.BaseApp;

public class CreateActivity extends AsyncTask<ActivityEntity, Void, Void> {
    private Application application;
    private OnAsyncEventListener callback;
    private Exception exception;

    public CreateActivity(Application app, OnAsyncEventListener listener){
        this.application=app;
        this.callback=listener;
    }
    // What is this?
    protected Void doInBackground(ActivityEntity...params){
        try{
            for(ActivityEntity activity : params)
                ((BaseApp) application).getDatabase().activityDao().insert(activity);

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
