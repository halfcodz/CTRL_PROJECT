package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.CustomBottomSheetDialog;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;
import com.halfcodz.ctrl_project.data.TodoItem;
import com.inhatc.real_project.R;

import java.util.Collections;
import java.util.List;

public class TodoMain_Adapter extends RecyclerView.Adapter<TodoMain_Adapter.TodoViewHolder> {

    private final List<TodoItem> todoItems;
    private final Context context;
    private final SharedPreferences sharedPreferences;

    public TodoMain_Adapter(List<TodoItem> todoItems, Context context) {
        this.todoItems = todoItems;
        this.context = context;
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

        // btnRecord 클릭 리스너: 슬라이드드로어 활성화
        holder.btnRecord.setOnClickListener(v -> {
            saveSelectedControlItem(todo.title);

            // CustomBottomSheetDialog 활성화
            CustomBottomSheetDialog bottomSheetDialog = new CustomBottomSheetDialog();
            new Thread(() -> {
                List<Control> controlItems = AppDatabase.getDatabase(context).controlDao().getTodosByCategoryName(todo.title);
                bottomSheetDialog.setControlList(controlItems);
                ((FragmentActivity) context).runOnUiThread(() ->
                        bottomSheetDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "CustomBottomSheet"));
            }).start();
        });

        holder.todo_del.setOnClickListener(view -> {
            // 항목 삭제
            todoItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, todoItems.size());

            // 단일 TodoItem을 List로 감싸서 전달
            new Thread(() -> {
                AppDatabase.getDatabase(context).todoItemDao().delete(Collections.singletonList(todo));
            }).start();
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
        Button btnRecord, todo_del;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            todoTitle = itemView.findViewById(R.id.todoTitle);
            todoStart = itemView.findViewById(R.id.todoStart);
            todoEnd = itemView.findViewById(R.id.todoEnd);
            btnRecord = itemView.findViewById(R.id.btn_record);
            todo_del = itemView.findViewById(R.id.todo_del);
        }
    }
}