package com.inhatc.real_project.adpater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.real_project.R;
import com.inhatc.real_project.data.AppDatabase;
import com.inhatc.real_project.data.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final OnCategoryClickListener listener;
    private List<Category> categories = new ArrayList<>();
    private Context context;

    public CategoryAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.categoryName.setText(category.getName());

        // 수정: 아이템 클릭 이벤트 추가
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category));

        // 삭제 버튼 클릭 이벤트 처리
        holder.deleteButton.setOnClickListener(v -> {
            categories.remove(position); // 선택된 카테고리를 리스트에서 제거
            notifyItemRemoved(position); // 해당 위치의 아이템 제거를 알림
            notifyItemRangeChanged(position, categories.size());
        });

        // 데이터베이스에서 항목 삭제 (스레드 내에서)
        new Thread(() -> {
            // task의 고유 ID로 해당 항목을 삭제
            Context context = holder.itemView.getContext();
            AppDatabase.getDatabase(context).categoryDao().delete(categories);
        }).start();

    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    public void updateCategories(List<Category> newCategories) {
        this.categories = newCategories != null ? newCategories : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageButton deleteButton;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}