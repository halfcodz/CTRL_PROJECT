package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.real_project.R;

import java.util.List;

public class TodoADD_Adapter extends RecyclerView.Adapter<TodoADD_Adapter.ViewHolder> {

    private final List<String> categoryNames;
    private final Context context;
    private int selectedPosition = -1;
    private final SharedPreferences sharedPreferences;
    private final OnCategorySelectedListener onCategorySelectedListener;

    // 인터페이스 정의
    public interface OnCategorySelectedListener {
        void onCategorySelected(String categoryName);
    }

    // 생성자
    public TodoADD_Adapter(Context context, List<String> categoryNames, OnCategorySelectedListener listener) {
        this.context = context;
        this.categoryNames = categoryNames;
        this.sharedPreferences = context.getSharedPreferences("com.halfcodz.ctrl_project.PREFS", Context.MODE_PRIVATE);
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
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousPosition);  // 이전 선택 항목 갱신
            notifyItemChanged(selectedPosition);  // 현재 선택 항목 갱신

            // 선택된 카테고리 이름을 SharedPreferences에 저장
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("selected_category_name", categoryName);
            editor.apply();

            // 콜백 호출하여 선택된 카테고리 전달
            if (onCategorySelectedListener != null) {
                onCategorySelectedListener.onCategorySelected(categoryName);
            }

            Toast.makeText(context, "선택된 카테고리: " + categoryName, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return categoryNames.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameText;
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameText = itemView.findViewById(R.id.control_item_text);
            radioButton = itemView.findViewById(R.id.todolist_add_RadioButton);
        }
    }
}