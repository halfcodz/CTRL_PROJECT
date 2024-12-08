package com.halfcodz.ctrl_project.adpater;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.data.Control;
import com.halfcodz.ctrl_project.ui.CategoryDetail;
import com.inhatc.real_project.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryMain_Adapter extends RecyclerView.Adapter<CategoryMain_Adapter.ViewHolder> {

    private List<Control> categoryList;
    private final OnCategoryInteractionListener interactionListener;

    public interface OnCategoryInteractionListener {
        void onCategoryClick(int position);
        void onCategoryDelete(int position);
    }

    public CategoryMain_Adapter(List<Control> categoryList, OnCategoryInteractionListener listener) {
        this.categoryList = (categoryList != null) ? categoryList : new ArrayList<>();
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
        holder.todoName.setText(category.getCategoryName());

        holder.itemView.setOnClickListener(view -> interactionListener.onCategoryClick(position));
        holder.deleteButton.setOnClickListener(view -> interactionListener.onCategoryDelete(position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void updateCategories(List<Control> newCategoryList) {
        this.categoryList.clear();
        this.categoryList.addAll(newCategoryList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView todoName;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todoName = itemView.findViewById(R.id.Main_todoName);
            deleteButton = itemView.findViewById(R.id.Main_deleteButton);
        }
    }
}