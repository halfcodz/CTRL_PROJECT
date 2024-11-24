package com.halfcodz.ctrl_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button Ctrl_add;    //통제탭 "+" 버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Ctrl_add = findViewById(R.id.Ctrl_add);
        Ctrl_add.setOnClickListener(new View.OnClickListener() {    // "+" 버튼 눌렀을 때
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, click_ctrl_add.class); // i 객체 생성 - 액티비티 이동
                startActivity(i);
            }
        });
    }
}