package com.halfcodz.ctrl_project;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.button.MaterialButton;
import com.halfcodz.ctrl_project.adpater.CategoryMain_Adapter;
import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Control;
import com.halfcodz.ctrl_project.ui.AddCategory;
import com.halfcodz.ctrl_project.ui.CategoryDetail;

import java.util.List;
import java.util.concurrent.Executors;

public class FragmentCategory extends Fragment {

    private RecyclerView recyclerView;
    private CategoryMain_Adapter categoryAdapter;
    private AppDatabase db;
    private Button addCategoryButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = AppDatabase.getDatabase(requireContext());

        addCategoryButton = view.findViewById(R.id.addCateogryButton);
        addCategoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddCategory.class);
            startActivity(intent);
        });

        loadCategories();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCategories();
    }

    private void loadCategories() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Control> categories = db.controlDao().getAllCategories();

            requireActivity().runOnUiThread(() -> {
                if (categories == null || categories.isEmpty()) {
                    Toast.makeText(getContext(), "카테고리가 없습니다. 추가해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (categoryAdapter == null) {
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
                } else {
                    categoryAdapter.updateCategories(categories);
                }
            });
        });
    }
}
