package com.halfcodz.ctrl_project.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.R;
import com.halfcodz.ctrl_project.adpater.CategoryAdd_Adapter;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;

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

        // View 초기화
        initializeViews();

        // 데이터베이스 초기화
        database = AppDatabase.getDatabase(getApplicationContext());

        // RecyclerView 설정
        setupRecyclerView();

        // 버튼 클릭 리스너 설정
        addTodoButton.setOnClickListener(v -> addTodoItem());
        saveCategoryButton.setOnClickListener(v -> saveCategoryAndNavigate());
    }

    // View 초기화 메서드
    private void initializeViews() {
        categoryName = findViewById(R.id.editCategoryName);
        todoItem = findViewById(R.id.editTodoItem);
        addTodoButton = findViewById(R.id.addTodoButton);
        saveCategoryButton = findViewById(R.id.saveCategoryButton);
        todoRecyclerView = findViewById(R.id.todoRecyclerView);
    }

    // RecyclerView 설정 메서드
    private void setupRecyclerView() {
        todoItems = new ArrayList<>();
        todoAdapter = new CategoryAdd_Adapter(this, todoItems);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoRecyclerView.setAdapter(todoAdapter);
    }

    // 통제 항목 추가 메서드
    private void addTodoItem() {
        String item = todoItem.getText().toString().trim();

        if (item.isEmpty()) {
            Toast.makeText(this, "통제 항목을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 중복 확인
        for (Control control : todoItems) {
            if (control.getControlItem() != null && control.getControlItem().equalsIgnoreCase(item)) {
                Toast.makeText(this, "이미 존재하는 항목입니다", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 통제 항목 추가
        Control control = new Control();
        control.setControlItem(item);
        todoItems.add(control);
        todoAdapter.notifyDataSetChanged();
        todoItem.setText("");
    }

    // 카테고리와 통제 항목 저장 메서드
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
            // 중복된 카테고리 이름 확인
            boolean isCategoryNameDuplicate = checkDuplicateCategoryName(name);
            if (isCategoryNameDuplicate) {
                runOnUiThread(() -> Toast.makeText(this, "이미 존재하는 카테고리 이름입니다", Toast.LENGTH_SHORT).show());
                return;
            }

            // 카테고리 및 통제 항목 데이터 저장
            Control category = new Control();
            category.setCategoryName(name);
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

    // 중복된 카테고리 이름 확인 메서드
    private boolean checkDuplicateCategoryName(String name) {
        List<String> allCategoryNames = database.controlDao().getAllCategoryNames();
        for (String categoryName : allCategoryNames) {
            if (categoryName.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
