package com.example.dynamicform.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "form_data")
public class FormData {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "formId")
    public String formId; // Now a String to hold the MD5 hash

    public String formTitle; // Title of the form
    public String fieldName; // Name of the field
    public String fieldValue; // Value of the field
}
