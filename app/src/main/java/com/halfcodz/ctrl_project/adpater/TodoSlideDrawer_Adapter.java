package com.halfcodz.ctrl_project.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.data.Control;
import com.inhatc.real_project.R;

import java.util.List;

public class TodoSlideDrawer_Adapter extends RecyclerView.Adapter<TodoSlideDrawer_Adapter.ViewHolder> {

    private Context context;               // 수정: Context 추가
    private List<Control> controlList;     // 수정: Control 데이터 리스트 추가

    // 수정: 생성자 추가
    public TodoSlideDrawer_Adapter(Context context, List<Control> controlList) {
        this.context = context;
        this.controlList = controlList;
    }

    // 수정: ViewHolder 클래스 정의
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;   // 카테고리 이름 텍스트뷰
        CheckBox checkBox;       // 체크박스

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.slidedrawer_CategoryName);
            checkBox = itemView.findViewById(R.id.slidedrawer_Checkbox);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 수정: item_slidedrawer.xml 레이아웃을 inflate
        View view = LayoutInflater.from(context).inflate(R.layout.item_slidedrawer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 수정: Control 데이터를 ViewHolder에 바인딩
        Control control = controlList.get(position);
        holder.categoryName.setText(control.getControl_Item()); // 통제 항목 이름 설정
        holder.checkBox.setChecked(control.isChecked());        // 체크박스 상태 설정

        // 체크박스 클릭 이벤트 처리
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            control.setChecked(isChecked); // 상태 업데이트
        });
    }

    @Override
    public int getItemCount() {
        // 수정: 리스트 크기 반환
        return controlList != null ? controlList.size() : 0;
    }

    // 수정: 데이터 업데이트 메서드 추가
    public void updateData(List<Control> newControlList) {
        this.controlList = newControlList;
        notifyDataSetChanged(); // 데이터 변경 알림
    }
}