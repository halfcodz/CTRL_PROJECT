package com.halfcodz.ctrl_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.halfcodz.ctrl_project.adpater.TodoSlideDrawer_Adapter;
import com.halfcodz.ctrl_project.data.Control;

import java.util.ArrayList;
import java.util.List;

public class CustomBottomSheetDialog extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private TodoSlideDrawer_Adapter adapter;
    private List<Control> controlList = new ArrayList<>();

    private Button missionClearButton;
    private Button missionFailButton;

    // 통제 항목 리스트를 설정하는 메서드
    public void setControlList(List<Control> controls) {
        if (controls != null && !controls.isEmpty()) {
            this.controlList.clear();
            this.controlList.addAll(controls);

            if (adapter != null) {
                adapter.updateData(controlList);  // RecyclerView Adapter에 데이터 업데이트
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.slidedrawer_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TodoSlideDrawer_Adapter(getContext(), controlList);
        recyclerView.setAdapter(adapter);

        // 버튼 초기화
        missionClearButton = view.findViewById(R.id.missonClear);
        missionFailButton = view.findViewById(R.id.missonFail);

        // 버튼 클릭 리스너 설정
        missionClearButton.setOnClickListener(v -> handleButtonPress(missionClearButton, missionFailButton));
        missionFailButton.setOnClickListener(v -> handleButtonPress(missionFailButton, missionClearButton));
    }

    private void handleButtonPress(Button pressedButton, Button otherButton) {
        // 눌린 버튼을 선택된 상태로 설정
        pressedButton.setSelected(true);
        // 다른 버튼의 선택 상태 해제
        otherButton.setSelected(false);
    }
}
