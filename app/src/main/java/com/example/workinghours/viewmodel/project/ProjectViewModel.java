package com.example.workinghours.viewmodel.project;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.workinghours.database.entity.ProjectEntity;
import com.example.workinghours.database.repository.ProjectRepository;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.BaseApp;

public class ProjectViewModel extends AndroidViewModel {

    private ProjectRepository repository;

    private Application application;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<ProjectEntity> observableProject;

    public ProjectViewModel(@NonNull Application application, final Long projectId,
                            ProjectRepository projectRepository) {
        super(application);

        this.application = application;

        repository = projectRepository;

        observableProject = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableProject.setValue(null);

        LiveData<ProjectEntity> project = repository.getProjectById(projectId, application);

        // observe the changes of the project entity from the database and forward them
        observableProject.addSource(project, observableProject::setValue);
    }

    /**
     * A creator is used to inject the activity id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final Long projectId;

        private final ProjectRepository repository;

        public Factory(@NonNull Application application, Long id) {
            this.application = application;
            this.projectId = id;
            repository = ((BaseApp) application).getProjectRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ProjectViewModel(application, projectId, repository);
        }
    }

    /**
     * Expose the LiveData ProjectEntity query so the UI can observe it.
     */
    public LiveData<ProjectEntity> getProject() {
        return observableProject;
    }

    public void createProject(ProjectEntity project, OnAsyncEventListener callback) {
        repository.insert(project, callback, application);
    }

    public void updateProject(ProjectEntity project, OnAsyncEventListener callback) {
        repository.update(project, callback, application);
    }

    public void deleteProject(ProjectEntity project, OnAsyncEventListener callback) {
        repository.delete(project, callback, application);

    }
}
