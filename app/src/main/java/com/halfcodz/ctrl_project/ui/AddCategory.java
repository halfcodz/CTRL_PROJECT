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

import com.halfcodz.ctrl_project.adpater.CategoryAdd_Adapter;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;
import com.inhatc.real_project.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AddCategory extends AppCompatActivity {

    private EditText categoryName;
    private EditText todoItem;
    private Button addTodoButton;
    private Button saveCategoryButton;
    private RecyclerView todoRecyclerView;
    private CategoryAdd_Adapter todoAdapter;
    private List<Control> todoItems;
    private AppDatabase database;
    private boolean isSaving = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcategory);

        initializeViews();
        database = AppDatabase.getDatabase(getApplicationContext());
        setupRecyclerView();

        addTodoButton.setOnClickListener(v -> addTodoItem());
        saveCategoryButton.setOnClickListener(v -> saveCategoryAndNavigate());
    }

    private void initializeViews() {
        categoryName = findViewById(R.id.editCategoryName);
        todoItem = findViewById(R.id.editTodoItem);
        addTodoButton = findViewById(R.id.addTodoButton);
        saveCategoryButton = findViewById(R.id.saveCategoryButton);
        todoRecyclerView = findViewById(R.id.todoRecyclerView);
    }

    private void setupRecyclerView() {
        todoItems = new ArrayList<>();
        todoAdapter = new CategoryAdd_Adapter(this, todoItems);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoRecyclerView.setAdapter(todoAdapter);
    }

    private void addTodoItem() {
        String item = todoItem.getText().toString().trim();
        if (item.isEmpty()) {
            Toast.makeText(this, "통제 항목을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Control control : todoItems) {
            if (control.getControlItem() != null && control.getControlItem().equalsIgnoreCase(item)) {
                Toast.makeText(this, "이미 존재하는 항목입니다", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Control control = new Control();
        control.setControlItem(item);
        todoItems.add(control);
        todoAdapter.notifyDataSetChanged();
        todoItem.setText("");
    }

    private void saveCategoryAndNavigate() {
        if (isSaving) return;

        String name = categoryName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "카테고리 이름을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if (todoItems.isEmpty()) {
            Toast.makeText(this, "통제 항목을 추가하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        isSaving = true;
        saveCategoryButton.setEnabled(false);

        Executors.newSingleThreadExecutor().execute(() -> {
            // 카테고리 저장
            Control category = new Control();
            category.setCategoryName(name);
            long categoryId = database.controlDao().insert(category);

            Log.d("AddCategory", "Category saved with name: " + name);

            // 통제 항목 저장
            for (Control control : todoItems) {
                if (control.getControlItem() != null && !control.getControlItem().trim().isEmpty()) {
                    control.setCategoryId((int) categoryId);
                    control.setCategoryName(name);
                    database.controlDao().insert(control);
                }
            }

            database.controlDao().deleteControlswithNullItems();

            runOnUiThread(() -> {
                Toast.makeText(this, "카테고리가 저장되었습니다", Toast.LENGTH_SHORT).show();
                isSaving = false;

                Intent resultIntent = new Intent();
                resultIntent.putExtra("categoryId", (int) categoryId);
                resultIntent.putExtra("categoryName", name);
                setResult(RESULT_OK, resultIntent);

                finish();
            });
        });
    }
}