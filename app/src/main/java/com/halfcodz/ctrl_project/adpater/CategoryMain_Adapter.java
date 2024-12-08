package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.R;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryMain_Adapter extends RecyclerView.Adapter<CategoryMain_Adapter.ViewHolder> {

    // 인터페이스에 삭제 메서드 추가
    public interface OnCategoryInteractionListener {
        void onCategoryClick(int position);
        void onCategoryDelete(int position); // 삭제 메서드 추가
    }

    private List<Control> categoryList;
    private final Context context;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final OnCategoryInteractionListener interactionListener;

    public CategoryMain_Adapter(Context context, List<Control> categoryList, OnCategoryInteractionListener listener) {
        this.context = context;
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
        holder.todoName.setText(category.getCategoryName());

        holder.itemView.setOnClickListener(view -> interactionListener.onCategoryClick(position));

        holder.deleteButton.setOnClickListener(view -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                interactionListener.onCategoryDelete(adapterPosition);
            }
        });
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
