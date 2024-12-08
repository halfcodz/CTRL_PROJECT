package com.halfcodz.ctrl_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.adpater.TodoMain_Adapter;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;
import com.halfcodz.ctrl_project.data.TodoItem;
import com.halfcodz.ctrl_project.ui.AddTodolist;
import com.inhatc.real_project.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentTodolist extends Fragment {

    private List<TodoItem> todoItems;
    private TodoMain_Adapter todoAdapter;
    private RecyclerView recyclerView_list;
    private CustomBottomSheetDialog customBottomSheetDialog;
    private SharedPreferences sharedPreferences;
    private Button btn_todo_add;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist, container, false);

        todoItems = new ArrayList<>();
        recyclerView_list = view.findViewById(R.id.recyclerView_list);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(requireContext()));

        customBottomSheetDialog = new CustomBottomSheetDialog();
        sharedPreferences = requireContext().getSharedPreferences("com.halfcodz.ctrl_project.PREFS", requireContext().MODE_PRIVATE);

        todoAdapter = new TodoMain_Adapter(todoItems, requireContext());
        recyclerView_list.setAdapter(todoAdapter);

        // btn_todo_add 버튼 초기화 및 클릭 리스너 설정
        btn_todo_add = view.findViewById(R.id.btn_todo_add);
        btn_todo_add.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTodolist.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        new Thread(() -> {
            List<TodoItem> newTodoList = AppDatabase.getDatabase(requireContext()).todoItemDao().getAll();

            requireActivity().runOnUiThread(() -> {
                todoItems.clear();
                todoItems.addAll(newTodoList);
                todoAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void showCustomBottomSheetDialog(TodoItem item) {
        new Thread(() -> {
            List<Control> controlItems = AppDatabase.getDatabase(requireContext())
                    .controlDao().getTodosByCategoryName(item.title);

            requireActivity().runOnUiThread(() -> {
                if (controlItems != null && !controlItems.isEmpty()) {
                    customBottomSheetDialog.setControlList(controlItems);
                    customBottomSheetDialog.show(getParentFragmentManager(), "CustomBottomSheet");
                } else {
                    Toast.makeText(getContext(), "해당 카테고리의 통제 항목이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppDatabase.destroyInstance();
    }
}