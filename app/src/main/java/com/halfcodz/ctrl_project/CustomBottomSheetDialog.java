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
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;
import com.halfcodz.ctrl_project.data.ControlDao;
import com.inhatc.real_project.R;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CustomBottomSheetDialog extends BottomSheetDialogFragment {

    private Button missionClearButton; // 성공 버튼
    private Button missionFailButton;  // 실패 버튼
    private RecyclerView recyclerView; // RecyclerView
    private TodoSlideDrawer_Adapter adapter; // 어댑터
    private List<Control> controlList; // Control 데이터 리스트

    private final Executor executor = Executors.newSingleThreadExecutor(); // 수정: Executor 추가

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // bottom_sheet_layout.xml 레이아웃을 inflate
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 확인 버튼 클릭 시 드로어 닫기
        view.findViewById(R.id.slide_button).setOnClickListener(v -> dismiss());

        // 버튼 초기화
        missionClearButton = view.findViewById(R.id.missonClear);
        missionFailButton = view.findViewById(R.id.missonFail);

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.slidedrawer_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 수정: Executor를 사용해 DB에서 데이터 로드
        executor.execute(() -> {
            ControlDao controlDao = AppDatabase.getDatabase(getContext()).controlDao();
            int selectedCategoryId = 1; // 예제용으로 카테고리 ID를 1로 설정
            controlList = controlDao.getTodosByCategoryId(selectedCategoryId);

            // UI 업데이트는 메인 스레드에서 실행
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    // 어댑터 초기화 및 RecyclerView에 연결
                    adapter = new TodoSlideDrawer_Adapter(getContext(), controlList);
                    recyclerView.setAdapter(adapter);
                });
            }
        });

        // 버튼 클릭 이벤트 설정
        missionClearButton.setOnClickListener(v -> updateButtonStyles(true));
        missionFailButton.setOnClickListener(v -> updateButtonStyles(false));
    }

    // 버튼 상태 업데이트 메서드
    private void updateButtonStyles(boolean isMissionClearSelected) {
        if (isMissionClearSelected) {
            missionClearButton.setBackgroundTintList(getResources().getColorStateList(R.color.button_active, null));
            missionFailButton.setBackgroundTintList(getResources().getColorStateList(R.color.button_inactive, null));
        } else {
            missionClearButton.setBackgroundTintList(getResources().getColorStateList(R.color.button_inactive, null));
            missionFailButton.setBackgroundTintList(getResources().getColorStateList(R.color.button_active, null));
        }
    }
}