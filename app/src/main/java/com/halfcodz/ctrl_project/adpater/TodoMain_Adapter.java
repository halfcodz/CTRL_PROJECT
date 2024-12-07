package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.CustomBottomSheetDialog;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.TodoItem;
import com.halfcodz.ctrl_project.ui.DetailTodolist;
import com.inhatc.real_project.R;

import java.util.Collections;
import java.util.List;

public class TodoMain_Adapter extends RecyclerView.Adapter<TodoMain_Adapter.TodoViewHolder> {

    private final List<TodoItem> todoItems; // TodoItem 리스트를 사용
    private FragmentManager fragmentManager; // 프래그먼트의 FragmentManager를 받아옴

    public TodoMain_Adapter(List<TodoItem> todoItems) {
        this.todoItems = todoItems;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_todolist, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoItem todo = todoItems.get(position);
        holder.todoTitle.setText(todo.title != null ? todo.title : "제목 없음");
        holder.todoStart.setText(todo.start_sch != null ? todo.start_sch : "시작 날짜 없음");
        holder.todoEnd.setText(todo.end_sch != null ? todo.end_sch : "종료 날짜 없음");

        holder.btnRecord.setOnClickListener(view -> {
            Context context = view.getContext();
            if (context instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) context;
                CustomBottomSheetDialog bottomSheetDialog = new CustomBottomSheetDialog();
                bottomSheetDialog.show(activity.getSupportFragmentManager(), "CustomBottomSheet");
            }
        });

        holder.todo_del.setOnClickListener(view -> {
            // 삭제할 항목을 먼저 저장
            TodoItem itemToDelete = todoItems.get(position);

            // UI에서 항목 삭제
            todoItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, todoItems.size());

            // 데이터베이스에서 항목 삭제 (스레드 내에서)
            new Thread(() -> {
                Context context = holder.itemView.getContext();
                AppDatabase.getDatabase(context).todoItemDao().delete(Collections.singletonList(itemToDelete));
            }).start();
        });

        // 항목 클릭 시 상세 화면 이동
        holder.todolist_detail.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), DetailTodolist.class);
            view.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView todoTitle, todoStart, todoEnd;
        Button todo_del;
        LinearLayout todolist_detail;
        Button btnRecord;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            todoTitle = itemView.findViewById(R.id.todoTitle);
            todoStart = itemView.findViewById(R.id.todoStart);
            todoEnd = itemView.findViewById(R.id.todoEnd);
            todo_del = itemView.findViewById(R.id.todo_del);
            todolist_detail = itemView.findViewById(R.id.todolist_detail);
            btnRecord = itemView.findViewById(R.id.btn_record);
        }
    }

}