package com.halfcodz.ctrl_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SuccessList extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_list);

        Button btnBack = findViewById(R.id.btnBack);
        ListView lvSuccessList = findViewById(R.id.lvSuccessList);

        // 성공 항목 리스트
        ArrayList<String> successList = new ArrayList<>();
        successList.add("아침 조깅 - 7:00 AM");
        successList.add("오후 요가 - 3:00 PM");
        successList.add("저녁 근력 운동 - 8:00 PM");

        // ArrayAdapter를 사용해 ListView에 데이터 연결
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                successList
        );
        lvSuccessList.setAdapter(adapter);

        // ListView 항목 클릭 이벤트 처리
        lvSuccessList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 클릭된 항목의 텍스트 가져오기
                String selectedItem = successList.get(position);

                // SuccessDetailActivity로 데이터 전달
                Intent intent = new Intent(SuccessList.this, SuccessDetail.class);
                intent.putExtra("successItemTitle", selectedItem);
                intent.putExtra("Category", selectedItem);
                intent.putExtra("successDetail", "상세 정보: " + selectedItem); // 예제 상세 정보
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
