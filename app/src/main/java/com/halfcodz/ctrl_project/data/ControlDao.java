package com.halfcodz.ctrl_project.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ControlDao {

    // 카테고리 ID를 기준으로 통제 항목 가져오기
    @Query("SELECT * FROM controls WHERE category_Id = :categoryId")
    List<Control> getTodosByCategoryId(int categoryId);

    // 카테고리 이름으로 중복 확인 메서드 추가
    @Query("SELECT EXISTS(SELECT 1 FROM controls WHERE category_Name = :categoryName LIMIT 1)")
    boolean existsByCategoryName(String categoryName);

    // 카테고리 이름으로 통제 항목 가져오기
    @Query("SELECT * FROM controls WHERE category_Name = :categoryName")
    List<Control> getTodosByCategoryName(String categoryName);

    // 모든 Control 항목 가져오기
    @Query("SELECT * FROM controls")
    List<Control> getAllControls();

    // 모든 카테고리 가져오기 (중복 제거)
    @Query("SELECT * FROM controls GROUP BY category_Name")
    List<Control> getAllCategories();

    // 중복 확인: 카테고리 ID와 항목 이름 기준
    @Query("SELECT EXISTS(SELECT 1 FROM controls WHERE category_Id = :categoryId AND control_Item = :itemName LIMIT 1)")
    boolean existsByCategoryAndItem(int categoryId, String itemName);

    // null 값인 controlItem 삭제
    @Query("DELETE FROM controls WHERE control_Item IS NULL")
    void deleteControlsWithNullItems();

    // 모든 데이터 삭제
    @Query("DELETE FROM controls")
    void deleteAll();

    // 빈 이름 데이터 삭제
    @Query("DELETE FROM controls WHERE category_Name IS NULL OR category_Name = ''")
    void deleteControlsWithEmptyNames();

    // 모든 카테고리 이름 가져오기 (중복 제거)
    @Query("SELECT DISTINCT category_Name FROM controls WHERE category_Name IS NOT NULL AND category_Name != ''")
    List<String> getAllCategoryNames();

    // 단일 항목 삽입
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Control control);

    // 다중 항목 삽입
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<Control> controls);

    // 데이터 삭제
    @Delete
    void delete(Control control);

    // 데이터 업데이트
    @Update
    void update(Control control);

    // 첫 번째 Control ID 가져오기
    @Query("SELECT id FROM controls LIMIT 1")
    int getFirstControlId();
}
