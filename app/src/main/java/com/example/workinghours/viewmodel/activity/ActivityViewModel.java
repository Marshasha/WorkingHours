package com.example.workinghours.viewmodel.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.workinghours.database.entity.ActivityEntity;
import com.example.workinghours.database.repository.ActivityRepository;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.BaseApp;

public class ActivityViewModel extends AndroidViewModel {

    private Application application;

    private ActivityRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<ActivityEntity> observableActivity;

    public ActivityViewModel(@NonNull Application application,
                            final int activityId, ActivityRepository activityRepository) {
        super(application);

        this.application = application;

        repository = activityRepository;

        observableActivity = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableActivity.setValue(null);

        LiveData<ActivityEntity> activity = repository.getActivity(activityId, application);

        // observe the changes of the activity entity from the database and forward them
        observableActivity.addSource(activity, observableActivity::setValue);
    }

    /**
     * A creator is used to inject the activity id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final int activityId;

        private final ActivityRepository repository;

        public Factory(@NonNull Application application, int activityId) {
            this.application = application;
            this.activityId = activityId;
            repository = ((BaseApp) application).getActivityRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ActivityViewModel(application, activityId, repository);
        }
    }

    /**
     * Expose the LiveData ActivityEntity query so the UI can observe it.
     */
    public LiveData<ActivityEntity> getActivity() {
        return observableActivity;
    }

    public void createActivity(ActivityEntity activity, OnAsyncEventListener callback) {
        repository.insert(activity, callback, application);
    }

    public void updateActivity(ActivityEntity activity, OnAsyncEventListener callback) {
        repository.update(activity, callback, application);
    }

    public void deleteActivity(ActivityEntity activity, OnAsyncEventListener callback) {
        repository.delete(activity, callback, application);
    }
}
