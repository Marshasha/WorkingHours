package com.example.workinghours.ui.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import com.example.workinghours.R;
import com.example.workinghours.database.entity.ProjectEntity;
import com.example.workinghours.ui.BaseActivity;
import com.example.workinghours.ui.activity.AddActivityInProject;
import com.example.workinghours.viewmodel.project.ProjectListViewModel;
import com.example.workinghours.viewmodel.project.ProjectViewModel;

public class ProjectDetails extends BaseActivity {

    private static final String TAG = "ProjectDetails";

    private static final int EDIT_PROJECT = 1;
    private ProjectEntity project;
    private ProjectViewModel viewModel;
    private View playButton;
    private View addButton;
    private TextView projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_project_page, frameLayout);

        navigationView.setCheckedItem(position);

        Long projectId = getIntent().getLongExtra("projectId", 0L);

        initiateView();

        ProjectViewModel.Factory factory = new ProjectViewModel.Factory(
                getApplication(), projectId);
        viewModel = ViewModelProviders.of(this, factory).get(ProjectViewModel.class);
        viewModel.getProject().observe(this, projectEntity -> {
            if(projectEntity != null){
                project = projectEntity;
                updateContent();
            }
        });
    }

    private void updateContent(){
        if(project != null){
            setTitle(project.getProjectName());
            Log.i(TAG, "Project page is opened");
        }
    }

    private void initiateView() {

        playButton = findViewById(R.id.imagebutton_Play);
        addButton = findViewById(R.id.imageButtonAdd);

        playButton.setOnClickListener(view -> startChronometer());
        addButton.setOnClickListener(view -> addActivityInProject());
    }

    private void addActivityInProject(){
        Intent intent = new Intent (ProjectDetails.this, AddActivityInProject.class);
        intent.putExtra("projectId", project.getId());
        startActivity(intent);
    }

    private void startChronometer(){

        Intent intent = new Intent(ProjectDetails.this, ProjectTrack.class);
        intent.putExtra("projectId", project.getId());
        startActivity(intent);
    }

    public static void start(Activity activity, String project){
        /** внутренний статик метод для старта активити.
        На вход строка project - название проекта, для которого нужны элементы из бд */
        Intent intent = new Intent();
        intent.setClass(activity, ProjectDetails.class);
        intent.putExtra("projectName", project);
        activity.startActivity(intent);
    }
}