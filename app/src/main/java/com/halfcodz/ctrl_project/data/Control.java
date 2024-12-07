package com.halfcodz.ctrl_project.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "controls")
public class Control {
    @PrimaryKey(autoGenerate = true)
    private int id;     // 기본 key
    @ColumnInfo
    private int categoryId; // 카테고리 ID (외래 키)
    @ColumnInfo
    private String Category_Name;    // 카테고리 이름
    @ColumnInfo
    private String Control_Item;     // 통제 항목 이름

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_Name() {
        return Category_Name;
    }

    public void setCategory_Name(String category_Name) {
        Category_Name = category_Name;
    }

    public String getControl_Item() {
        return Control_Item;
    }

    public void setControl_Item(String control_Item) {
        Control_Item = control_Item;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    private boolean isChecked;  // 체크박스 상태

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}