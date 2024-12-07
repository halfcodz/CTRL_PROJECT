package com.halfcodz.ctrl_project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.halfcodz.ctrl_project.adpater.TodoSlideDrawer_Adapter;
import com.halfcodz.ctrl_project.data.Control;
import com.inhatc.real_project.R;

import java.util.ArrayList;
import java.util.List;

public class CustomBottomSheetDialog extends BottomSheetDialogFragment {

    private List<Control> controlList = new ArrayList<>();
    private TodoSlideDrawer_Adapter adapter;

    public void setControlItems(List<Control> controlItems) {
        controlList.clear();
        if (controlItems != null && !controlItems.isEmpty()) {
            controlList.addAll(controlItems);
        } else {
            Log.d("ControlDao", "Loaded Control Items: " + controlItems);
            controlList.add(new Control()); // 기본 메시지로 채우기
        }
        if (adapter != null) {
            adapter.updateData(controlList);
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

        RecyclerView recyclerView = view.findViewById(R.id.slidedrawer_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TodoSlideDrawer_Adapter(getContext(), controlList);
        recyclerView.setAdapter(adapter);
    }
}