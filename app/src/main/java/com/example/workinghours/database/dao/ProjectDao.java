package com.example.workinghours.database.dao;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.workinghours.database.entity.ProjectEntity;


import java.util.List;

@Dao
public interface ProjectDao {

    @Query("SELECT * FROM projects WHERE user = :user")
    public abstract LiveData<List<ProjectEntity>> getProjectsByUser(String user);

    @Query("SELECT * FROM projects")
    LiveData<List<ProjectEntity>> getAll();

    @Query("SELECT * FROM projects WHERE projectName = :name")
    LiveData<ProjectEntity> getByName(String name);

    @Insert
    long insert(ProjectEntity project) throws SQLiteConstraintException;

    @Update
    void update(ProjectEntity project);

    @Delete
    void delete(ProjectEntity project);

    @Query("DELETE FROM projects")
    void deleteAll();

    @Query("SELECT * FROM projects WHERE id = :id")
    LiveData<ProjectEntity> getById(Long id);
}
