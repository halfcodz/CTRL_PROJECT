package com.halfcodz.ctrl_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentPet extends Fragment {

    private ProgressBar progressBar;
    private ImageView characterImage;
    private Button clickButton;

    private int clickCount = 0;
    private int stage = 1; // 캐릭터 단계

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment의 View를 생성
        View view = inflater.inflate(R.layout.fragment_pet, container, false);

        // UI 요소 초기화
        progressBar = view.findViewById(R.id.progressBar);
        characterImage = view.findViewById(R.id.characterImage);
        clickButton = view.findViewById(R.id.clickButton);

        // 클릭 버튼 이벤트 설정
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick();
            }
        });


        return view;
    }

    private void handleButtonClick() {
        clickCount++;
        progressBar.setProgress(clickCount);

        if (clickCount >= 10) {
            evolveCharacter();
            clickCount = 0; // 게이지 초기화
            progressBar.setProgress(clickCount);
        }
    }

    private void evolveCharacter() {
        stage++;
        switch (stage) {
            case 2:
                characterImage.setImageResource(R.drawable.yadoran);
                Toast.makeText(getActivity(), "캐릭터가 2단계로 진화했습니다!", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                characterImage.setImageResource(R.drawable.yadoking);
                Toast.makeText(getActivity(), "캐릭터가 3단계로 진화했습니다!", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(), "최종 진화 상태입니다!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void resetGame() {
        stage = 1; // 단계 초기화
        clickCount = 0; // 클릭 수 초기화
        progressBar.setProgress(0); // 게이지 초기화
        characterImage.setImageResource(R.drawable.yadoran); // 기본 이미지로 변경
        Toast.makeText(getActivity(), "초기화되었습니다. 다시 시작하세요!", Toast.LENGTH_SHORT).show();
    }
}
