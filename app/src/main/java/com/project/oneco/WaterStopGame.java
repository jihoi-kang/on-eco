package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class WaterStopGame extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    String[] item;

    // TimerTest.java
    private TextView seleted_minText; // 타이머 현황

    private Button startButton;
    private Button stopButton;
    private Button cancelButton;

    private TextView secondText;

    private CountDownTimer countDownTimer;

    private boolean timerRunning;   // 타이머 상태
    private boolean firstState;     // 처음인지 아닌지

    private long time = 0;
    private long tempTime = 0;

    LinearLayout setting;    // 셋팅 화면
    LinearLayout timer;      // 타이머 화면


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_stop_game);

        seleted_minText = (TextView) findViewById(R.id.seleted_minText);
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        item = new String[]{"선택하세요", "03", "05", "07", "10", "15"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner.setAdapter(adapter);



        // TimerTest.java
        seleted_minText = findViewById(R.id.seleted_minText);
        startButton = findViewById(R.id.countdown_button);
        stopButton = findViewById(R.id.stop_btn);
        cancelButton = findViewById(R.id.cancel_btn);

        seleted_minText = findViewById(R.id.min);
        secondText = findViewById(R.id.second);

        setting = findViewById(R.id.setting);
        timer = findViewById(R.id.timer);

        // 타이머 시작
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstState = true;
                setting.setVisibility(View.GONE);    // 설정 사라짐
                timer.setVisibility(View.VISIBLE);     // 타이머 생김
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
                setting.setVisibility(View.VISIBLE); // 설정 생김
                timer.setVisibility(View.GONE);        // 타이머 사라짐
                firstState = true;
                stopTimer();
            }
        });
        updateTimer();
    }

    // 타이머 상태에 따른 시작 & 정지
    private void startStop() {
        if (timerRunning) {   // 시작이면 정지
            stopTimer();
        } else {
            startTimer();   // 정지면 시작
        }
    }

    // 타이머 구현
    private void startTimer() {
        // 처음이면 설정 타이머 값을 사용한다.
        if (firstState) {
            String sMin = seleted_minText.getText().toString();
            String sSecond = secondText.getText().toString();
            time = ((Long.parseLong(sMin) * 60000) + (Long.parseLong(sSecond) * 1000) + 1000);
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
            public void onFinish() {
            }
        }.start();

        stopButton.setText("일시정지");
        timerRunning = true;
        firstState = false;
    }

    // 타이머 정지
    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        stopButton.setText("계속");
    }

    // 시간 업데이트
    private void updateTimer() {
        int minutes = (int) tempTime % 3600000 / 60000;
        int seconds = (int) tempTime % 3600000 % 60000 / 1000;

        String timeLeftText = "";

        // 분이 10보다 작으면 0이 붙는다
        if (minutes < 10) timeLeftText += "0";
        timeLeftText += minutes + ":";

        // 초가 10보다 작으면 0이 붙는다
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        seleted_minText.setText(timeLeftText);

    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        seleted_minText.setText(item[i]);
        if(seleted_minText.getText().toString().equals("선택하세요")){
            seleted_minText.setText("");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        seleted_minText.setText("");
    }
}
