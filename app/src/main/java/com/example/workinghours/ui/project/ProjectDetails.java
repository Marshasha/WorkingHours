package com.example.workinghours.ui.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workinghours.R;
import com.example.workinghours.adapter.RecyclerAdapter;
import com.example.workinghours.database.entity.ActivityEntity;
import com.example.workinghours.database.entity.ProjectEntity;
import com.example.workinghours.ui.BaseActivity;
import com.example.workinghours.ui.activity.AddActivityInProject;
import com.example.workinghours.util.RecyclerViewItemClickListener;
import com.example.workinghours.viewmodel.activity.ActivityListViewModel;
import com.example.workinghours.viewmodel.project.ProjectViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetails extends BaseActivity {

    private static final String TAG = "ProjectDetails";

    private static final int EDIT_PROJECT = 1;
    private ProjectEntity project;
    private ProjectViewModel viewModelP;
    private View playButton;
    private View addButton;

    private List<ActivityEntity> activities;
    private RecyclerAdapter<ActivityEntity> adapter;
    private ActivityListViewModel viewModelA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** Project page initiation, while activities are not yet created */
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_project_page, frameLayout);

        navigationView.setCheckedItem(position);
        Long projectId = getIntent().getLongExtra("projectId", 0L);

        initiateView();

        ProjectViewModel.Factory factoryP = new ProjectViewModel.Factory(
                getApplication(), projectId);
        viewModelP = ViewModelProviders.of(this, factoryP).get(ProjectViewModel.class);
        viewModelP.getProject().observe(this, projectEntity -> {
            if(projectEntity != null){
                project = projectEntity;
                Log.i(TAG, "projectEntity is not null " + project.getUser());
                updateContent();

                ActivityListViewModel.Factory factoryA = new ActivityListViewModel.Factory(getApplication(), project.getId());
                viewModelA = ViewModelProviders.of(this, factoryA).get(ActivityListViewModel.class);
                viewModelA.getProjectActivities().observe(this, activityEntities -> {
                    if(activityEntities != null){
                        activities = activityEntities;
                        adapter.setData(activities);
                    }
                });
            }
        });

        /** 2nd part of the screen devoted to the list of activities bolonging to the project.
         * This list may be empty, if there are not yet activities added*/

        RecyclerView recyclerView = findViewById(R.id.activityRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        activities = new ArrayList<>();
        adapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "clicked activity : " + position);
                Log.d(TAG, "clicked on : " + activities.get(position).getActivityName());

                Intent intent = new Intent (ProjectDetails.this, AddActivityInProject.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                intent.putExtra("activityId", activities.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });



        recyclerView.setAdapter(adapter);
    }

    /** the methods for selected project presentation */

    private void updateContent(){
        if(project != null){
            setTitle(project.getProjectName());
            Log.i(TAG, "Project page is opened" + project.getProjectName());
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
      //  intent.putExtra("projectId", project.getId()); //??? NullPointerException
        startActivity(intent);
    }

    private void startChronometer(){

        Intent intent = new Intent(ProjectDetails.this, ProjectTrack.class);
    //    intent.putExtra("projectId", project.getId());
        startActivity(intent);
    }

    /** methods for the list of activities inside the project */

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

}