package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.CustomBottomSheetDialog;
import com.halfcodz.ctrl_project.R;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.TodoItem;
import com.halfcodz.ctrl_project.ui.DetailTodolist;

import java.util.Collections;
import java.util.List;

public class TodoMain_Adapter extends RecyclerView.Adapter<TodoMain_Adapter.TodoViewHolder> {

    private final List<TodoItem> todoItems;
    private final Context context;
    private final OnRecordButtonClickListener recordButtonClickListener;
    private SharedPreferences sharedPreferences;

    public interface OnRecordButtonClickListener {
        void onRecordButtonClick(TodoItem item);
    }

    public TodoMain_Adapter(List<TodoItem> todoItems, Context context, OnRecordButtonClickListener listener) {
        this.todoItems = todoItems;
        this.context = context;
        this.recordButtonClickListener = listener;
        this.sharedPreferences = context.getSharedPreferences("com.halfcodz.ctrl_project.PREFS", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        // btnRecord 클릭 리스너 설정
        holder.btnRecord.setOnClickListener(v -> {
            saveSelectedControlItem(todo.title);
            if (recordButtonClickListener != null) {
                recordButtonClickListener.onRecordButtonClick(todo);
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
                AppDatabase.getDatabase(context).todoItemDao().delete(Collections.singletonList(itemToDelete));
            }).start();
        });

        // 항목 클릭 시 상세 화면 이동
        holder.todolist_detail.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailTodolist.class);
            context.startActivity(intent);
        });
    }

    private void saveSelectedControlItem(String controlItemText) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selected_control_item", controlItemText);
        editor.apply();
        Toast.makeText(context, "선택된 통제 항목이 저장되었습니다.", Toast.LENGTH_SHORT).show();
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
