package com.halfcodz.ctrl_project.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ControlDao {

    // 카테고리 ID를 기준으로 통제 항목 가져오기
    @Query("SELECT * FROM controls WHERE categoryId = :categoryId")
    List<Control> getTodosByCategoryId(int categoryId);

    // 특정 카테고리 이름 가져오기
    @Query("SELECT Category_Name FROM controls WHERE id = :categoryId")
    String getCategory_Name(int categoryId);

    // 모든 카테고리 이름 가져오기
    @Query("SELECT DISTINCT Category_Name FROM controls WHERE Category_Name IS NOT NULL AND Category_Name != ''")
    List<String> getAllCategoryNames();

    // 중복 확인: 카테고리 ID와 항목 이름 기준
    @Query("SELECT EXISTS(SELECT 1 FROM controls WHERE categoryId = :categoryId AND Control_Item = :itemName LIMIT 1)")
    boolean existsByCategoryAndItem(int categoryId, String itemName);

    // 빈 이름 데이터 삭제
    @Query("DELETE FROM controls WHERE Category_Name IS NULL OR Category_Name = ''")
    void deleteControlsWithEmptyNames();

    // 데이터 삽입
    @Insert
    long insert(Control control);

    // 데이터 삭제
    @Delete
    void delete(Control controls);

    // 데이터 업데이트
    @Update
    void update(Control control);

    // 사용자 ID를 가져오는 메서드
    @Query("SELECT id FROM controls LIMIT 1")
    int getFirstControlId();
}