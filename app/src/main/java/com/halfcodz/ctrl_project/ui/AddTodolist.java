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
import com.halfcodz.ctrl_project.adpater.TodoADD_Adapter;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;
import com.halfcodz.ctrl_project.data.TodoItem;
import com.inhatc.real_project.R;

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

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private CustomBottomSheetDialog customBottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtodolist);

        // View 초기화
        initializeViews();

        // DB 및 RecyclerView 설정
        appDatabase = AppDatabase.getDatabase(this);
        recyclerViewTodolistAddCategory.setLayoutManager(new LinearLayoutManager(this));
        customBottomSheetDialog = new CustomBottomSheetDialog();

        // **수정된 부분: deleteControlsWithEmptyNames 호출 제거**
        executorService.execute(() -> loadCategoryNames());

        setClickListeners();
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

    private void setClickListeners() {
        startDateText.setOnClickListener(view -> showDatePickerDialog(SnoneText));
        SnoneText.setOnClickListener(view -> showDatePickerDialog(SnoneText));
        endDateText.setOnClickListener(view -> showDatePickerDialog(EnoneText));
        EnoneText.setOnClickListener(view -> showDatePickerDialog(EnoneText));
        timeText.setOnClickListener(view -> showTimePickerDialog());
        TnoneText.setOnClickListener(view -> showTimePickerDialog());
        completion_btn.setOnClickListener(view -> saveTodoItem());
    }

    private void loadCategoryNames() {
        executorService.execute(() -> {
            List<String> categoryNames = appDatabase.controlDao().getAllCategoryNames();

            runOnUiThread(() -> {
                todoADDAdapter = new TodoADD_Adapter(this, categoryNames, selectedCategoryName -> {
                    executorService.execute(() -> {
                        List<Control> controlItems = appDatabase.controlDao().getTodosByCategoryName(selectedCategoryName);
                        runOnUiThread(() -> {
                            if (controlItems == null || controlItems.isEmpty()) {
                                Toast.makeText(this, "해당 카테고리에 통제 항목이 없습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                customBottomSheetDialog.setControlItems(controlItems);
                            }
                        });
                    });
                });
                recyclerViewTodolistAddCategory.setAdapter(todoADDAdapter);
            });
        });
    }

    private void saveTodoItem() {
        executorService.execute(() -> {
            try {
                String title = todoadd_title.getText().toString();
                String startDate = SnoneText.getText().toString();
                String endDate = EnoneText.getText().toString();
                String time = TnoneText.getText().toString();

                if (title.isEmpty() || "없음".equals(startDate) || "없음".equals(endDate) || "없음".equals(time)) {
                    runOnUiThread(() -> Toast.makeText(AddTodolist.this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show());
                    return;
                }

                TodoItem todoItem = new TodoItem();
                todoItem.setTitle(title);
                todoItem.setStart_sch(startDate);
                todoItem.setEnd_sch(endDate);

                appDatabase.todoItemDao().insert(todoItem);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Todo 항목이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(AddTodolist.this, "에러 발생: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void showTimePickerDialog() {
        int hour = 12, minute = 0;
        new TimePickerDialog(this, (view, selectedHour, selectedMinute) ->
                TnoneText.setText(String.format("%02d:%02d", selectedHour, selectedMinute)), hour, minute, true
        ).show();
    }

    private void showDatePickerDialog(Button targetButton) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) ->
                targetButton.setText(String.format("%d-%d-%d", year, month + 1, day)),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}