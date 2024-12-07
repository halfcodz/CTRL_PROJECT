package com.halfcodz.ctrl_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.inhatc.real_project.R;

public class FragmentComplete extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment의 View를 생성
        View view = inflater.inflate(R.layout.fragment_complete, container, false);
        return view;
    }
}