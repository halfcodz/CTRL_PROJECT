package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.data.Control;
import com.inhatc.real_project.R;

import java.util.List;

public class TodoSlideDrawer_Adapter extends RecyclerView.Adapter<TodoSlideDrawer_Adapter.ViewHolder> {

    private final Context context;
    private List<Control> controlItems;

    public TodoSlideDrawer_Adapter(Context context, List<Control> controlItems) {
        this.context = context;
        this.controlItems = controlItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slidedrawer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Control control = controlItems.get(position);
        holder.controlItemName.setText(control.getControlItem() != null ? control.getControlItem() : "항목 없음");
    }

    @Override
    public int getItemCount() {
        return controlItems.size();
    }

    // 추가된 updateData 메서드
    public void updateData(List<Control> newControlItems) {
        this.controlItems = newControlItems;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView controlItemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            controlItemName = itemView.findViewById(R.id.controlItemName);
        }
    }
}