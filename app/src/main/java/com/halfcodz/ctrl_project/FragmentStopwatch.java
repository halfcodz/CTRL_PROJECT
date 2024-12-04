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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.adpater.FocusAdapter;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Focus;
import com.inhatc.real_project.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentStopwatch extends Fragment {

    private List<Focus> focusList;
    private FocusAdapter focusAdapter;
    private RecyclerView recyclerView_focus;
    private Button btn_focus_mode;

    // 추가: 시간을 저장할 변수
    private int receivedHour, receivedMinute, receivedSecond;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        // RecyclerView 초기화
        focusList = new ArrayList<>();
        recyclerView_focus = view.findViewById(R.id.recyclerView_focus);
        recyclerView_focus.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 어댑터 설정
        focusAdapter = new FocusAdapter(focusList);
        recyclerView_focus.setAdapter(focusAdapter);

        if (getArguments() != null) {
            receivedHour = getArguments().getInt("hour", 0);
            receivedMinute = getArguments().getInt("minute", 0);
            receivedSecond = getArguments().getInt("second", 0);

            // 받은 시간 데이터를 RecyclerView에 추가
            addFocusToList(receivedHour, receivedMinute, receivedSecond);
        }

        // FocusMode 버튼 클릭 시 FocusMode 화면으로 이동
        btn_focus_mode = view.findViewById(R.id.btn_focus_mode);
        btn_focus_mode.setOnClickListener(v -> {
            // FocusMode 화면을 Activity로 전환
            startActivity(new Intent(getActivity(), FocusMode.class));
        });

        return view;
    }

    // 시간 데이터를 RecyclerView에 추가하는 메서드
    private void addFocusToList(int hour, int minute, int second) {

        Focus focus = new Focus();
        focus.focus_hour = String.format("%02d", hour);
        focus.focus_minutes = String.format("%02d", minute);
        focus.focus_second = String.format("%02d", second);

        // FocusDao를 이용하여 데이터베이스에 저장
        new Thread(() -> {
            AppDatabase.getDatabase(getContext()).focusDao().insert(focus);

            // UI 업데이트는 메인 스레드에서 실행해야 합니다.
            requireActivity().runOnUiThread(() -> {
                focusList.add(focus);
                focusAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    // Fragment가 화면에 보일 때마다 데이터를 다시 로드하도록 수정
    @Override
    public void onResume() {
        super.onResume();
        loadData(); // 데이터 로드
    }

    // 데이터베이스에서 Focus 데이터를 로드하는 메서드
    private void loadData() {
        new Thread(() -> {
            List<Focus> newFocusList = AppDatabase.getDatabase(requireContext()).focusDao().getAll();

            requireActivity().runOnUiThread(() -> {

                focusList.clear();
                for (Focus focus : newFocusList) {
                    if (!focus.focus_hour.equals("00") || !focus.focus_minutes.equals("00") || !focus.focus_second.equals("00")) {
                        focusList.add(focus);
                    }
                }
                focusAdapter.notifyDataSetChanged();
            });
        }).start();
    }
}