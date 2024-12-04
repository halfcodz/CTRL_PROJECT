package com.halfcodz.ctrl_project.data;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "focuslist")
public class Focus {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "focus_text")
    public String focus_text;

    @ColumnInfo(name = "focus_hour")
    public String focus_hour;

    @ColumnInfo(name = "focus_minutes")
    public String focus_minutes;

    @ColumnInfo(name = "focus_second")
    public String focus_second;

    public void setItemName(String item) {
    }

    public void setCategoryId(int categoryId) {
    }

    public long getId() {
        return 0;
    }
}