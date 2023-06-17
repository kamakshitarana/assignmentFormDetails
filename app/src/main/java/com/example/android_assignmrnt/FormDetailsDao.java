package com.example.android_assignmrnt;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FormDetailsDao {
    @Insert
    void insert(FormDetails formDetails);
    @Query("SELECT * FROM form_details")
    List<FormDetails> getAllFormDetails();
}