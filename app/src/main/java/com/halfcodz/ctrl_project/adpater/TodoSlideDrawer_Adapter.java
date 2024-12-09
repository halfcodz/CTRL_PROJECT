package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.R;
import com.halfcodz.ctrl_project.data.Control;

import java.util.Iterator;
import java.util.List;

public class TodoSlideDrawer_Adapter extends RecyclerView.Adapter<TodoSlideDrawer_Adapter.ViewHolder> {

    private final Context context;
    private List<Control> controlItems;

    public TodoSlideDrawer_Adapter(Context context, List<Control> controlItems) {
        this.context = context;
        this.controlItems = controlItems;
        removeNullItems(); // 초기화 시 null 값 항목 제거
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

        if (control.getControlItem() != null) {
            holder.controlItemName.setText(control.getControlItem());
        } else {
            // null 값이 발견되면 항목 제거
            removeItem(position);
        }
    }

    @Override
    public int getItemCount() {
        return controlItems != null ? controlItems.size() : 0;
    }

    // null 값이 있는 항목 제거 메서드
    private void removeNullItems() {
        Iterator<Control> iterator = controlItems.iterator();
        while (iterator.hasNext()) {
            Control control = iterator.next();
            if (control.getControlItem() == null) {
                iterator.remove();
            }
        }
    }

    // 특정 위치의 항목 제거 메서드
    private void removeItem(int position) {
        controlItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, controlItems.size());
    }

    // 추가된 updateData 메서드
    public void updateData(List<Control> newControlItems) {
        this.controlItems = newControlItems;
        removeNullItems(); // 새로운 데이터에서도 null 값 제거
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
