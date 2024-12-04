package com.halfcodz.ctrl_project;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.halfcodz.ctrl_project.data.AppDatabase;
import com.halfcodz.ctrl_project.data.Focus;
import com.inhatc.real_project.R;

import java.util.Timer;
import java.util.TimerTask;

public class FocusMode extends AppCompatActivity {

    LinearLayout timeCountSettingLV, timeCountLV, lockScreenView;
    EditText hourET, minuteET, secondET;
    TextView hourTV, minuteTV, secondTV;
    Button startBtn;
    int hour, minute, second;
    private boolean isFocusModeStarted = false; // 집중 모드 시작 여부 추적 변수

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focusmode);

        // 레이아웃 및 뷰 초기화
        timeCountSettingLV = findViewById(R.id.timeCountSettingLV);
        timeCountLV = findViewById(R.id.timeCountLV);
        lockScreenView = findViewById(R.id.lockScreenView);

        hourET = findViewById(R.id.hourET);
        minuteET = findViewById(R.id.minuteET);
        secondET = findViewById(R.id.secondET);
        hourTV = findViewById(R.id.hourTV);
        minuteTV = findViewById(R.id.minuteTV);
        secondTV = findViewById(R.id.secondTV);
        startBtn = findViewById(R.id.startBtn);

        // 초기 상태에서 startBtn을 비활성화
        startBtn.setEnabled(false);

        // EditText에 텍스트가 변경될 때마다 호출되는 TextWatcher 설정
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                checkStartButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        hourET.addTextChangedListener(textWatcher);
        minuteET.addTextChangedListener(textWatcher);
        secondET.addTextChangedListener(textWatcher);

        // startBtn 클릭 시 타이머 시작
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBtn.setEnabled(false); // 버튼 비활성화
                isFocusModeStarted = true;  // 집중 모드 시작 상태로 설정
                startFocusMode();
            }
        });
    }

    private void startFocusMode() {
        timeCountSettingLV.setVisibility(View.GONE);
        timeCountLV.setVisibility(View.VISIBLE);

        // EditText에서 입력된 시간 값을 가져옴
        String hourStr = hourET.getText().toString();
        String minuteStr = minuteET.getText().toString();
        String secondStr = secondET.getText().toString();

        // 입력된 값이 없으면 기본값 설정
        if (hourStr.isEmpty()) hourStr = "00";
        if (minuteStr.isEmpty()) minuteStr = "00";
        if (secondStr.isEmpty()) secondStr = "00";

        // 두 자릿수 포맷팅
        hourStr = String.format("%02d", Integer.parseInt(hourStr));
        minuteStr = String.format("%02d", Integer.parseInt(minuteStr));
        secondStr = String.format("%02d", Integer.parseInt(secondStr));

        hourTV.setText(hourStr);
        minuteTV.setText(minuteStr);
        secondTV.setText(secondStr);

        try {
            hour = Integer.parseInt(hourStr);
            minute = Integer.parseInt(minuteStr);
            second = Integer.parseInt(secondStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        // Focus 객체 생성하여 데이터베이스에 저장
        Focus focus = new Focus();
        focus.focus_hour = hourStr;
        focus.focus_minutes = minuteStr;
        focus.focus_second = secondStr;
        focus.focus_text = "집중 타이머";

        // 데이터베이스에 삽입 (백그라운드 스레드에서 처리)
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getDatabase(getApplicationContext()).focusDao().insert(focus);
            }
        }).start();

        // 잠금 화면 보이기
        lockScreenView.setVisibility(View.VISIBLE);

        // 앱 고정
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            startLockTask(); // 앱 고정 시작
        }

        // 타이머 설정 및 실행
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                updateTimer(timer);
            }
        };

        timer.schedule(timerTask, 0, 1000); // 1초 간격으로 실행
    }

    private void updateTimer(Timer timer) {
        if (second != 0) {
            second--;
        } else if (minute != 0) {
            second = 60;
            second--;
            minute--;
        } else if (hour != 0) {
            second = 60;
            minute = 60;
            second--;
            minute--;
            hour--;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hourTV.setText(String.format("%02d", hour));
                minuteTV.setText(String.format("%02d", minute));
                secondTV.setText(String.format("%02d", second));

                // 시간이 0이 되면 타이머 종료
                if (hour == 0 && minute == 0 && second == 0) {
                    timer.cancel();
                    lockScreenView.setVisibility(View.GONE);
                    finish(); // 액티비티 종료
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        stopLockTask(); // 앱 고정 해제
                    }
                }
            }
        });
    }

    // 버튼 활성화/비활성화 체크 메서드
    private void checkStartButtonState() {
        String hourStr = hourET.getText().toString();
        String minuteStr = minuteET.getText().toString();
        String secondStr = secondET.getText().toString();

        // secondET가 비어있거나 값이 0이면 버튼 비활성화
        if (secondStr.isEmpty()) {
            secondStr = "00";
        }

        int secondValue = Integer.parseInt(secondStr);

        // secondET가 1 이상일 때만 버튼 활성화
        startBtn.setEnabled(secondValue > 0 || !hourStr.isEmpty() || !minuteStr.isEmpty());
    }

    // 뒤로 가기 버튼을 막기 위한 메서드 오버라이드
    @Override
    public void onBackPressed() {
        if (isFocusModeStarted) {
            // 집중 모드가 시작된 경우 뒤로 가기 버튼 무시
            return; // 아무 동작도 하지 않음
        }
        super.onBackPressed(); // 기본 동작 (집중 모드가 시작되지 않은 경우)
    }

    // 뒤로 가기 버튼을 막는 방법 - 키 이벤트 처리
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isFocusModeStarted) {
            // 집중 모드가 시작된 경우 뒤로 가기 버튼 막기
            return true; // 뒤로 가기 동작 무시
        }
        return super.onKeyDown(keyCode, event); // 기본 동작
    }

    // 홈 버튼을 막기 위한 코드 (효과가 미미할 수 있음)
    @Override
    public void onUserLeaveHint() {
        if (isFocusModeStarted) {
            // 홈 버튼을 누르면 아무런 동작을 하지 않도록 처리
            // 앱을 강제로 포그라운드 상태로 유지
            moveTaskToBack(true);
        }
        super.onUserLeaveHint();
    }
}