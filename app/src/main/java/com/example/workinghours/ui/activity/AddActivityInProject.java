package com.example.workinghours.ui.activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.example.workinghours.R;
import com.example.workinghours.database.entity.ActivityEntity;
import com.example.workinghours.ui.BaseActivity;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.viewmodel.activity.ActivityViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActivityInProject extends BaseActivity {


    private static final String TAG = "AddActivityInProject";

    private ActivityEntity activity;
    private Long projectId;
    private boolean isEditMode;
    private Toast toast;
    private EditText etActivityName;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private TextView startTimeTitle;
    private TextView endTimeTitle;
    private String durationOfActivity;
    private ActivityViewModel viewModel;
    private final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.add_activity_in_project, frameLayout);

        navigationView.setCheckedItem(position);

        projectId = getIntent().getLongExtra("projectId", 0L);

        etActivityName = findViewById(R.id.textView_ActivityName);
        startTimePicker = findViewById(R.id.startTime);
        startTimePicker.setIs24HourView(true);
        startTimeTitle = findViewById(R.id.title_startTime);
        endTimePicker = findViewById(R.id.endTime);
        endTimePicker.setIs24HourView(true);
        endTimeTitle = findViewById(R.id.title_endTime);
        etActivityName.requestFocus();

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
        myCalendar.set(Calendar.HOUR_OF_DAY, startTimePicker.getHour());
        myCalendar.set(Calendar.MINUTE, startTimePicker.getMinute());
        Date startDate = myCalendar.getTime();
        myCalendar.set(Calendar.HOUR_OF_DAY, endTimePicker.getHour());
        myCalendar.set(Calendar.MINUTE, endTimePicker.getMinute());
        Date endDate = myCalendar.getTime();
        durationOfActivity = calculateDuration(startDate, endDate);

        if (isEditMode) {
            if(!"".equals(activityName)) {
                activity.setActivityName(activityName);
                activity.setDateStart(startDate);
                activity.setDateFinish(endDate);
                activity.setDuration(durationOfActivity);

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
            newActivity.setDateStart(startDate);
            newActivity.setDateFinish(endDate);
            newActivity.setDuration(durationOfActivity);

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

        String duration = elapsedDays + " d" + elapsedHours + " h" + elapsedMinutes + " min" + elapsedSeconds + " sec";

        return duration;
    }
}
