package com.halfcodz.ctrl_project.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TodoItemDao {

    @Query("SELECT * FROM todoitem")
    List<TodoItem> getAll();

    @Insert
    void insert(TodoItem todoItem);

    @Query("SELECT * FROM todoitem WHERE id IN (:Todo_ListIds)")
    List<TodoItem> loadAllByIds(int[] Todo_ListIds);

    @Delete void
    delete(List<TodoItem> todoItems);
}