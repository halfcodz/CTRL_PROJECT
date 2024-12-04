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

import com.halfcodz.ctrl_project.adpater.TodoMain_Adapter;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.TodoItem;
import com.halfcodz.ctrl_project.ui.AddTodolist;
import com.inhatc.real_project.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentTodolist extends Fragment {

    private List<TodoItem> todoItems;
    private TodoMain_Adapter todoAdapter;
    private RecyclerView recyclerView_list;
    private Button btn_todo_add;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist, container, false);

        todoItems = new ArrayList<>();
        recyclerView_list = view.findViewById(R.id.recyclerView_list);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(requireContext()));

        todoAdapter = new TodoMain_Adapter(todoItems);
        recyclerView_list.setAdapter(todoAdapter);

        btn_todo_add = view.findViewById(R.id.btn_todo_add);

        btn_todo_add.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTodolist.class);
            startActivity(intent);
        });
        return view;

    }

    // onViewCreated는 Fragment의 View가 완전히 생성된 후 호출됩니다. UI 관련 작업을 여기서 처리합니다.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 데이터 로드를 위한 스레드 시작
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(); // 화면 복귀 시 데이터 갱신
    }

    // 데이터베이스에서 스케줄 데이터를 로드하는 메서드
    private void loadData() {
        new Thread(() -> {
            List<TodoItem> newTodList = AppDatabase.getDatabase(requireContext()).todoItemDao().getAll();

            requireActivity().runOnUiThread(() -> {
                todoItems.clear();
                todoItems.addAll(newTodList);
                todoAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Fragment가 화면에서 제거될 때, 리소스 해제
        AppDatabase.destroyInstance();
    }


}