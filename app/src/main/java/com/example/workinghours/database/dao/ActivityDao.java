package com.example.workinghours.database.dao;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.workinghours.database.entity.ActivityEntity;

import java.util.List;

@Dao
public abstract class ActivityDao {

        @Query("SELECT * FROM activities WHERE projectId = :projectId")
        public abstract LiveData<List<ActivityEntity>> getByProject(Long projectId);

        @Query("SELECT * FROM activities WHERE activityId = :id")
        public abstract LiveData<ActivityEntity> getActivityById(int id);

        @Insert
        public abstract void insert (ActivityEntity activity);

        @Update
        public abstract void update (ActivityEntity activity);

        @Delete
        public abstract void delete(ActivityEntity activity);

}
