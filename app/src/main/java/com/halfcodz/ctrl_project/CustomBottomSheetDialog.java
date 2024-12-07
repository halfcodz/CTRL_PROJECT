package com.halfcodz.ctrl_project;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class CustomBottomSheetDialog extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private TodoSlideDrawer_Adapter adapter;
    private List<Control> controlList = new ArrayList<>();

    // 통제 항목 리스트를 설정하는 메서드
    public void setControlList(List<Control> controls) {
        controlList.clear();
        if (controls != null && !controls.isEmpty()) {
            controlList.addAll(controls);
        }

        if (adapter != null) {
            adapter.updateData(controlList);
        }
    }

    public void setSelectedControlItem(String controlItemText) {
        controlList.clear();
        Control control = new Control();
        control.setControlItem(controlItemText);
        controlList.add(control);

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

        recyclerView = view.findViewById(R.id.slidedrawer_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TodoSlideDrawer_Adapter(getContext(), controlList);
        recyclerView.setAdapter(adapter);
    }
}
