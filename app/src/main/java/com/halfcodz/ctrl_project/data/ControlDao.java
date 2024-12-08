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
    @Query("SELECT * FROM control WHERE category_id = :categoryId")
    List<Control> getTodosByCategoryId(int categoryId);


    // 카테고리 이름으로 중복 확인 메서드 추가
    @Query("SELECT EXISTS(SELECT 1 FROM control WHERE category_name = :categoryName LIMIT 1)")
    boolean existsByCategoryName(String categoryName);
    @Query("DELETE FROM control WHERE control_item IS NULL")
    void deleteControlswithNullItems();

    // 카테고리 이름으로 통제 항목 가져오기
    @Query("SELECT * FROM control WHERE category_name = :categoryName")
    List<Control> getTodosByCategoryName(String categoryName);

    // 모든 Control 항목을 가져오는 메서드
    @Query("SELECT * FROM control")
    List<Control> getAllControls();

    @Query("SELECT * FROM control GROUP BY category_name")
    List<Control> getAllCategories();

    // 중복 확인: 카테고리 ID와 항목 이름 기준
    @Query("SELECT EXISTS(SELECT 1 FROM control WHERE category_id = :categoryId AND control_item = :itemName LIMIT 1)")
    boolean existsByCategoryAndItem(int categoryId, String itemName);




    @Query("DELETE FROM control")
    void deleteAll();

    // 빈 이름 데이터 삭제
    @Query("DELETE FROM control WHERE category_name IS NULL OR category_name = ''")
    void deleteControlsWithEmptyNames();

    // 모든 카테고리 이름 가져오기 (중복 제거)
    @Query("SELECT DISTINCT category_name FROM control WHERE category_name IS NOT NULL AND category_name != ''")
    List<String> getAllCategoryNames();

    @Insert
    long insert(Control control); // 단일 항목 삽입

    @Insert
    List<Long> insert(List<Control> controls); // 다중 항목 삽입 (선택 사항)

    // 데이터 삭제
    @Delete
    void delete(Control control);

    // 데이터 업데이트
    @Update
    void update(Control control);

    // 첫 번째 Control ID 가져오기
    @Query("SELECT id FROM control LIMIT 1")
    int getFirstControlId();
}