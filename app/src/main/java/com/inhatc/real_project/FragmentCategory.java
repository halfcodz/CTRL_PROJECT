package com.inhatc.real_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.inhatc.real_project.adpater.CategoryAdapter;
import com.inhatc.real_project.data.AppDatabase;
import com.inhatc.real_project.data.Category;
import com.inhatc.real_project.ui.AddCategory;
import com.inhatc.real_project.ui.CategoryDetail;

import java.util.List;
import java.util.concurrent.Executors;

public class FragmentCategory extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
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
        view.findViewById(R.id.addTodoButton).setOnClickListener(v -> {
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
            List<Category> categories = db.categoryDao().getAllCategories();
            requireActivity().runOnUiThread(() -> {
                if (categoryAdapter == null) {
                    // Adapter 초기화
                    categoryAdapter = new CategoryAdapter(categories, category -> {
                        Intent intent = new Intent(getActivity(), CategoryDetail.class);
                        intent.putExtra("categoryId", category.getId());
                        startActivity(intent);
                    });
                    recyclerView.setAdapter(categoryAdapter);
                } else {
                    // 기존 Adapter 업데이트
                    categoryAdapter.updateCategories(categories);
                }
            });
        });
    }
}