package com.example.workinghours.viewmodel.project;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.workinghours.database.entity.ProjectEntity;
import com.example.workinghours.database.repository.ProjectRepository;

import java.util.List;

public class ProjectListViewModel extends AndroidViewModel {

    private ProjectRepository repository;

    private Context applicationContext;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ProjectEntity>> observableProjects;

    public ProjectListViewModel(@NonNull Application application, ProjectRepository projectRepository) {
        super(application);

        repository=projectRepository;

        observableProjects = new MediatorLiveData<>();

        // set by default null, until we get data from the database.
        observableProjects.setValue(null);

        LiveData<List<ProjectEntity>> projects = repository.getAllProjects(applicationContext);

        // observe the changes of the entities from the database and forward them
        observableProjects.addSource(projects, observableProjects::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final ProjectRepository projectRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            projectRepository = ProjectRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ProjectListViewModel(application, projectRepository);
        }
    }

    /**
     * Expose the LiveData ProjectEntities query so the UI can observe it.
     */
    public LiveData<List<ProjectEntity>> getAllProjects() {
        return observableProjects;
    }
}
