package com.halfcodz.ctrl_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.adpater.CategoryDetail_Adapter;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;
import com.inhatc.real_project.R;

import java.util.ArrayList;
import java.util.List;
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

        loadCategoryDetailsFromIntent();

        detailAddTodoButton.setOnClickListener(v -> addTodoItem());
        detailSaveCategoryButton.setOnClickListener(v -> saveCategoryDetails());
    }

    private void loadCategoryDetailsFromIntent() {
        categoryId = getIntent().getIntExtra("categoryId", -1);
        String categoryNameFromIntent = getIntent().getStringExtra("categoryName");

        if (categoryId == -1) {
            Toast.makeText(this, "잘못된 카테고리 ID입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (categoryNameFromIntent != null) {
            detailCategoryName.setText(categoryNameFromIntent);
        }

        loadCategoryNameAndItems(categoryId);
    }

    private void loadCategoryNameAndItems(int categoryId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Control> controls = db.controlDao().getTodosByCategoryId(categoryId);

            runOnUiThread(() -> {
                todoList.clear();
                todoList.addAll(controls);
                adapter.notifyDataSetChanged();
            });
        });
    }

    private void addTodoItem() {
        String todoText = detailTodoItem.getText().toString().trim();
        if (!todoText.isEmpty()) {
            Executors.newSingleThreadExecutor().execute(() -> {
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
            });
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

    private void saveCategoryDetails() {
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

            for (Control todo : todoList) {
                if (todo.getId() == 0) {
                    db.controlDao().insert(todo);
                } else {
                    db.controlDao().update(todo);
                }
            }

            runOnUiThread(() -> {
                Intent intent = new Intent();
                intent.putExtra("categoryId", categoryId);
                intent.putExtra("categoryName", updatedName);
                setResult(RESULT_OK, intent);
                finish();
            });
        });
    }
}