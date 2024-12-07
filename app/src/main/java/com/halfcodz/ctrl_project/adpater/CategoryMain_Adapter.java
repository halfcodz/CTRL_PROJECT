package com.halfcodz.ctrl_project.adpater;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.R;
import com.halfcodz.ctrl_project.data.Control;
import com.halfcodz.ctrl_project.ui.CategoryDetail;

import java.util.ArrayList;
import java.util.List;

public class CategoryMain_Adapter extends RecyclerView.Adapter<CategoryMain_Adapter.ViewHolder> {

    private List<Control> categoryList; // 카테고리 리스트
    private final OnCategoryInteractionListener interactionListener; // 인터랙션 리스너

    // 인터페이스: 카테고리 클릭 및 삭제 이벤트 처리
    public interface OnCategoryInteractionListener {
        void onCategoryClick(int position);  // 카테고리 클릭 이벤트
        void onCategoryDelete(int position); // 카테고리 삭제 이벤트
    }

    // 생성자
    public CategoryMain_Adapter(List<Control> categoryList, OnCategoryInteractionListener listener) {
        this.categoryList = (categoryList != null) ? categoryList : new ArrayList<>(); // Null 방어
        this.interactionListener = listener; // 리스너 초기화
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 아이템 레이아웃 설정
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 데이터 바인딩
        Control category = categoryList.get(position);
        holder.todoName.setText(category.getCategoryName()); // 수정된 부분: 카테고리 이름 설정

        // 카테고리 클릭 이벤트
        holder.itemView.setOnClickListener(view -> interactionListener.onCategoryClick(position));

        // 상세 화면으로 이동하는 클릭 리스너
        holder.todoName.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CategoryDetail.class);
            intent.putExtra("category_id", category.getId()); // 카테고리 ID 전달
            view.getContext().startActivity(intent);
        });

        // 삭제 버튼 클릭 이벤트 처리
        holder.deleteButton.setOnClickListener(v -> interactionListener.onCategoryDelete(position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size(); // 리스트 크기 반환
    }

    // 새로운 카테고리 데이터를 업데이트하는 메서드
    public void updateCategories(List<Control> newCategoryList) {
        this.categoryList = newCategoryList; // 기존 데이터 리스트를 새로운 리스트로 교체
        notifyDataSetChanged(); // RecyclerView를 갱신
    }

    // ViewHolder 클래스
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView todoName;       // 카테고리 이름 표시용 TextView
        ImageButton deleteButton; // 삭제 버튼

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todoName = itemView.findViewById(R.id.Main_todoName);     // item_main_category.xml의 TextView와 연결
            deleteButton = itemView.findViewById(R.id.Main_deleteButton); // item_main_category.xml의 ImageButton과 연결
        }
    }
}
