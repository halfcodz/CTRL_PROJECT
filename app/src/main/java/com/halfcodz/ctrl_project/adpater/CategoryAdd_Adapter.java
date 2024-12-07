package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.R;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;

import java.util.List;

public class CategoryAdd_Adapter extends RecyclerView.Adapter<CategoryAdd_Adapter.TodoViewHolder> {

    private final List<Control> todoItems; // Control 리스트를 사용
    private final Context context;

    public CategoryAdd_Adapter(Context context, List<Control> todoItems) {
        this.context = context;
        this.todoItems = todoItems;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Control control = todoItems.get(position);
        holder.todoName.setText(control.getControlItem() != null ? control.getControlItem() : "항목 없음");

        holder.deleteButton.setOnClickListener(view -> {
            Control itemToDelete = todoItems.get(position);

            // UI에서 항목 삭제
            todoItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, todoItems.size());

            // 데이터베이스에서 항목 삭제 (스레드 내에서)
            new Thread(() -> {
                AppDatabase.getDatabase(context).controlDao().delete(itemToDelete);
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView todoName;
        ImageButton deleteButton;
        LinearLayout todolist_detail;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            todoName = itemView.findViewById(R.id.todoName);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            todolist_detail = itemView.findViewById(R.id.todolist_detail);
        }
    }
}
