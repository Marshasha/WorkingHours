package com.example.workinghours.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.workinghours.R;
import com.example.workinghours.adapter.RecyclerAdapter;
import com.example.workinghours.database.entity.ProjectEntity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
     //   setContentView(R.layout.activity_main);

        setTitle(getString(R.string.title_main_activity));

     /*   RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(new RecyclerAdapter<ProjectEntity>((text) -> {
            ProjectDetails.start(MainActivity.this, text);
        }));

        recyclerView.setItemAnimator(new DefaultItemAnimator()); */
        navigationView.setCheckedItem(R.id.nav_none);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(getString(R.string.app_name));
        navigationView.setCheckedItem(R.id.nav_none);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.action_logout));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getString(R.string.question_logout));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_logout), (dialog, which) -> logout());
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }
}