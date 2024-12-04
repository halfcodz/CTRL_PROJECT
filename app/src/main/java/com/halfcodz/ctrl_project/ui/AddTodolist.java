package com.halfcodz.ctrl_project.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.FragmentTodolist;
import com.halfcodz.ctrl_project.adpater.TodoADD_Adapter;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.TodoItem;
import com.inhatc.real_project.R;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddTodolist extends AppCompatActivity {
    Button startDateText, SnoneText, endDateText, EnoneText,
            timeText, TnoneText, completion_btn;
    EditText todoadd_title;

    private AppDatabase appDatabase;
    private RecyclerView recyclerViewTodolistAddCategory;
    private TodoADD_Adapter todoADDAdapter;

    // **수정된 부분: ExecutorService 추가**
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtodolist);

        // View 초기화
        startDateText = findViewById(R.id.startDateText);
        SnoneText = findViewById(R.id.SnoneText);
        endDateText = findViewById(R.id.endDateText);
        EnoneText = findViewById(R.id.EnoneText);
        timeText = findViewById(R.id.timeText);
        TnoneText = findViewById(R.id.TnoneText);
        todoadd_title = findViewById(R.id.todoadd_title);
        recyclerViewTodolistAddCategory = findViewById(R.id.recyclerView_todolist_addCategory);

        // DB 초기화
        appDatabase = AppDatabase.getDatabase(this);

        // RecyclerView 초기화
        recyclerViewTodolistAddCategory.setLayoutManager(new LinearLayoutManager(this));

        // 빈 데이터 정리 후 카테고리 이름 로드
        executorService.execute(() -> {
            appDatabase.controlDao().deleteControlsWithEmptyNames();
            loadCategoryNames();
        });

        // 날짜 선택 버튼 설정
        startDateText.setOnClickListener(view -> showDatePickerDialog());
        SnoneText.setOnClickListener(view -> showDatePickerDialog());

        endDateText.setOnClickListener(view -> showDatePickerDialog2());
        EnoneText.setOnClickListener(view -> showDatePickerDialog2());

        timeText.setOnClickListener(view -> showTimePickerDialog());
        TnoneText.setOnClickListener(view -> showTimePickerDialog());

        // **수정된 부분: 데이터 로드 메서드 호출**
        loadCategoryNames();

        // 완료 버튼 클릭 리스너
        completion_btn = findViewById(R.id.completion_btn);
        completion_btn.setOnClickListener(view -> {
            executorService.execute(() -> {
                try {
                    String title = todoadd_title.getText().toString();
                    String startDate = SnoneText.getText().toString();
                    String endDate = EnoneText.getText().toString();
                    String time = TnoneText.getText().toString();

                    if (title.isEmpty() || startDate.equals("없음") || endDate.equals("없음") || time.equals("없음")) {
                        runOnUiThread(() -> Toast.makeText(AddTodolist.this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    TodoItem todoItem = new TodoItem();
                    todoItem.title = title;
                    todoItem.start_sch = startDate;
                    todoItem.end_sch = endDate;

                    appDatabase.todoItemDao().insert(todoItem);

                    runOnUiThread(() -> {
                        // **수정된 부분: Fragment로 전환**
                        moveToFragment(new FragmentTodolist());
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(AddTodolist.this, "에러 발생: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        });
    }

    // **수정된 부분: 프래그먼트로 전환 메서드**
    private void moveToFragment(FragmentTodolist fragmentTodolist) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentTodolist); // fragment_container는 MainActivity의 레이아웃 ID여야 합니다.
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // DB에서 모든 카테고리 이름 로드
    private void loadCategoryNames() {
        executorService.execute(() -> {
            List<String> categoryNames = appDatabase.controlDao().getAllCategoryNames();

            runOnUiThread(() -> {
                // RecyclerView 어댑터 설정
                todoADDAdapter = new TodoADD_Adapter(this, categoryNames);
                recyclerViewTodolistAddCategory.setAdapter(todoADDAdapter);
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppDatabase.destroyInstance();
        executorService.shutdown(); // **수정된 부분: ExecutorService 종료 추가**
    }

    private void showTimePickerDialog() {
        int hour = 12;
        int minute = 0;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int selectedHour, int selectedMinute) -> {
                    String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    TnoneText.setText(formattedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    SnoneText.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showDatePickerDialog2() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog2 = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    EnoneText.setText(date);
                }, year, month, day);
        datePickerDialog2.show();
    }
}