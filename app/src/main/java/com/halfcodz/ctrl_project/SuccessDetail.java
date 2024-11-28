package com.halfcodz.ctrl_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.halfcodz.ctrl_project.R;

public class SuccessDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_detail);

        // UI 요소 초기화
        TextView tvDetailTitle = findViewById(R.id.tvSuccessItemTitle);
        TextView tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        TextView tvDetailContent = findViewById(R.id.tvDetailContent);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnDelete = findViewById(R.id.btnDelete);

        // Intent로 전달된 데이터 받기
        String successItemTitle = getIntent().getStringExtra("successItemTitle");
        String Category = getIntent().getStringExtra("Category");
        String successDetail = getIntent().getStringExtra("successDetail");

        // 데이터 세팅
        tvDetailTitle.setText(successItemTitle);
        tvCategoryTitle.setText(Category);
        tvDetailContent.setText(successDetail);

        // 뒤로 가기
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 이전 화면으로 돌아가기
            }
        });

        // 삭제
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SuccessDetail.this, successItemTitle + " 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                finish(); // 삭제 후 이전 화면으로 돌아가기
            }
        });
    }
}