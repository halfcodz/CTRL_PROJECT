package com.inhatc.real_project.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories")
    List<Category> getAllCategories();

    @Query("SELECT name FROM categories WHERE id = :id")
    String getCategoryNameById(int id);

    @Insert
    long insert(Category category);

    @Delete
    void delete(List<Category> categories);
}