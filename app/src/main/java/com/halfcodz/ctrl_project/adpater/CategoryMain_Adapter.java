package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.halfcodz.ctrl_project.ui.CategoryDetail;

import java.util.List;
import java.util.concurrent.Executors;

public class    CategoryMain_Adapter extends RecyclerView.Adapter<CategoryMain_Adapter.ViewHolder> {

    private final List<Control> categoryList;
    private final Context context;
    private final OnCategoryInteractionListener interactionListener;

    public interface OnCategoryInteractionListener {
        void onCategoryClick(int position);
        void onCategoryDelete(int position);
    }

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
        holder.categoryName.setText(category.getCategoryName());

        // 카테고리 이름 클릭 이벤트 처리
        holder.categoryName.setOnClickListener(view -> {
            Intent intent = new Intent(context, CategoryDetail.class);
            intent.putExtra("categoryId", category.getId());
            intent.putExtra("categoryName", category.getCategoryName());
            context.startActivity(intent);
        });

        // 삭제 버튼 클릭 이벤트 처리
        holder.deleteButton.setOnClickListener(view -> {
            if (position >= 0 && position < categoryList.size()) {
                Control categoryToDelete = categoryList.get(position);

                // 삭제 전 로그 출력
                Log.d("CategoryMain_Adapter", "Deleting category: " + categoryToDelete.getCategoryName() + ", ID: " + categoryToDelete.getId());

                Executors.newSingleThreadExecutor().execute(() -> {
                    AppDatabase db = AppDatabase.getDatabase(context);
                    db.controlDao().delete(categoryToDelete);

                    holder.itemView.post(() -> {
                        categoryList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, categoryList.size());
                        Toast.makeText(context, "카테고리가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    });
                });
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
