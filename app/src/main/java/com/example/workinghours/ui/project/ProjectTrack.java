package com.example.workinghours.ui.project;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.workinghours.R;
import com.example.workinghours.database.entity.ProjectEntity;
import com.example.workinghours.ui.BaseActivity;
import com.example.workinghours.viewmodel.project.ProjectViewModel;

import java.util.Locale;

public class ProjectTrack extends BaseActivity {

    private static final String TAG = "ProjectTrack";

    private TextView textViewTimer;
    private ProjectEntity project;
    private ProjectViewModel viewModel;

    private int seconds = 0;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_project_track, frameLayout);
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


    private void initiateView(){
        textViewTimer = findViewById(R.id.textView_Timer);
        runTimer();
    }

    public void onClickStartTimer(View view) {
        isRunning = true;
    }

    public void onClickStopTimer(View view) {
        isRunning=false;
    }


    private void runTimer(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds/3600;
                int minutes = (seconds%3600)/60;
                int secs = seconds%60;

                String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);
                textViewTimer.setText(time);

                if(isRunning){
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });

    }
}
