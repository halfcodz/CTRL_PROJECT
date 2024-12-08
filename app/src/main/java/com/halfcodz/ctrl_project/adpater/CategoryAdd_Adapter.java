package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.content.Intent;
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
import com.halfcodz.ctrl_project.ui.CategoryDetail;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryAdd_Adapter extends RecyclerView.Adapter<CategoryAdd_Adapter.TodoViewHolder> {

    private final List<Control> todoItems;
    private final Context context;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public CategoryAdd_Adapter(Context context, List<Control> todoItems) {
        this.context = context;
        this.todoItems = todoItems;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Control control = todoItems.get(position);

        // controlItem이 null이 아닌지 확인하고 기본값 설정
        if (control != null && control.getControlItem() != null) {
            holder.todoName.setText(control.getControlItem());
        } else {
            holder.todoName.setText("항목 없음");
        }

        // 항목 클릭 시 CategoryDetail로 이동
        holder.itemView.setOnClickListener(v -> {
            if (control != null) {
                Intent intent = new Intent(context, CategoryDetail.class);
                intent.putExtra("categoryId", control.getCategoryId());
                intent.putExtra("categoryName", control.getCategoryName());
                context.startActivity(intent);
            }
        });

        // 삭제 버튼 클릭 시
        holder.deleteButton.setOnClickListener(view -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION && control != null) {
                Control controlToDelete = todoItems.get(adapterPosition);

                // 데이터베이스에서 항목 삭제
                executorService.execute(() -> {
                    AppDatabase.getDatabase(context).controlDao().delete(controlToDelete);

                    // UI 업데이트
                    ((RecyclerView) holder.itemView.getParent()).post(() -> {
                        todoItems.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                        notifyItemRangeChanged(adapterPosition, todoItems.size());
                        Toast.makeText(context, "항목이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    });
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoItems != null ? todoItems.size() : 0;
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
