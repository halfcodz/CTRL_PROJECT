package com.inhatc.real_project.ui;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.inhatc.real_project.R;
import com.inhatc.real_project.adpater.TodoAdapter;
import com.inhatc.real_project.data.AppDatabase;
import com.inhatc.real_project.data.Category;
import com.inhatc.real_project.data.CategoryDao;
import com.inhatc.real_project.data.TodoItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class CategoryDetail extends AppCompatActivity {

    private EditText editCategoryName;
    private RecyclerView detailRecyclerView;
    private TodoAdapter todoAdapter;
    private ArrayList<Category> todoList = new ArrayList<>();
    private AppDatabase database;
    private TodoAdapter adapter;
    private int categoryId; // 수정: 카테고리 ID 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailcategory);

        editCategoryName = findViewById(R.id.detailCategoryName);
        detailRecyclerView = findViewById(R.id.detailRecyclerView);

        detailRecyclerView.setAdapter(todoAdapter);
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = AppDatabase.getDatabase(getApplicationContext());

        // 수정: Intent로 전달받은 categoryId 사용
        categoryId = getIntent().getIntExtra("categoryId", -1); // 카테고리 ID 가져오기
        loadCategoryDetails(); // 데이터 로드

        Executors.newSingleThreadExecutor().execute(() -> {
            String name = database.categoryDao().getCategoryNameById(categoryId);

            runOnUiThread(() -> {
                editCategoryName.setText(name);
                detailRecyclerView.setAdapter(adapter);
            });
        });
    }

    private void loadCategoryDetails() {
        new Thread(() -> {
            // 수정: Database에서 카테고리 데이터와 아이템 가져오기
            String categoryName = database.categoryDao().getCategoryNameById(categoryId);

            runOnUiThread(() -> {
                editCategoryName.setText(categoryName); // 카테고리 이름 설정
                todoList.clear();
                todoAdapter.notifyDataSetChanged();
            });
        }).start();
    }
}