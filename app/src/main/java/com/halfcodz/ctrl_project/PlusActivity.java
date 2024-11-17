package com.halfcodz.ctrl_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Time;
import java.util.Calendar;

public class PlusActivity extends AppCompatActivity {
    Button next_button, back_button;
    Button startDateText, SnoneText, endDateText, EnoneText, timeText, TnoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plus);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startDateText = findViewById(R.id.startDateText);
        SnoneText = (Button) findViewById(R.id.SnoneText);
        endDateText = (Button) findViewById(R.id.endDateText);
        EnoneText = (Button) findViewById(R.id.EnoneText);
        back_button = (Button) findViewById(R.id.back_button);
        timeText = (Button) findViewById(R.id.timeText);
        TnoneText = (Button) findViewById(R.id.TnoneText);

        // 시작 날짜 버튼 클릭 시 날짜 선택
        startDateText.setOnClickListener(view -> showDatePickerDialog());
        SnoneText.setOnClickListener(view -> showDatePickerDialog());

        // 종료 날짜 버튼 클릭 시 날짜 선택
        endDateText.setOnClickListener(view -> showDatePickerDialog2());
        EnoneText.setOnClickListener(view -> showDatePickerDialog2());

        timeText.setOnClickListener(view -> showTimePickerDialog());
        TnoneText.setOnClickListener(view -> showTimePickerDialog());

        Button next_button = (Button) findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlusActivity.this, PPlusActivity.class);
                startActivity(intent);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(PlusActivity.this, MainActivity.class);
                startActivity(intent2);
            }
        });
    }

    private void showTimePickerDialog() {
        int hour = 12;
        int minute = 0;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePickerDialog.OnTimeSetListener) (TimePicker view,
                                                      int selectedHour, int selectedMinute) -> {
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
                    // 선택된 날짜를 "YYYY-MM-DD" 형식으로 버튼 텍스트에 설정
                    String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    SnoneText.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showDatePickerDialog2() {
        Calendar calendar = Calendar.getInstance();
        int year2 = calendar.get(Calendar.YEAR);
        int month2 = calendar.get(Calendar.MONTH);
        int day2 = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog2 = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // 선택된 날짜를 "YYYY-MM-DD" 형식으로 버튼 텍스트에 설정
                    String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    EnoneText.setText(date);
                }, year2, month2, day2);
        datePickerDialog2.show();
    }
}