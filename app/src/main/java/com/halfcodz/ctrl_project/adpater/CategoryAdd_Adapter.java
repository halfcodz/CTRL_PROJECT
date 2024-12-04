package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;
import com.inhatc.real_project.R;

import java.util.List;

public class CategoryAdd_Adapter extends RecyclerView.Adapter<CategoryAdd_Adapter.TodoViewHolder> {

    private final List<Control> todoItems; // Control 리스트를 사용

    public CategoryAdd_Adapter(List<Control> todoItems) {
        this.todoItems = todoItems;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_todo, parent, false); // item_todo.xml 레이아웃 연결
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Control control = todoItems.get(position); // Control 객체 가져오기
        holder.todoName.setText(control.getControl_Item() != null ? control.getControl_Item() : "항목 없음"); // todoName과 연결

        holder.deleteButton.setOnClickListener(view -> { // deleteButton과 연결
            Control itemToDelete = todoItems.get(position);

            // UI에서 항목 삭제
            todoItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, todoItems.size());

            // 데이터베이스에서 항목 삭제 (스레드 내에서)
            new Thread(() -> {
                Context context = holder.itemView.getContext();
                AppDatabase.getDatabase(context).controlDao().delete(itemToDelete);
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView todoName; // todoName과 연결
        ImageButton deleteButton; // deleteButton과 연결
        LinearLayout todolist_detail;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            todoName = itemView.findViewById(R.id.todoName); // item_todo.xml의 todoName과 연결
            deleteButton = itemView.findViewById(R.id.deleteButton); // item_todo.xml의 deleteButton과 연결
            todolist_detail = itemView.findViewById(R.id.todolist_detail); // 그대로 유지
        }
    }
}
