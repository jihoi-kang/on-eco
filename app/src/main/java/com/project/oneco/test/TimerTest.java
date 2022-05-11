package com.project.oneco.test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.os.Bundle;

import com.project.oneco.R;

public class TimerTest extends AppCompatActivity {

    private TextView countdownText; // 타이머 현황

    private Button startButton;
    private Button stopButton;
    private Button cancelButton;

    private EditText hourText;
    private EditText minText;
    private EditText secondText;

    private CountDownTimer countDownTimer;

    private boolean timerRunning;   // 타이머 상태
    private boolean firstState;     // 처음인지 아닌지

    private long time = 0;
    private long tempTime = 0;

    FrameLayout setting;    // 셋팅 화면
    FrameLayout timer;      // 타이머 화면


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_test);

        countdownText = findViewById(R.id.countdown_text);
        startButton = findViewById(R.id.countdown_button);
        stopButton = findViewById(R.id.stop_btn);
        cancelButton = findViewById(R.id.cancel_btn);

        hourText = findViewById(R.id.hour);
        minText = findViewById(R.id.min);
        secondText = findViewById(R.id.second);

        setting = findViewById(R.id.setting);
        timer = findViewById(R.id.timer);

        // 타이머 시작
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstState = true;
                setting.setVisibility(setting.GONE);    // 설정 사라짐
                timer.setVisibility(timer.VISIBLE);     // 타이머 생김
                startStop();
            }
        });

        // 일시 정지
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
            }
        });

        // 취소
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setting.setVisibility(setting.VISIBLE); // 설정 생김
                timer.setVisibility(timer.GONE);        // 타이머 사라짐
                firstState = true;
                stopTimer();
            }
        });
        updateTimer();
    }

    // 타이머 상태에 따른 시작 & 정지
    private void startStop(){
        if(timerRunning){   // 시작이면 정지
            stopTimer();
        } else {
            startTimer();   // 정지면 시작
        }
    }

    // 타이머 구현
    private void startTimer(){
        // 처음이면 설정 타이머 값을 사용한다.
        if(firstState){
            String sHour = hourText.getText().toString();
            String sMin = minText.getText().toString();
            String sSecond = secondText.getText().toString();
            time = (Long.parseLong(sHour) * 3600000) + (Long.parseLong(sMin) * 60000) + (Long.parseLong(sSecond) * 1000) + 1000;
        } else {
            time = tempTime;
        }

        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tempTime = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() { }
        }.start();

        stopButton.setText("일시정지");
        timerRunning = true;
        firstState = false;
    }

    // 타이머 정지
    private void stopTimer(){
        countDownTimer.cancel();
        timerRunning = false;
        stopButton.setText("계속");
    }

    // 시간 업데이트
    private void updateTimer(){
        int hour = (int) tempTime / 3600000;
        int minutes = (int) tempTime % 3600000 / 60000;
        int seconds = (int) tempTime % 3600000 % 60000 / 1000;

        String timeLeftText = "";

        timeLeftText = "" + hour + ":";

        // 분이 10보다 작으면 0이 붙는다
        if(minutes < 10) timeLeftText += "0";
        timeLeftText += minutes + ":";

        // 초가 10보다 작으면 0이 붙는다
        if(seconds <10) timeLeftText += "0";
        timeLeftText += seconds + ":";

        countdownText.setText(timeLeftText);

    }
}