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
    private List<Control> controlList;

    public TodoSlideDrawer_Adapter(Context context, List<Control> controlList) {
        this.context = context;
        this.controlList = controlList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slidedrawer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (controlList == null || controlList.isEmpty()) {
            holder.controlItemName.setText("통제 항목이 없습니다.");
        } else {
            holder.controlItemName.setText(controlList.get(position).getControlItem());
        }
    }

    @Override
    public int getItemCount() {
        return controlList != null ? controlList.size() : 0;
    }

    public void updateData(List<Control> newControlList) {
        this.controlList = newControlList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView controlItemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            controlItemName = itemView.findViewById(R.id.slidedrawer_Control_Item);
        }
    }
}