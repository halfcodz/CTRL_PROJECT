package com.halfcodz.ctrl_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.halfcodz.ctrl_project.ui.ControlList;
import com.halfcodz.ctrl_project.ui.FailList;
import com.halfcodz.ctrl_project.ui.SuccessList;

public class FragmentComplete extends Fragment {

    private Button successBtn, failBtn, ctrlBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment의 View를 생성
        View view = inflater.inflate(R.layout.fragment_complete, container, false);

        // 버튼 초기화
        successBtn = view.findViewById(R.id.success_btn);
        failBtn = view.findViewById(R.id.fail_btn);
        ctrlBtn = view.findViewById(R.id.ctrl_btn);

        // 버튼 클릭 리스너 설정
        successBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSuccessList();
            }
        });

        failBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFailList();
            }
        });

        ctrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openControlList();
            }
        });

        return view;
    }

    private void openSuccessList() {
        Intent intent = new Intent(getActivity(), SuccessList.class);
        startActivity(intent);
    }

    private void openFailList() {
        Intent intent = new Intent(getActivity(), FailList.class);
        startActivity(intent);
    }

    private void openControlList() {
        Intent intent = new Intent(getActivity(), ControlList.class);
        startActivity(intent);
    }
}
