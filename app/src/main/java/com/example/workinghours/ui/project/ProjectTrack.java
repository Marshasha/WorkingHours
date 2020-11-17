package com.example.workinghours.ui.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.workinghours.R;
import com.example.workinghours.database.entity.ActivityEntity;
import com.example.workinghours.database.entity.ProjectEntity;
import com.example.workinghours.ui.BaseActivity;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.viewmodel.activity.ActivityViewModel;
import com.example.workinghours.viewmodel.project.ProjectViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProjectTrack extends BaseActivity {

    private static final String TAG = "ProjectTrack";

    private TextView textViewTimer;
    private Button startButton;
    private Button stopButton;
    private EditText input;
    private ProjectEntity project;
    private Long projectId;
    private Toast toast;
    private ProjectViewModel viewModelP;
    private ActivityViewModel viewModelA;

    private int seconds = 0;
    private boolean isRunning = false;

    private ActivityEntity activity = new ActivityEntity();
    private Date dateStart;
    private Date dateEnd;
    private String duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_project_track, frameLayout);
        navigationView.setCheckedItem(position);
        projectId = getIntent().getLongExtra("projectId", 0L);

        initiateView();

        ProjectViewModel.Factory factoryP = new ProjectViewModel.Factory(
                getApplication(), projectId);
        viewModelP = ViewModelProviders.of(this, factoryP).get(ProjectViewModel.class);
        viewModelP.getProject().observe(this, projectEntity -> {
            if(projectEntity != null){
                project = projectEntity;
                updateContent();
            }
        });

        int activityId = getIntent().getIntExtra("activityId", 0);

        ActivityViewModel.Factory factoryA = new ActivityViewModel.Factory(
                getApplication(), activityId);
        viewModelA = ViewModelProviders.of(this, factoryA).get(ActivityViewModel.class);
        viewModelA.getActivity().observe(this, activityEntity -> {
            if(activityEntity != null){
                activity = activityEntity;
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
        startButton = findViewById(R.id.button_start);
        stopButton = findViewById(R.id.button_stop);
        runTimer();
    }

    public void onClickStartTimer(View view) {
        dateStart = Calendar.getInstance().getTime();
        activity.setDateStart(dateStart);
        activity.setProjectId(projectId);
        isRunning = true;
    }

    public void onClickStopTimer(View view) {
        isRunning=false;
        createActivityNameDialog();
    }

    private void createActivityNameDialog(){
        dateEnd = Calendar.getInstance().getTime();
        activity.setDateFinish(dateEnd);
        duration = calculateDuration(dateStart, dateEnd);
        activity.setDuration(duration);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.activity_name, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.activityName);
        input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.create();
        builder.setView(input);
        builder.setCancelable(false);

        final EditText activityName = view.findViewById(R.id.activity_name);
        builder.setPositiveButton(getString(R.string.action_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                activity.setActivityName(name);

                viewModelA.createActivity(activity, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "updateActivity : success"); }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "updateActivity : failure"); }
                });
            }
        });

        builder.setNegativeButton( getString(R.string.action_cancel), new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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

    public String calculateDuration(Date start, Date finish){

        long different = finish.getTime() - start.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String duration = elapsedDays + " d" + elapsedHours + "h " + elapsedMinutes + " min" + elapsedSeconds + " sec";

        return duration;
    }
}
