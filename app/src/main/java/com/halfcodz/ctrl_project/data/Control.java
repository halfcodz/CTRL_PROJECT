package com.halfcodz.ctrl_project.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "controls")
public class Control {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(name = "category_name")
    private String categoryName;

    @ColumnInfo(name = "control_item")
    private String controlItem;

    @ColumnInfo(name = "is_checked")
    private boolean isChecked;

    @ColumnInfo(name = "new_column")
    private String newColumn;

    // 기본 생성자
    public Control() {
    }

    // categoryName과 controlItem을 설정하는 생성자
    public Control(String categoryName, String controlItem) {
        this.categoryName = categoryName;
        this.controlItem = controlItem;
    }

    // controlItem만 설정하는 생성자
    public Control(String controlItem) {
        this.controlItem = controlItem;
    }

    // Getter 및 Setter 메서드
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getControlItem() {
        return controlItem;
    }

    public void setControlItem(String controlItem) {
        this.controlItem = controlItem;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getNewColumn() {
        return newColumn;
    }

    public void setNewColumn(String newColumn) {
        this.newColumn = newColumn;
    }
}