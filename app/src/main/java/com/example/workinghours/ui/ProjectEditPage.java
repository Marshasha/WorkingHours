package com.example.workinghours.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.example.workinghours.R;
import com.example.workinghours.database.entity.ActivityEntity;
import com.example.workinghours.util.OnAsyncEventListener;
import com.example.workinghours.viewmodel.activity.ActivityViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectEditPage extends BaseActivity {

    private static final int EDIT_ACTIVITY = 1;
    private static final int DELETE_ACTIVITY = 2;

    private Toast toast;

    private boolean isEditable;

    private EditText etProjectName;
    private EditText etDateStart;
    private EditText etDateEnd;
    private EditText etActivityName;
    private SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

    private ActivityViewModel viewModel;

    private ActivityEntity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.editActivity);
        navigationView.setCheckedItem(position);

        getLayoutInflater().inflate(R.layout.activity_project_edit_page, frameLayout);

        initiateView();

        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        int activityId = settings.getInt(PREFS_USER, 0);

        ActivityViewModel.Factory factory = new ActivityViewModel.Factory(getApplication(), activityId);
        viewModel = ViewModelProviders.of(this, factory).get(ActivityViewModel.class);
        viewModel.getActivity().observe(this, activityEntity -> {
            if(activityEntity!=null){
                activity= activityEntity;
                updateContent();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == BaseActivity.position) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
        /*
        The activity has to be finished manually in order to guarantee the navigation hierarchy working.
        */
        finish();
        return super.onNavigationItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, EDIT_ACTIVITY, Menu.NONE, getString(R.string.action_edit))
                .setIcon(R.drawable.ic_edit_white_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, DELETE_ACTIVITY, Menu.NONE, getString(R.string.action_delete))
                .setIcon(R.drawable.ic_delete_white_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == EDIT_ACTIVITY) {
            if (isEditable) {
                item.setIcon(R.drawable.ic_edit_white_24dp);
                switchEditableMode();
            } else {
                item.setIcon(R.drawable.ic_done_white_24dp);
                switchEditableMode();
            }
        }
        if (item.getItemId() == DELETE_ACTIVITY) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.action_delete));
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getString(R.string.delete_msg));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_delete), (dialog, which) -> {
                viewModel.deleteActivity(activity, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        alertDialog.setMessage(getString(R.string.delete_confirm_msg));
                    }

                    @Override
                    public void onFailure(Exception e) {}
                });
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchEditableMode() {
        if (!isEditable) {
            LinearLayout linearLayout = findViewById(R.id.activityDataLayout);
            linearLayout.setVisibility(View.VISIBLE);
            etProjectName.setFocusable(true);
            etProjectName.setEnabled(true);
            etProjectName.setFocusableInTouchMode(true);

            etDateStart.setFocusable(true);
            etDateStart.setEnabled(true);
            etDateStart.setFocusableInTouchMode(true);

            etDateEnd.setFocusable(true);
            etDateEnd.setEnabled(true);
            etDateEnd.setFocusableInTouchMode(true);

            etActivityName.setFocusable(true);
            etActivityName.setEnabled(true);
            etActivityName.setFocusableInTouchMode(true);
            etActivityName.requestFocus(); // For what???
        } else {
            try {
                saveChanges(

                        etProjectName.getText().toString(),
                        format.parse(etDateEnd.getText().toString()),
                        format.parse(etDateStart.getText().toString()),
                        etActivityName.getText().toString()

                );
            } catch (ParseException e) {
                e.printStackTrace();
            }
            LinearLayout linearLayout = findViewById(R.id.activityDataLayout);
            linearLayout.setVisibility(View.GONE);
            etProjectName.setFocusable(false);
            etProjectName.setEnabled(false);
            etDateStart.setFocusable(false);
            etDateStart.setEnabled(false);
            etDateEnd.setFocusable(false);
            etDateEnd.setEnabled(false);
            etActivityName.setFocusable(false);
            etActivityName.setEnabled(false);
        }
        isEditable = !isEditable;
    }

    private void saveChanges(String projectName, Date dateStart, Date dateEnd, String activityName) {

        activity.setOwner(projectName);
        activity.setDateStart(dateStart);
        activity.setDateFinish(dateEnd);
        activity.setActivityName(activityName);

        viewModel.updateActivity(activity, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                setResponse(false);
            }
        });
    }

    private void setResponse(Boolean response) {
        if (response) {
            updateContent();
            toast = Toast.makeText(this, getString(R.string.activity_edited), Toast.LENGTH_LONG);
            toast.show();
        } else {
            etActivityName.setError(getString(R.string.error_something_wrong));
            etActivityName.requestFocus();
        }
    }


    private void updateContent() {
        if (activity != null) {
            etProjectName.setText(activity.getOwner());
            etDateStart.setText(format.format(activity.getDateStart()));
            etDateEnd.setText(format.format(activity.getDateFinish()));
            etActivityName.setText(activity.getActivityName());
        }
    }

    private void initiateView() {
        isEditable = false;
        etProjectName = findViewById(R.id.textView_ProjectNameEdit);
        etDateStart = findViewById(R.id.textView_DateStartEdit);
        etDateEnd = findViewById(R.id.textView_DateEndEdit);
        etActivityName = findViewById(R.id.textView_ActivityNameEdit);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
