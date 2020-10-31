package com.example.workinghours.database.dao;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.workinghours.database.entity.ProjectEntity;

// How to check unique name of the project???

import java.util.List;

@Dao
public interface ProjectDao {

    @Query("SELECT * FROM projects")
    LiveData<List<ProjectEntity>> getAll();

    @Query("SELECT projectName FROM projects")
    LiveData<ProjectEntity> getByName(String projectName);

    @Insert
    String insert(ProjectEntity project) throws SQLiteConstraintException;

    @Update
    void update(ProjectEntity project);

    @Delete
    void delete(ProjectEntity project);

    @Query("DELETE FROM projects")
    void deleteAll();
}
