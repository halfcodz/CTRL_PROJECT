package com.halfcodz.ctrl_project.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.R;
import com.halfcodz.ctrl_project.adpater.CategoryDetail_Adapter;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;

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
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailcategory);

        initializeViews();
        db = AppDatabase.getDatabase(getApplicationContext());

        todoList = new ArrayList<>();
        adapter = new CategoryDetail_Adapter(todoList, position -> deleteTodoItem(position));
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailRecyclerView.setAdapter(adapter);

        loadCategoryDetailsFromIntent();

        detailAddTodoButton.setOnClickListener(v -> addTodoItem());
        detailSaveCategoryButton.setOnClickListener(v -> saveCategoryDetails());
    }

    private void initializeViews() {
        detailCategoryName = findViewById(R.id.detailCategoryName);
        detailTodoItem = findViewById(R.id.detailTodoItem);
        detailAddTodoButton = findViewById(R.id.detailaddTodoButton);
        detailSaveCategoryButton = findViewById(R.id.detailsaveCategoryButton);
        detailRecyclerView = findViewById(R.id.detailRecyclerView);
    }

    private void loadCategoryDetailsFromIntent() {
        categoryId = getIntent().getIntExtra("categoryId", -1);
        categoryName = getIntent().getStringExtra("categoryName");

        Log.d("CategoryDetail", "Received Category ID: " + categoryId);
        Log.d("CategoryDetail", "Received Category Name: " + categoryName);

        if (categoryId == -1) {
            Toast.makeText(this, "잘못된 카테고리 ID입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (categoryName != null) {
            detailCategoryName.setText(categoryName);
        }

        loadCategoryItems(categoryId);
    }

    // 카테고리 ID로 통제 항목 불러오기
    private void loadCategoryItems(int categoryId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Control> controls = db.controlDao().getTodosByCategoryId(categoryId);

            Log.d("CategoryDetail", "Controls loaded: " + controls.size());
            for (Control control : controls) {
                Log.d("CategoryDetail", "Loaded Control - ID: " + control.getId() + ", Item: " + control.getControlItem() + ", Category ID: " + control.getCategoryId());
            }

            runOnUiThread(() -> {
                todoList.clear();
                todoList.addAll(controls);
                adapter.notifyDataSetChanged();
            });
        });
    }


    private void deleteTodoItem(int position) {
        Control todo = todoList.get(position);
        Executors.newSingleThreadExecutor().execute(() -> {
            db.controlDao().delete(todo);
            runOnUiThread(() -> {
                todoList.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(this, "항목이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void addTodoItem() {
        String todoText = detailTodoItem.getText().toString().trim();
        if (todoText.isEmpty()) {
            Toast.makeText(this, "통제 항목을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            Control newTodo = new Control();
            newTodo.setControlItem(todoText);
            newTodo.setCategoryId(categoryId);
            newTodo.setCategoryName(categoryName);

            long newTodoId = db.controlDao().insert(newTodo);
            newTodo.setId((int) newTodoId);

            runOnUiThread(() -> {
                todoList.add(newTodo);
                adapter.notifyDataSetChanged();
                detailTodoItem.setText("");
                Toast.makeText(this, "항목이 추가되었습니다.", Toast.LENGTH_SHORT).show();
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

            runOnUiThread(() -> {
                Toast.makeText(this, "카테고리가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
