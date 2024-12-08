package com.halfcodz.ctrl_project.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.adpater.CategoryDetail_Adapter;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;
import com.inhatc.real_project.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

public class CategoryDetail extends AppCompatActivity {

    private EditText detailCategoryName;
    private EditText detailTodoItem;
    private Button detailAddTodoButton;
    private Button detailSaveCategoryButton;
    private RecyclerView detailRecyclerView;

    private CategoryDetail_Adapter adapter;
    private List<Control> todoList;
    private AppDatabase db;
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailcategory);

        detailCategoryName = findViewById(R.id.detailCategoryName);
        detailTodoItem = findViewById(R.id.detailTodoItem);
        detailAddTodoButton = findViewById(R.id.detailaddTodoButton);
        detailSaveCategoryButton = findViewById(R.id.detailsaveCategoryButton);
        detailRecyclerView = findViewById(R.id.detailRecyclerView);

        db = AppDatabase.getDatabase(getApplicationContext());

        todoList = new ArrayList<>();
        adapter = new CategoryDetail_Adapter(todoList, this::deleteTodoItem);
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailRecyclerView.setAdapter(adapter);

        categoryId = getIntent().getIntExtra("category_id", -1);
        if (categoryId != -1) {
            loadCategoryDetails(categoryId);
        } else {
            Log.e("CategoryDetail", "Invalid category ID");
        }

        detailAddTodoButton.setOnClickListener(v -> addTodoItem());
        detailSaveCategoryButton.setOnClickListener(v -> saveCategory());
    }

    private void loadCategoryDetails(int categoryId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            String categoryName = db.controlDao().getTodosByCategoryId(categoryId).stream()
                    .map(Control::getCategoryName)
                    .findFirst()
                    .orElse(null);

            List<Control> todos = db.controlDao().getTodosByCategoryId(categoryId);

            runOnUiThread(() -> {
                if (categoryName != null) {
                    detailCategoryName.setText(categoryName);
                }
                todoList.clear();
                if (todos != null) {
                    todoList.addAll(todos);
                }
                adapter.notifyDataSetChanged();
            });
        });
    }

    private void addTodoItem() {
        String todoText = detailTodoItem.getText().toString().trim();
        if (!todoText.isEmpty()) {
            boolean isDuplicate = todoList.stream()
                    .anyMatch(todo -> todo.getControlItem().equalsIgnoreCase(todoText));

            if (!isDuplicate) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    boolean existsInDb = db.controlDao().existsByCategoryAndItem(categoryId, todoText);
                    if (!existsInDb) {
                        Control newTodo = new Control();
                        newTodo.setControlItem(todoText);
                        newTodo.setCategoryId(categoryId);

                        long newTodoId = db.controlDao().insert(newTodo);
                        newTodo.setId((int) newTodoId);

                        runOnUiThread(() -> {
                            todoList.add(newTodo);
                            adapter.notifyDataSetChanged();
                            detailTodoItem.setText("");
                        });
                    } else {
                        runOnUiThread(() -> detailTodoItem.setError("이미 존재하는 항목입니다."));
                    }
                });
            } else {
                detailTodoItem.setError("이미 존재하는 항목입니다.");
            }
        }
    }

    private void deleteTodoItem(int position) {
        Control todo = todoList.get(position);
        Executors.newSingleThreadExecutor().execute(() -> {
            db.controlDao().delete(todo);
            runOnUiThread(() -> {
                todoList.remove(position);
                adapter.notifyItemRemoved(position);
            });
        });
    }

    private void saveCategory() {
        String updatedName = detailCategoryName.getText().toString().trim();
        if (updatedName.isEmpty()) {
            detailCategoryName.setError("카테고리 이름을 입력하세요.");
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            Control category = new Control();
            category.setId(categoryId);
            category.setCategoryName(updatedName);
            db.controlDao().update(category);

            Set<Integer> existingIds = new HashSet<>();
            List<Control> existingTodos = db.controlDao().getTodosByCategoryId(categoryId);
            for (Control existingTodo : existingTodos) {
                existingIds.add(existingTodo.getId());
            }

            for (Control todo : todoList) {
                if (!existingIds.contains(todo.getId())) {
                    db.controlDao().insert(todo);
                } else {
                    db.controlDao().update(todo);
                }
            }

            runOnUiThread(this::finish);
        });
    }
}