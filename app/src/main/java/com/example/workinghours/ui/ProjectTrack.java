package com.example.workinghours.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workinghours.R;

import java.util.Locale;

public class ProjectTrack extends AppCompatActivity {

    private TextView textViewTimer;

    private int seconds = 0;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_track);
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
