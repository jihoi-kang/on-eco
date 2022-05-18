package com.project.oneco;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class WaterStopGame extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    String[] item;

    // TimerTest.java
    private TextView selected_minText; // 타이머 현황

    private Button startButton;
    private Button stopButton;
    private Button Btn_finish_game;

    private CountDownTimer countDownTimer;

    private boolean timerRunning;   // 타이머 상태
    private boolean firstState;     // 처음인지 아닌지
    private boolean start_already;

    private long time = 0;
    private long tempTime = 0;
    private int secondText = 0;

    LinearLayout setting;    // 셋팅 화면
    LinearLayout timer;      // 타이머 화면


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_stop_game);

        spinner = (Spinner) findViewById(R.id.spinner);

        selected_minText = findViewById(R.id.selected_minText);
        startButton = findViewById(R.id.countdown_button);
        stopButton = findViewById(R.id.stopButton);
        Btn_finish_game = findViewById(R.id.Btn_finish_game);

        setting = findViewById(R.id.setting);
        timer = findViewById(R.id.timeup);


        timer.setVisibility(View.GONE);

        // 상황에 따른 이전 버튼 동작 선택
        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
                if (start_already) {
                    // 취소하시겠습니까? 팝업창을 띄움
                    AlertDialog.Builder dlg_sure_out = new AlertDialog.Builder(WaterStopGame.this);
                    dlg_sure_out.setMessage("취소하시겠습니까?");
                    dlg_sure_out.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            startStop();
                            Toast.makeText(WaterStopGame.this, "계속합니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dlg_sure_out.setNegativeButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setting.setVisibility(View.VISIBLE); // 설정 생김
                            timer.setVisibility(View.GONE);        // 타이머 사라짐
                            firstState = true;
                            stopTimer();
                            updateTimer();
                            start_already = false;
                            selected_minText.setText("00:00");
                        }
                    });
                    dlg_sure_out.show();
                } else {
                    onBackPressed();
                }
            }
        });


        // 물 사용 통계 화면 넘어가기
        // todo: 통계화면 넘어갈 때 물 vs 쓰레기 중 먼저 보여주는 그래프가 다르도록 구현
        ImageButton Wstatistic = findViewById(R.id.Wstatistic);
        Wstatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Statistic.class);
                startActivity(intent);
            }
        });

        // 게임 후 물 사용 통계 화면 넘어가기
        // todo: 통계화면 넘어갈 때 물 vs 쓰레기 중 먼저 보여주는 그래프가 다르도록 구현
        Btn_finish_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterAfterStati.class);
                startActivity(intent);
            }
        });


        // 스피너 구현
        spinner.setOnItemSelectedListener(this);

        String m3 = "03:00";
        String m5 = "05:00";
        String m7 = "07:00";
        String m10 = "10:00";
        String m15 = "15:00";

        item = new String[]{"선택하세요", m3, m5, m7, m10, m15};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner.setAdapter(adapter);


        // 타이머 시작
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstState = true;
                start_already = true;
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
                start_already = true;
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
            String choosedM = selected_minText.getText().toString();
            String sMin = choosedM.substring(0,2);
            String sSecond = String.valueOf(secondText);
            time = ((Long.parseLong(sMin) * 60*1000) + (Long.parseLong(sSecond) * 1000) + 1000);
        } else {
            time = tempTime;
        }

        Log.d("tag", "time" + time);

        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Log.d("tag", "tempTime: " + tempTime);
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
        int one_hour = 1000 * 60 * 60;
        int one_min = 1000 * 60;
        int one_sec = 1000;

        // 420000;
        // 몫: 시간
        // 나머지: 분, 초

        int minutes = (int) tempTime % one_hour / one_min;
        int seconds = (int) tempTime % one_hour % one_min / one_sec;

        Log.d("tag", "minutes: " + minutes);
        Log.d("tag", "seconds: " + seconds);

        String timeLeftText = "";

        // 분이 10보다 작으면 0이 붙는다
        if (minutes < 10) timeLeftText += "0";
        timeLeftText += minutes + ":";

        // 초가 10보다 작으면 0이 붙는다
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        //Log.d("tag", " timeLeftText: " + timeLeftText);

        selected_minText.setText(timeLeftText);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selected_minText.setText(item[i]);
        if(selected_minText.getText().toString().equals("선택하세요")){
            selected_minText.setText("00:00");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        selected_minText.setText("");
    }
}
