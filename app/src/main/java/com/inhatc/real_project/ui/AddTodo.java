package com.inhatc.real_project.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TimePicker;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.inhatc.real_project.FragmentTodolist;
import com.inhatc.real_project.R;
import com.inhatc.real_project.data.AppDatabase;
import com.inhatc.real_project.data.TodoItem;

import java.util.Calendar;

    public class AddTodo extends AppCompatActivity {
        Button startDateText, SnoneText, endDateText, EnoneText,
                timeText, TnoneText, completion_btn;
        EditText todoadd_title;

        private AppDatabase appDatabase = null;
        private Context sContext;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_addtodo);

            startDateText = findViewById(R.id.startDateText);
            SnoneText = findViewById(R.id.SnoneText);
            endDateText = findViewById(R.id.endDateText);
            EnoneText = findViewById(R.id.EnoneText);
            timeText = findViewById(R.id.timeText);
            TnoneText = findViewById(R.id.TnoneText);
            todoadd_title = findViewById(R.id.todoadd_title);

            appDatabase= AppDatabase.getDatabase(this);
            sContext = getApplicationContext();

            // 날짜 선택 버튼 설정
            startDateText.setOnClickListener(view -> showDatePickerDialog());
            SnoneText.setOnClickListener(view -> showDatePickerDialog());

            endDateText.setOnClickListener(view -> showDatePickerDialog2());
            EnoneText.setOnClickListener(view -> showDatePickerDialog2());

            timeText.setOnClickListener(view -> showTimePickerDialog());
            TnoneText.setOnClickListener(view -> showTimePickerDialog());

            // 완료 버튼 클릭 리스너
            completion_btn = findViewById(R.id.completion_btn);
            completion_btn.setOnClickListener(view -> {
                new Thread(() -> {
                    String title = todoadd_title.getText().toString();
                    String startDate = SnoneText.getText().toString();
                    String endDate = EnoneText.getText().toString();
                    String time = TnoneText.getText().toString();

                    if (title.isEmpty() || startDate.equals("없음") || endDate.equals("없음") || time.equals("없음")) {
                        runOnUiThread(() -> Toast.makeText(AddTodo.this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    TodoItem todoItem = new TodoItem();
                    todoItem.title = title;
                    todoItem.start_sch = startDate;
                    todoItem.end_sch = endDate;
                    AppDatabase.getDatabase(sContext).todoItemDao().insert(todoItem);

                    runOnUiThread(() -> {
                        Intent intent = new Intent(AddTodo.this, FragmentTodolist.class);
                        startActivity(intent);
                        finish();
                    });
                }).start();
            });
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            AppDatabase.destroyInstance();
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
