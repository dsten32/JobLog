package com.comp575.myapplication;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface JobDao {
    @Insert
    void insert(Job job);
    @Update
    void update(Job job);
    @Delete
    void delete(Job job);
    @Query("SELECT * from Job ORDER BY client")
    LiveData<List<Job>> getAllJobs();
}
