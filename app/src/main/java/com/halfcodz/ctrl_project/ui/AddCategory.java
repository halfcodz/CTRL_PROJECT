package com.halfcodz.ctrl_project.ui;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcategory);

        categoryName = findViewById(R.id.editCategoryName);
        todoItem = findViewById(R.id.editTodoItem);
        addTodoButton = findViewById(R.id.addTodoButton);
        saveCategoryButton = findViewById(R.id.saveCategoryButton);
        todoRecyclerView = findViewById(R.id.todoRecyclerView);

        database = AppDatabase.getDatabase(getApplicationContext());

        todoItems = new ArrayList<>();
        todoAdapter = new CategoryAdd_Adapter(todoItems);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoRecyclerView.setAdapter(todoAdapter);

        addTodoButton.setOnClickListener(v -> addTodoItem()); // 기존 기능 유지
        saveCategoryButton.setOnClickListener(v -> saveCategoryAndNavigate()); // 수정: 화면 전환 포함
    }

    private void addTodoItem() {
        String item = todoItem.getText().toString().trim();

        if (item.isEmpty()) {
            Toast.makeText(this, "통제항목을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isDuplicate = false;
        for (Control control : todoItems) {
            if (control.getControl_Item() != null && control.getControl_Item().equalsIgnoreCase(item)) {
                isDuplicate = true;
                break;
            }
        }

        if (isDuplicate) {
            Toast.makeText(this, "이미 존재하는 항목입니다", Toast.LENGTH_SHORT).show();
        } else {
            Control control = new Control();
            control.setControl_Item(item);
            todoItems.add(control);
            todoAdapter.notifyDataSetChanged();
            todoItem.setText("");
        }
    }

    private void saveCategoryAndNavigate() {
        String name = categoryName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "카테고리 이름을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if (todoItems.isEmpty()) {
            Toast.makeText(this, "통제 항목을 추가하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            boolean isCategoryNameDuplicate = checkDuplicateCategoryName(name);
            if (isCategoryNameDuplicate) {
                runOnUiThread(() -> Toast.makeText(this, "이미 존재하는 카테고리 이름입니다", Toast.LENGTH_SHORT).show());
                return;
            }

            // 카테고리 및 항목 데이터 저장
            Control category = new Control();
            category.setCategory_Name(name);
            long categoryId = database.controlDao().insert(category);

            for (Control control : todoItems) {
                control.setCategoryId((int) categoryId);
                database.controlDao().insert(control);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "카테고리가 저장되었습니다", Toast.LENGTH_SHORT).show();
                finish(); // 이전 화면으로 돌아감
            });
        });
    }

    private boolean checkDuplicateCategoryName(String name) {
        List<String> allCategoryNames = database.controlDao().getAllCategoryNames();
        for (String categoryName : allCategoryNames) {
            if (categoryName != null && categoryName.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}