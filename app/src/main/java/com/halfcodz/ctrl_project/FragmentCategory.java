package com.halfcodz.ctrl_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.halfcodz.ctrl_project.adpater.CategoryMain_Adapter;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;
import com.halfcodz.ctrl_project.ui.AddCategory;
import com.halfcodz.ctrl_project.ui.CategoryDetail;
import com.inhatc.real_project.R;

import java.util.List;
import java.util.concurrent.Executors;

public class FragmentCategory extends Fragment {

    private RecyclerView recyclerView;
    private CategoryMain_Adapter categoryAdapter;
    private AppDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment의 View를 생성
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // RecyclerView 설정
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 데이터베이스 초기화
        db = AppDatabase.getDatabase(requireContext());

        // 추가 버튼 클릭 이벤트
        Button addTodoButton = view.findViewById(R.id.addCateogryButton);
        addTodoButton.setOnClickListener(v -> {
            // 버튼 클릭 시 동작
            Intent intent = new Intent(getActivity(), AddCategory.class);
            startActivity(intent);
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 카테고리 데이터를 비동기로 로드
        Executors.newSingleThreadExecutor().execute(() -> {
            // Control 테이블에서 데이터를 가져오기 - 수정된 부분
            List<Control> categories = db.controlDao().getTodosByCategoryId(0); // 모든 카테고리를 가져옴

            requireActivity().runOnUiThread(() -> {
                if (categoryAdapter == null) {
                    // Adapter 초기화
                    categoryAdapter = new CategoryMain_Adapter(categories, new CategoryMain_Adapter.OnCategoryInteractionListener() {
                        @Override
                        public void onCategoryClick(int position) {
                            // 카테고리 클릭 시 상세 화면으로 이동
                            Control clickedCategory = categories.get(position);
                            Intent intent = new Intent(getActivity(), CategoryDetail.class);
                            intent.putExtra("categoryId", clickedCategory.getId());
                            startActivity(intent);
                        }

                        @Override
                        public void onCategoryDelete(int position) {
                            Executors.newSingleThreadExecutor().execute(() -> {
                                // 삭제할 항목 가져오기
                                Control categoryToDelete = categories.get(position);

                                // 데이터베이스에서 삭제
                                db.controlDao().delete(categoryToDelete);

                                // UI 업데이트
                                requireActivity().runOnUiThread(() -> {
                                    categories.remove(position);
                                    categoryAdapter.notifyItemRemoved(position);
                                });
                            });
                        }
                    });
                    recyclerView.setAdapter(categoryAdapter);
                } else {
                    // 기존 Adapter 업데이트 - 수정된 부분
                    categoryAdapter.updateCategories(categories);
                }
            });
        });
    }
}