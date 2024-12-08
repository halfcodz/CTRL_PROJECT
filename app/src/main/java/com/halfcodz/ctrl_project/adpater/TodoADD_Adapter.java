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

import com.halfcodz.ctrl_project.R;
import com.halfcodz.ctrl_project.data.Control;

import java.util.List;

public class TodoADD_Adapter extends RecyclerView.Adapter<TodoADD_Adapter.ViewHolder> {

    private List<String> categoryNames;
    private List<Control> controlItem;
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
        Control control = controlItem.get(position);
        String categoryName = categoryNames.get(position);
        holder.categoryNameText.setText(categoryName);
        holder.todoName.setText(controlItem.toString());
        holder.radioButton.setChecked(position == selectedPosition);

        holder.radioButton.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
            onCategorySelectedListener.onCategorySelected(categoryName);
        });

        holder.radioButton.setOnClickListener(v -> {
            saveSelectedControlItem(control.todoName);
            if (recordButtonClickListener != null) {
                recordButtonClickListener.onRecordButtonClick(todo);
            }
        });
    }

    private void saveSelectedControlItem(String controlItemText) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selected_control_item", controlItemText);
        editor.apply();
        Toast.makeText(context, "선택된 통제 항목이 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return categoryNames != null ? categoryNames.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameText;
        RadioButton radioButton;
        TextView todoName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameText = itemView.findViewById(R.id.control_item_text);
            radioButton = itemView.findViewById(R.id.todolist_add_RadioButton);
            todoName = itemView.findViewById(R.id.todoName);
        }
    }
}
