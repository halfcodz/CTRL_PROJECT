package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.real_project.R;

import java.util.List;

public class TodoADD_Adapter extends RecyclerView.Adapter<TodoADD_Adapter.ViewHolder> {

    private List<String> categoryNames;
    private Context context;
    private int selectedPosition = -1;
    private final OnCategorySelectedListener onCategorySelectedListener;

    public interface OnCategorySelectedListener {
        void onCategorySelected(String categoryName);
    }

    public TodoADD_Adapter(Context context, List<String> categoryNames, OnCategorySelectedListener listener) {
        this.context = context;
        this.categoryNames = categoryNames;
        this.onCategorySelectedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_todolistadd, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String categoryName = categoryNames.get(position);

        holder.categoryNameText.setText(categoryName);
        holder.radioButton.setChecked(position == selectedPosition);

        holder.radioButton.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
            onCategorySelectedListener.onCategorySelected(categoryName); // 카테고리 이름 전달
        });
    }

    @Override
    public int getItemCount() {
        return categoryNames != null ? categoryNames.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameText;
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameText = itemView.findViewById(R.id.control_item_text);
            radioButton = itemView.findViewById(R.id.todolist_add_RadioButton);
        }
    }
}