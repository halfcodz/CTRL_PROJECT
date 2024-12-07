package com.halfcodz.ctrl_project.data;

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

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and Setter for start_sch
    public String getStart_sch() {
        return start_sch;
    }

    public void setStart_sch(String start_sch) {
        this.start_sch = start_sch;
    }

    // Getter and Setter for end_sch
    public String getEnd_sch() {
        return end_sch;
    }

    public void setEnd_sch(String end_sch) {
        this.end_sch = end_sch;
    }
}