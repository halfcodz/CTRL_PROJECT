package com.inhatc.real_project.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todoitem")
public class TodoItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "start_sch")
    public String start_sch;

    @ColumnInfo(name = "end_sch")
    public String end_sch;

    public void setItemName(String item) {
    }

    public void setCategoryId(int categoryId) {
    }

    public long getId() {
        return 0;
    }
}