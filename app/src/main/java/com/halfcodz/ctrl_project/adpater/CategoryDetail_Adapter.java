package com.halfcodz.ctrl_project.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.R;
import com.halfcodz.ctrl_project.data.Control;

import java.util.List;

public class CategoryDetail_Adapter extends RecyclerView.Adapter<CategoryDetail_Adapter.ViewHolder> {

    private final List<Control> todoList;
    private final OnTodoDeleteListener deleteListener;

    public interface OnTodoDeleteListener {
        void onDelete(int position);
    }

    public CategoryDetail_Adapter(List<Control> todoList, OnTodoDeleteListener deleteListener) {
        this.todoList = todoList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Control todo = todoList.get(position);
        holder.todoNameTextView.setText(todo.getControlItem());
        holder.deleteButton.setOnClickListener(v -> deleteListener.onDelete(position));
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView todoNameTextView;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todoNameTextView = itemView.findViewById(R.id.detailcategoryName);
            deleteButton = itemView.findViewById(R.id.detaildeleteButton);
        }
    }
}