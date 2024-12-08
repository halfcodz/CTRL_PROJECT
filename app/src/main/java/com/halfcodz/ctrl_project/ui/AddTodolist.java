package com.halfcodz.ctrl_project.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.CustomBottomSheetDialog;
import com.halfcodz.ctrl_project.R;
import com.halfcodz.ctrl_project.adpater.TodoADD_Adapter;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddTodolist extends AppCompatActivity {

    private Button startDateText, SnoneText, endDateText, EnoneText, timeText, TnoneText, completion_btn;
    private EditText todoadd_title;

    private AppDatabase appDatabase;
    private RecyclerView recyclerViewTodolistAddCategory;
    private TodoADD_Adapter todoADDAdapter;
    private CustomBottomSheetDialog customBottomSheetDialog;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String selectedCategoryName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtodolist);

        initializeViews();
        appDatabase = AppDatabase.getDatabase(this);
        customBottomSheetDialog = new CustomBottomSheetDialog();

        recyclerViewTodolistAddCategory.setLayoutManager(new LinearLayoutManager(this));
        loadCategoryNames();
    }

    private void initializeViews() {
        startDateText = findViewById(R.id.startDateText);
        SnoneText = findViewById(R.id.SnoneText);
        endDateText = findViewById(R.id.endDateText);
        EnoneText = findViewById(R.id.EnoneText);
        timeText = findViewById(R.id.timeText);
        TnoneText = findViewById(R.id.TnoneText);
        todoadd_title = findViewById(R.id.todoadd_title);
        recyclerViewTodolistAddCategory = findViewById(R.id.recyclerView_todolist_addCategory);
        completion_btn = findViewById(R.id.completion_btn);
    }

    private void loadCategoryNames() {
        executorService.execute(() -> {
            List<String> categoryNames = appDatabase.controlDao().getAllCategoryNames();

            runOnUiThread(() -> {
                todoADDAdapter = new TodoADD_Adapter(this, categoryNames, categoryName -> {
                    selectedCategoryName = categoryName;
                    loadControlsForSelectedCategory();
                });
                recyclerViewTodolistAddCategory.setAdapter(todoADDAdapter);
            });
        });
    }

    private void loadControlsForSelectedCategory() {
        if (selectedCategoryName == null) return;

        executorService.execute(() -> {
            List<Control> controls = appDatabase.controlDao().getTodosByCategoryName(selectedCategoryName);

            runOnUiThread(() -> {
                if (controls == null || controls.isEmpty()) {
                    Toast.makeText(this, "해당 카테고리에 통제 항목이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    customBottomSheetDialog.setControlList(controls);
                    customBottomSheetDialog.show(getSupportFragmentManager(), "CustomBottomSheet");
                }
            });
        });
    }
}
