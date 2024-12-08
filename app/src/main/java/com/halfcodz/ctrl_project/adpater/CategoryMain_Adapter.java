package com.halfcodz.ctrl_project.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;
import com.inhatc.real_project.R;

import java.util.List;

public class CategoryMain_Adapter extends RecyclerView.Adapter<CategoryMain_Adapter.ViewHolder> {

    public interface OnCategoryInteractionListener {
        void onCategoryClick(int position);
        void onCategoryDelete(int position);
    }

    private final List<Control> categoryList;
    private final OnCategoryInteractionListener interactionListener;

    public CategoryMain_Adapter(List<Control> categoryList, OnCategoryInteractionListener listener) {
        this.categoryList = categoryList;
        this.interactionListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Control category = categoryList.get(position);
        holder.categoryName.setText(category.getCategoryName());

        // 카테고리 클릭 이벤트 처리
        holder.itemView.setOnClickListener(view -> interactionListener.onCategoryClick(position));

        // 삭제 버튼 클릭 이벤트 처리
        holder.deleteButton.setOnClickListener(view -> {
            if (position >= 0 && position < categoryList.size()) {
                new Thread(() -> {
                    AppDatabase db = AppDatabase.getDatabase(holder.itemView.getContext());
                    db.controlDao().delete(categoryList.get(position));

                    holder.itemView.post(() -> {
                        categoryList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, categoryList.size());
                        interactionListener.onCategoryDelete(position);
                    });
                }).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.Main_todoName);
            deleteButton = itemView.findViewById(R.id.Main_deleteButton);
        }
    }
}