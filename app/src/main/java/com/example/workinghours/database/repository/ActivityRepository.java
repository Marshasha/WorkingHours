package com.example.workinghours.database.repository;

import android.app.Application;
import android.util.Pair;

import androidx.lifecycle.LiveData;

import com.example.workinghours.database.async.activity.CreateActivity;
import com.example.workinghours.database.async.activity.DeleteActivity;
import com.example.workinghours.database.async.activity.UpdateActivity;
import com.example.workinghours.database.entity.ActivityEntity;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.viewmodel.BaseApp;

import java.util.List;

public class ActivityRepository {

    private static ActivityRepository instance;

    private ActivityRepository() {

    }

    public static ActivityRepository getInstance() {
        if (instance == null) {
            synchronized (ActivityRepository.class) {
                if (instance == null) {
                    instance = new ActivityRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<ActivityEntity> getActivity(final int activityId, Application application) {
        return ((BaseApp) application).getDatabase().activityDao().getActivityById(activityId);
    }

    public LiveData<List<ActivityEntity>> getByProject(final String projectName, Application application) {
        return ((BaseApp) application).getDatabase().activityDao().getOwned(projectName);
    }

    public void insert(final ActivityEntity activity, OnAsyncEventListener callback,
                       Application application) {
        new CreateActivity(application, callback).execute(activity);
    }

    public void update(final ActivityEntity activity, OnAsyncEventListener callback,
                       Application application) {
        new UpdateActivity(application, callback).execute(activity);
    }

    public void delete(final ActivityEntity activity, OnAsyncEventListener callback,
                       Application application) {
        new DeleteActivity(application, callback).execute(activity);
    }


}
