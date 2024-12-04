package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.real_project.R;

import java.util.List;

public class TodoADD_Adapter extends RecyclerView.Adapter<TodoADD_Adapter.ViewHolder> {

    private List<String> categoryNames;
    private Context context;

    // 생성자
    public TodoADD_Adapter(Context context, List<String> categoryNames) {
        this.context = context;
        this.categoryNames = categoryNames;
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

        // 카테고리 이름을 TextView에 설정
        holder.categoryNameText.setText(categoryName);
        holder.checkBox.setChecked(false); // 기본적으로 체크되지 않음
    }

    @Override
    public int getItemCount() {
        return categoryNames != null ? categoryNames.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameText;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // item_todolistadd.xml의 View ID 연결
            categoryNameText = itemView.findViewById(R.id.control_item_text);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}