package com.example.workinghours.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.example.workinghours.R;
import com.example.workinghours.database.entity.ActivityEntity;
import com.example.workinghours.ui.BaseActivity;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.viewmodel.activity.ActivityViewModel;

public class AddActivityInProject extends BaseActivity {


    private static final String TAG = "AddActivityInProject";

    private ActivityEntity activity;
    private Long projectId;
    private boolean isEditMode;
    private Toast toast;
    private EditText etActivityName;
    private ActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.add_activity_in_project, frameLayout);

        navigationView.setCheckedItem(position);

        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        projectId = settings.getLong(BaseActivity.PREFS_PROJECT, 0L);

        etActivityName = findViewById((R.id.textView_ActivityName));

        Button saveBtn = findViewById(R.id.createActivityButton);
        saveBtn.setOnClickListener(view -> {
            saveChanges(etActivityName.getText().toString());
            onBackPressed();
            toast.show();
        });

        int activityId = getIntent().getIntExtra("activityId", 0);
        if (activityId == 0) {
            setTitle(getString(R.string.activity_create));
            toast = Toast.makeText(this, getString(R.string.activity_created), Toast.LENGTH_LONG);
            isEditMode = false;
        } else {
            setTitle(getString(R.string.activity_edit));
            saveBtn.setText(R.string.save_changes);
            toast = Toast.makeText(this, getString(R.string.activity_edited), Toast.LENGTH_LONG);
            isEditMode = true;
        }

        ActivityViewModel.Factory factory = new ActivityViewModel.Factory(
                getApplication(), activityId);
        viewModel = ViewModelProviders.of(this, factory).get(ActivityViewModel.class);
        if (isEditMode) {
            viewModel.getActivity().observe(this, activityEntity -> {
                if (activityEntity != null) {
                    activity = activityEntity;
                    etActivityName.setText(activity.getActivityName());
                }
            });
        }
    }

    private void saveChanges(String activityName) {
        if (isEditMode) {
            if(!"".equals(activityName)) {
                activity.setActivityName(activityName);
                viewModel.updateActivity(activity, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "updateActivity: success");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "updateActivity: failure", e);
                    }
                });
            }
        } else {
            ActivityEntity newActivity = new ActivityEntity();
            newActivity.setProjectId(projectId);
            newActivity.setActivityName(activityName);

            viewModel.createActivity(newActivity, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "createActivity: success");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "createActivity: failure", e);
                }
            });
        }
    }
}
