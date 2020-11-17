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
import com.example.workinghours.database.repository.ProjectRepository;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.BaseApp;

import java.util.List;

public class ActivityListViewModel extends AndroidViewModel {

    private Application application;

    private ActivityRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ActivityEntity>> observableProjectActivities;

    public ActivityListViewModel(@NonNull Application application,
                                final Long projectId,
                                ProjectRepository projectRepository,
                                ActivityRepository activityRepository) {
        super(application);

        this.application = application;

        repository = activityRepository;

        observableProjectActivities = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableProjectActivities.setValue(null);

        LiveData<List<ActivityEntity>> projectActivities = repository.getByProject(projectId, application);

        // observe the changes of the entities from the database and forward them
        observableProjectActivities.addSource(projectActivities, observableProjectActivities::setValue);
    }

    /**
     * A creator is used to inject the activity id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final Long projectId;

        private final ProjectRepository projectRepository;

        private final ActivityRepository activityRepository;

        public Factory(@NonNull Application application, Long projectId) {
            this.application = application;
            this.projectId = projectId;
            projectRepository = ((BaseApp) application).getProjectRepository();
            activityRepository = ((BaseApp) application).getActivityRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ActivityListViewModel(application, projectId, projectRepository, activityRepository);
        }
    }

    /**
     * Expose the LiveData ActivityEntities query so the UI can observe it.
     */
    public LiveData<List<ActivityEntity>> getProjectActivities() {
        return observableProjectActivities;
    }

    public void deleteActivity(ActivityEntity activity, OnAsyncEventListener callback) {
        repository.delete(activity, callback, application);
    }

}
