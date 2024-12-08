package com.halfcodz.ctrl_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class FragmentCategory extends Fragment {

    private static final int CATEGORY_DETAIL_REQUEST_CODE = 2;

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
                if (position >= 0 && position < categories.size()) {
                    Control clickedCategory = categories.get(position);
                    Intent intent = new Intent(getActivity(), CategoryDetail.class);
                    intent.putExtra("categoryId", clickedCategory.getId());
                    intent.putExtra("categoryName", clickedCategory.getCategoryName());
                    startActivityForResult(intent, CATEGORY_DETAIL_REQUEST_CODE);
                }
            }

            @Override
            public void onCategoryDelete(int position) {
                if (position >= 0 && position < categories.size()) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        Control categoryToDelete = categories.get(position);
                        db.controlDao().delete(categoryToDelete);

                        requireActivity().runOnUiThread(() -> {
                            categories.remove(position);
                            categoryAdapter.notifyItemRemoved(position);
                            Log.d("FragmentCategory", "Category deleted and UI updated.");
                        });
                    });
                }
            }
        });

        recyclerView.setAdapter(categoryAdapter);

        addCategoryButton = view.findViewById(R.id.addCateogryButton);
        addCategoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddCategory.class);
            startActivityForResult(intent, CATEGORY_DETAIL_REQUEST_CODE);
        });

        loadCategories(); // 초기 데이터 로드
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCategories(); // 화면에 돌아왔을 때 데이터 새로 로드
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CATEGORY_DETAIL_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            loadCategories(); // CategoryDetail에서 돌아온 후 데이터 새로 로드
        }
    }

    private void loadCategories() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Control> fetchedCategories = db.controlDao().getAllCategories();

            requireActivity().runOnUiThread(() -> {
                categories.clear();
                if (fetchedCategories != null) {
                    categories.addAll(fetchedCategories);
                    Log.d("FragmentCategory", "Categories loaded: " + fetchedCategories.size());
                }
                categoryAdapter.notifyDataSetChanged();
            });
        });
    }
}