package com.example.workinghours.ui.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.workinghours.R;
import com.example.workinghours.database.entity.ProjectEntity;
import com.example.workinghours.ui.BaseActivity;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.viewmodel.project.ProjectViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class AddProjectPage extends BaseActivity {

    private static final String TAG = "EditProjectName";

    private ProjectEntity project;
    private String user;
    private boolean isEditMode;
    private Toast toast;
    private EditText etProjectName;

    private ProjectViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.project_edit_account, frameLayout);

        navigationView.setCheckedItem(position);

        user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        etProjectName = findViewById(R.id.textView_ProjectName);
        etProjectName.requestFocus();
        Button saveBtn = findViewById(R.id.createProjectButton);
        saveBtn.setOnClickListener(view -> {
            saveChanges(etProjectName.getText().toString());
            onBackPressed();
            toast.show();
        });

        String projectId = getIntent().getStringExtra("projectId");
        if (projectId == null) {
            setTitle(getString(R.string.project_create));
            toast = Toast.makeText(this, getString(R.string.project_created), Toast.LENGTH_LONG);
            isEditMode = false;
        } else {
            setTitle(getString(R.string.title_project_edit));
            saveBtn.setText(R.string.save_changes);
            toast = Toast.makeText(this, getString(R.string.project_edited), Toast.LENGTH_LONG);
            isEditMode = true;
        }

        ProjectViewModel.Factory factory = new ProjectViewModel.Factory(
                getApplication(), projectId);
        viewModel = new ViewModelProvider(this, factory).get(ProjectViewModel.class);
        if (isEditMode) {
            viewModel.getProject().observe(this, projectEntity -> {
                if (projectEntity != null) {
                    project = projectEntity;
                    etProjectName.setText(project.getProjectName());
                }
            });
        }


    }

    private void saveChanges(String projectName) {
        if (isEditMode) {
            if(!"".equals(projectName)) {
                project.setProjectName(projectName);
                viewModel.updateProject(project, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "updateProject: success");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "updateProject: failure", e);
                    }
                });
            }
        } else {
            ProjectEntity newProject = new ProjectEntity();
            newProject.setUser(user);
            newProject.setProjectName(projectName);

            viewModel.createProject(newProject, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "createProject: success");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "createProject: failure", e);
                }
            });
        }
    }
}
