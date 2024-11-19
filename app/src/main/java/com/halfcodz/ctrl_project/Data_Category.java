package com.halfcodz.ctrl_project;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Data_Category {
    @PrimaryKey(autoGenerate = true)
    // 하나하나를 column으로 보면 되겠다
    private String category;

    //getter & setter 가져오거나 세팅을 하기위한 준비단계

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
