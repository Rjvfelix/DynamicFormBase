package com.example.dynamicform.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dynamicform.models.FormData;
import com.example.dynamicform.models.FormSummary;

import java.util.List;

@Dao
public interface FormDataDao {
    @Insert
    void insert(FormData formData);

    @Query("SELECT * FROM form_data WHERE formTitle = :formTitle ORDER BY inserted_on DESC")
    List<FormData> getFormData(String formTitle);

    @Query("SELECT DISTINCT formId, formTitle FROM form_data")
    List<FormSummary> getFormSummaries(); // Retrieve all unique forms (formId and title)

    @Query("SELECT * FROM form_data WHERE formId = :formId")
    List<FormData> getFormDataByFormId(int formId); // Retrieve fields of a specific form instance

    @Query("UPDATE form_data SET synced = :synced, synced_on = :syncedOn WHERE formId = :formId")
    void markAsSynced(String formId, boolean synced, long syncedOn);

    @Query("SELECT * FROM form_data WHERE synced = 0")
    List<FormData> getUnsyncedData();

}
