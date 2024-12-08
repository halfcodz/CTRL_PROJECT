package com.halfcodz.ctrl_project.adpater;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Focus;
import com.halfcodz.ctrl_project.data.FocusDao;
import com.inhatc.real_project.R;

import java.util.Collections;
import java.util.List;

public class FocusAdapter extends RecyclerView.Adapter<FocusAdapter.FocusViewHolder> {

    private List<Focus> focusList; // Focus 리스트를 사용

    public FocusAdapter(List<Focus> focusList) {
        this.focusList = focusList;
    }

    @NonNull
    @Override
    public FocusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_stopwatch, parent, false);
        return new FocusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FocusViewHolder holder, int position) {
        Focus focus = focusList.get(position);
        holder.focusHour.setText(focus.focus_hour);
        holder.focusMinutes.setText(focus.focus_minutes);
        holder.focusSecond.setText(focus.focus_second);

        holder.focus_del.setOnClickListener(view -> {
            // 삭제할 항목을 먼저 저장
            Focus focusDelete = focusList.get(position);

            // UI에서 항목 삭제
            focusList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, focusList.size());

            // 데이터베이스에서 항목 삭제 (스레드 내에서)
            new Thread(() -> {
                // AppDatabase에서 FocusDao를 가져옴
                Context context = holder.itemView.getContext();
                FocusDao focusDao = AppDatabase.getDatabase(context).focusDao(); // FocusDao 사용
                focusDao.delete(Collections.singletonList(focusDelete));  // Focus 삭제
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return focusList.size();
    }

    public void setFocusList(List<Focus> newFocusList) {
        this.focusList = newFocusList;
        notifyDataSetChanged();
    }

    public static class FocusViewHolder extends RecyclerView.ViewHolder {

        ImageView recycler_image;
        TextView focusHour, focusMinutes, focusSecond, focus_second;
        Button focus_del;

        public FocusViewHolder(@NonNull View itemView) {
            super(itemView);
            recycler_image = itemView.findViewById(R.id.recycler_image);
            focusHour = itemView.findViewById(R.id.focus_hour);
            focusMinutes = itemView.findViewById(R.id.focus_minutes);
            focusSecond = itemView.findViewById(R.id.focus_second);
            focus_second = itemView.findViewById(R.id.focus_second);
            focus_del = itemView.findViewById(R.id.focus_del);
        }
    }
}