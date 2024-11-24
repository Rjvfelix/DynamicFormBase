package com.example.dynamicform.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "form_data")
public class FormData {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "formId")
    public String formId;

    @ColumnInfo(name = "formTitle")
    public String formTitle;

    @ColumnInfo(name = "fieldName")
    public String fieldName;

    @ColumnInfo(name = "fieldValue")
    public String fieldValue;

    @ColumnInfo(name = "inserted_on", defaultValue = "0")
    public long insertedOn; // NOT NULL with a default value of 0

    @ColumnInfo(name = "synced", defaultValue = "0")
    public int synced; // NOT NULL with a default value of 0

    @ColumnInfo(name = "synced_on", defaultValue = "0")
    public long syncedOn; // NOT NULL with a default value of 0
}