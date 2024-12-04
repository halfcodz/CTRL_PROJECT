package com.halfcodz.ctrl_project.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FocusDao {

    // focuslist 테이블에서 모든 항목을 가져옵니다.
    @Query("SELECT * FROM focuslist")
    List<Focus> getAll();

    // Focus 객체를 삽입합니다.
    @Insert
    void insert(Focus focus);

    // focuslist 테이블에서 주어진 id에 해당하는 항목들을 가져옵니다.
    @Query("SELECT * FROM focuslist WHERE id IN (:focusListIds)")
    List<Focus> loadAllByIds(int[] focusListIds);

    // Focus 객체들을 삭제합니다.
    @Delete
    void delete(List<Focus> focusList);
}