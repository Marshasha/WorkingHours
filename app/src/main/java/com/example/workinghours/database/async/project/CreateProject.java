package com.example.workinghours.database.async.project;

import android.app.Application;
import android.os.AsyncTask;

import com.example.workinghours.database.entity.ProjectEntity;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.BaseApp;

public class CreateProject extends AsyncTask<ProjectEntity, Void, Void> {

    private Application application;
    private OnAsyncEventListener callback;
    private Exception exception;

    public CreateProject(Application app, OnAsyncEventListener listener){
        this.application=app;
        this.callback=listener;
    }

    public Void doInBackground(ProjectEntity...params){
        try{
            for(ProjectEntity project : params)
                ((BaseApp) application).getDatabase().projectDao().insert(project);

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
