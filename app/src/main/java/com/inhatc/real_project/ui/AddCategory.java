package com.inhatc.real_project.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.real_project.R;
import com.inhatc.real_project.adpater.TodoAdapter;
import com.inhatc.real_project.data.AppDatabase;
import com.inhatc.real_project.data.Category;
import com.inhatc.real_project.data.TodoItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AddCategory extends AppCompatActivity {

    private EditText categoryName, todoItem;
    private Button addTodoButton, saveCategoryButton;
    private RecyclerView todoRecyclerView;
    private TodoAdapter todoAdapter; // !! 추가: RecyclerView에 데이터를 표시하기 위한 어댑터
    private List<TodoItem> todoItems; // !! 추가: RecyclerView에서 관리할 데이터 리스트
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

        todoItems = new ArrayList<>(); // !! 초기화
        todoAdapter = new TodoAdapter(todoItems); // !! 어댑터 초기화
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoRecyclerView.setAdapter(todoAdapter);

        addTodoButton.setOnClickListener(v -> addTodoItem());
        saveCategoryButton.setOnClickListener(v -> saveCategory());
    }

    private void addTodoItem() {
        String item = todoItem.getText().toString().trim();
        if (!item.isEmpty()) {
            TodoItem todo = new TodoItem(); // 새로운 TodoItem 객체 생성
            todo.setItemName(item);        // 아이템 이름 설정
            todoItems.add(todo);           // 리스트에 추가
            todoAdapter.notifyDataSetChanged(); // RecyclerView 업데이트
            todoItem.setText("");          // 입력 필드 초기화
        }
    }

    private void saveCategory() {
        String name = categoryName.getText().toString().trim();
        if (name.isEmpty()) return;

        Executors.newSingleThreadExecutor().execute(() -> {
            // 카테고리 저장
            Category category = new Category();
            category.setName(name);
            long categoryId = database.categoryDao().insert(category); // autoGenerate가 설정된 경우 ID 반환

            // 통제 목록 저장
            for (TodoItem todo : todoItems) { // todoItems는 TodoItem 객체 리스트
                todo.setCategoryId((int) categoryId); // 생성된 카테고리 ID 설정
                database.todoItemDao().insert(todo);
            }

            runOnUiThread(this::finish); // UI 스레드에서 액티비티 종료
        });
    }
}