package com.example.workinghours.ui;

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
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(new RecyclerAdapter<ProjectEntity>((text) -> {
            ProjectDetails.start(MainActivity.this, text);
        }));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}