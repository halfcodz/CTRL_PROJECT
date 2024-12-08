
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

    @ColumnInfo(name = "categoryId") // **카테고리 ID 필드 추가**
    private int categoryId;

    @ColumnInfo(name = "time") // **시간 필드 추가**
    private String time;


    public String categoryName;

    // Getter와 Setter 추가
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

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

    // **Getter and Setter for categoryId**
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    // **Getter and Setter for time**
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}