package com.halfcodz.ctrl_project;

import android.icu.util.ULocale;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.Locale;

//Data Access Object
@Dao
public interface DAO_Category {

    @Insert //삽입
    void setInsertCategory(Data_Category category);

    @Update //수정
    void setUpdateCategory(Data_Category category);

    @Delete //삭제
    void setDeleteCategory(Data_Category category);

//    쿼리: 데이터베이스에 요청하는 명령문
//    더 다양하게 관리하고 싶으면 Query문을 더 공부하면 된다.
    @Query("SELECT * FROM data_category")
    List<Data_Category> getCategoryAll();



}
