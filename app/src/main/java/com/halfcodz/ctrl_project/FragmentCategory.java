package com.halfcodz.ctrl_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class FragmentCategory extends Fragment {

    private static final int ADD_CATEGORY_REQUEST_CODE = 1;

    private RecyclerView recyclerView;
    private CategoryMain_Adapter categoryAdapter;
    private AppDatabase db;
    private Button addCategoryButton;
    private List<Control> categories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = AppDatabase.getDatabase(requireContext());
        categories = new ArrayList<>();
        categoryAdapter = new CategoryMain_Adapter(categories, new CategoryMain_Adapter.OnCategoryInteractionListener() {
            @Override
            public void onCategoryClick(int position) {
                Control clickedCategory = categories.get(position);
                Intent intent = new Intent(getActivity(), CategoryDetail.class);
                intent.putExtra("categoryId", clickedCategory.getId());
                startActivity(intent);
            }

            @Override
            public void onCategoryDelete(int position) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    Control categoryToDelete = categories.get(position);
                    db.controlDao().delete(categoryToDelete);

                    requireActivity().runOnUiThread(() -> {
                        categories.remove(position);
                        categoryAdapter.notifyItemRemoved(position);
                        Toast.makeText(getContext(), "카테고리가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    });
                });
            }
        });
        recyclerView.setAdapter(categoryAdapter);

        addCategoryButton = view.findViewById(R.id.addCateogryButton);
        addCategoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddCategory.class);
            startActivityForResult(intent, ADD_CATEGORY_REQUEST_CODE);
        });

        loadCategories();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CATEGORY_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            loadCategories(); // 명시적으로 데이터 로드
        }
    }

    private void loadCategories() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Control> fetchedCategories = db.controlDao().getAllCategories();

            // 디버그용 코드: 모든 Control 항목을 로그로 출력
            List<Control> allControls = db.controlDao().getAllControls();
            Log.d("DatabaseCheck", "All controls in the database:");
            for (Control control : allControls) {
                Log.d("DatabaseCheck", "Category Name: " + control.getCategoryName() + ", Control Item: " + control.getControlItem());
            }

            requireActivity().runOnUiThread(() -> {
                categories.clear();
                categories.addAll(fetchedCategories);
                categoryAdapter.notifyDataSetChanged();
            });
        });
    }
}