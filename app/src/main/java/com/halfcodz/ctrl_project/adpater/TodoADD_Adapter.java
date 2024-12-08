package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.data.AppDatabase;
import com.inhatc.real_project.R;

import java.util.List;

public class TodoADD_Adapter extends RecyclerView.Adapter<TodoADD_Adapter.ViewHolder> {

    private final List<String> categoryNames;
    private final Context context;
    private final OnCategorySelectedListener onCategorySelectedListener;
    private int selectedPosition = -1;

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
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                selectedPosition = currentPosition;
                notifyDataSetChanged();

                new Thread(() -> {
                    boolean categoryExists = AppDatabase.getDatabase(context)
                            .controlDao().existsByCategoryName(categoryName);

                    ((RecyclerView) holder.itemView.getParent()).post(() -> {
                        if (categoryExists) {
                            onCategorySelectedListener.onCategorySelected(categoryName);
                            Toast.makeText(context, "카테고리가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "카테고리를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryNames.size();
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