package com.example.workinghours.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workinghours.R;
import com.example.workinghours.database.entity.ProjectEntity;
import com.example.workinghours.viewmodel.project.ProjectListViewModel;

public class ProjectDetails extends AppCompatActivity {

    private static final String TAG = "ProjectDetails";

    private static final int EDIT_PROJECT = 1;
    private static final int DELETE_PROJECT = 2;

    private Toast statusToast;

    private boolean isEditable;

    private EditText editProjectName;
    private EditText editActivityName;
    private EditText editTimeSpent;
    private EditText editDate;

    private ProjectListViewModel viewModel;

    private ProjectEntity project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_project_page);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /** тут ты получаешь то имя проекта, что кладется в static void start*/
        String projectName = getIntent().getStringExtra("projectName");

        //  initiateView();

     /*   ProjectListViewModel.Factory factory = new ProjectListViewModel.Factory(getApplication(), projectName);
        viewModel = ViewModelProviders.of(this, factory).get(ProjectListViewModel.class);
        viewModel.getProject().observe(this, projectEntity -> {
            if (projectEntity != null) {
                project = projectEntity;
                updateContent();
            }
        }); */

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