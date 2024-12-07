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

    // View 요소 선언
    private EditText detailCategoryName; // 카테고리 이름 입력 필드
    private EditText detailTodoItem; // 새로운 통제 항목 입력 필드
    private Button detailAddTodoButton; // 통제 항목 추가 버튼
    private Button detailSaveCategoryButton; // 카테고리 저장 버튼
    private RecyclerView detailRecyclerView; // RecyclerView

    private CategoryDetail_Adapter adapter; // 어댑터
    private List<Control> todoList; // 통제 항목 리스트
    private AppDatabase db; // Room Database
    private int categoryId; // 카테고리 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailcategory);

        // View 초기화
        detailCategoryName = findViewById(R.id.detailCategoryName);
        detailTodoItem = findViewById(R.id.detailTodoItem);
        detailAddTodoButton = findViewById(R.id.detailaddTodoButton);
        detailSaveCategoryButton = findViewById(R.id.detailsaveCategoryButton);
        detailRecyclerView = findViewById(R.id.detailRecyclerView);

        // Room Database 인스턴스 가져오기
        db = AppDatabase.getDatabase(getApplicationContext());

        // RecyclerView 설정
        todoList = new ArrayList<>();
        adapter = new CategoryDetail_Adapter(todoList, this::deleteTodoItem);
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailRecyclerView.setAdapter(adapter);

        // Intent에서 category_id를 받아서 처리
        categoryId = getIntent().getIntExtra("category_id", -1);
        if (categoryId != -1) {
            Log.d("CategoryDetail", "Loaded category ID: " + categoryId);
            loadCategoryDetails(categoryId); // 올바르게 categoryId 전달
        } else {
            Log.e("CategoryDetail", "Invalid category ID");
        }

        // 버튼 클릭 리스너 설정
        detailAddTodoButton.setOnClickListener(v -> addTodoItem());
        detailSaveCategoryButton.setOnClickListener(v -> saveCategory());
    }

    private void loadCategoryDetails(int categoryId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            // 카테고리 이름 가져오기
            String categoryName = db.controlDao().getCategory_Name(categoryId);

            // 해당 카테고리의 통제 항목 리스트 가져오기
            List<Control> todos = db.controlDao().getTodosByCategoryId(categoryId);

            // UI 업데이트 (Main Thread)
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
            boolean isDuplicate = false;

            for (Control todo : todoList) {
                if (todo.getControl_Item().equals(todoText)) {
                    isDuplicate = true;
                    break;
                }
            }

            if (!isDuplicate) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    boolean existsInDb = db.controlDao().existsByCategoryAndItem(categoryId, todoText);
                    runOnUiThread(() -> {
                        if (!existsInDb) {
                            Control newTodo = new Control();
                            newTodo.setControl_Item(todoText);
                            newTodo.setCategoryId(categoryId);

                            Executors.newSingleThreadExecutor().execute(() -> {
                                db.controlDao().insert(newTodo); // 데이터베이스에 저장
                                runOnUiThread(() -> {
                                    todoList.add(newTodo); // 수정: 추가된 항목을 리스트에 업데이트
                                    adapter.notifyDataSetChanged();
                                    detailTodoItem.setText("");
                                });
                            });
                        } else {
                            detailTodoItem.setError("이미 존재하는 항목입니다.");
                        }
                    });
                });
            } else {
                detailTodoItem.setError("이미 존재하는 항목입니다.");
            }
        }
    }

    private void deleteTodoItem(int position) {
        Control todo = todoList.get(position);
        Executors.newSingleThreadExecutor().execute(() -> {
            db.controlDao().delete(todo); // 데이터베이스에서 삭제
            runOnUiThread(() -> {
                todoList.remove(position); // 리스트에서 삭제
                adapter.notifyItemRemoved(position);
            });
        });
    }

    private void saveCategory() {
        String updatedName = detailCategoryName.getText().toString().trim();
        if (updatedName.isEmpty()) return;

        Executors.newSingleThreadExecutor().execute(() -> {
            Control category = new Control();
            category.setId(categoryId);
            category.setCategory_Name(updatedName);
            db.controlDao().update(category); // 수정: 카테고리 이름 업데이트

            Set<Integer> existingIds = new HashSet<>();
            List<Control> existingTodos = db.controlDao().getTodosByCategoryId(categoryId);
            for (Control existingTodo : existingTodos) {
                existingIds.add(existingTodo.getId());
            }

            for (Control todo : todoList) {
                if (todo.getId() == 0) {
                    db.controlDao().insert(todo); // 데이터베이스에 없는 항목 저장
                } else if (existingIds.contains(todo.getId())) {
                    db.controlDao().update(todo); // 데이터베이스에 있는 항목 업데이트
                }
            }

            runOnUiThread(this::finish); // 수정: 저장 후 화면 종료
        });
    }
}