package com.project.oneco;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WaterStopWatch extends AppCompatActivity {

    private TextView countup_text; // 타이머 현황

    private Button Btn_start_ST;
    private Button Btn_stop_ST;
    private Button Btn_finish_ST;

    private Timer countUpTimer;
    private TimerTask second;
    private int timerSec = 0;

    private boolean timerRunning;   // 타이머 상태
    private boolean firstState;     // 처음인지 아닌지
    private boolean start_already;

    private long time = 0;

    LinearLayout setting;    // 셋팅 화면
    LinearLayout timeup;      // 타이머 화면


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_stop_game);


        countup_text = findViewById(R.id.countup_text);
        Btn_start_ST = findViewById(R.id.countdown_button);
        Btn_stop_ST = findViewById(R.id.Btn_stop_ST);
        Btn_finish_ST = findViewById(R.id.Btn_finish_ST);

        setting = findViewById(R.id.setting);
        timeup = findViewById(R.id.timeup);


        timeup.setVisibility(View.GONE);

        // 상황에 따른 이전 버튼 동작 선택
        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
                if (start_already) {
                    // 취소하시겠습니까? 팝업창을 띄움
                    AlertDialog.Builder dlg_sure_out = new AlertDialog.Builder(WaterStopWatch.this);
                    dlg_sure_out.setMessage("취소하시겠습니까?");
                    dlg_sure_out.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            startStop();
                            Toast.makeText(WaterStopWatch.this, "계속합니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dlg_sure_out.setNegativeButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setting.setVisibility(View.VISIBLE); // 설정 생김
                            timeup.setVisibility(View.GONE);        // 타이머 사라짐
                            firstState = true;
                            stopTimer();
                            updateTimer();
                            start_already = false;
                            countup_text.setText("00:00");
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
        Btn_finish_ST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterAfterStati.class);
                startActivity(intent);
            }
        });



        // 타이머 시작
        Btn_start_ST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstState = true;
                start_already = true;
                setting.setVisibility(View.GONE);    // 설정 사라짐
                timeup.setVisibility(View.VISIBLE);     // 타이머 생김
                startStop();
            }
        });

        // 일시 정지
        Btn_stop_ST.setOnClickListener(new View.OnClickListener() {
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
        // 처음이면 00:00으로 UI 변경
        if (firstState) {
            countup_text.setText("00:00");
        }

        // 초기화 작업
        timerSec = 0;
        second = new TimerTask() {  // TimerTask는 안드로이드 클래스
            @Override
            public void run() {
                Log.d("jay", "Timer start: " + timerSec); // 로그 찍기
            }
        };
        Timer CountUpTimer = new Timer();  // Timer는 안드로이드 클래스
        CountUpTimer.schedule(second, 0, 1000);

        Btn_stop_ST.setText("일시정지");
        timerRunning = true;
        firstState = false;
    }

    // 타이머 정지
    private void stopTimer() {
        countUpTimer.cancel();
        timerRunning = false;
        Btn_stop_ST.setText("계속");
    }

    // 시간 업데이트
    private void updateTimer() {
        int one_day = 1000 * 60 * 60 * 24;
        int one_hour = 1000 * 60 * 60;
        int one_min = 1000 * 60;
        int one_sec = 1000;

        int hours = (int) time % one_day / one_hour;
        int minutes = (int) time % one_hour / one_min;
        int seconds = (int) time % one_hour % one_min / one_sec;

        String timeLeftText = "";

        // 분이 10보다 작으면 0이 붙는다
        if (hours < 10) timeLeftText += "0";
        timeLeftText += hours + ":";

        // 분이 10보다 작으면 0이 붙는다
        if (minutes < 10) timeLeftText += "0";
        timeLeftText += minutes + ":";

        // 초가 10보다 작으면 0이 붙는다
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        Log.d("tag", " timeLeftText: " + timeLeftText);

        countup_text.setText(timeLeftText);

    }
}


//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.LinearLayout;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class WaterStopWatch extends AppCompatActivity {
//
//    private boolean start_already;
//    private boolean timerRunning;   // 타이머 상태
//    private boolean firstState;     // 처음인지 아닌지
//
//    LinearLayout setting;    // 셋팅 화면
//    LinearLayout timeup;      // 타이머 화면
//
//    private TextView countup_text;
//    private TimerTask second;
//    private int timerSec = 0;
//    private String timeLeftText = "";
//    private final Handler handler = new Handler();
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_water_stop_watch);
//
//        // todo: SoundMeter 이용하여 기능 구현(TestActivity 참고)
//
//        setting = findViewById(R.id.setting);
//        timeup = findViewById(R.id.timeup);
//
//        countup_text = findViewById(R.id.countup_text);
//
//        Button Btn_start_ST = findViewById(R.id.Btn_start_ST);
//        Button Btn_stop_ST = findViewById(R.id.Btn_stop_ST);
//
//
//        timeup.setVisibility(View.GONE);     // 타이업 조작 버튼 사라짐
//
//
//        // 상황에 따른 이전 버튼 동작 선택
//        ImageButton Btn_back = findViewById(R.id.Btn_back);
//        Btn_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (start_already) {
//                    // 종료하시겠습니까? 팝업창을 띄움
//                    AlertDialog.Builder dlg_sure_out = new AlertDialog.Builder(WaterStopWatch.this);
//                    dlg_sure_out.setMessage("취소하시겠습니까?");
//                    dlg_sure_out.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int which) {
//                            Toast.makeText(WaterStopWatch.this, "계속합니다.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    dlg_sure_out.setNegativeButton("네", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            onBackPressed();
//                            start_already = false;
//                        }
//                    });
//                    dlg_sure_out.show();
//                } else {
//                    onBackPressed();
//                    start_already = false;
//                }
//            }
//        });
//
//        // 물 사용 통계 화면 넘어가기
//        // todo: 통계화면 넘어갈 때 물 vs 쓰레기 중 먼저 보여주는 그래프가 다르도록 구현
//        ImageButton Wstatistic = findViewById(R.id.Wstatistic);
//        Wstatistic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Statistic.class);
//                startActivity(intent);
//            }
//        });
//
//        // WaterAfterStati(물 사용 측정 후 Text 통계 화면)로 넘어가기
//        Button btn_finish_ST = findViewById(R.id.Btn_finish_ST);
//        btn_finish_ST.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), WaterAfterStati.class);
//                startActivity(intent);
//            }
//        });
//
//
//        // 측정 시작 버튼 누르면 기록 & 타이머 프레임 레이아웃 보이기
//        Btn_start_ST.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firstState = false;
//                start_already = true;
//                setting.setVisibility(View.GONE);    // 설정 사라짐
//                timeup.setVisibility(View.VISIBLE);     // 타임업 조작 버튼 생김
//
//                startTimeup();
//            }
//        });
//
//        // 중지 버튼 눌렀을 때
//        Btn_stop_ST.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//    }
//
//
//    // 타이머 시작 메서드
//    public void startTimeup() {
//        // 초기화 작업
//        timerSec = 0;
//        second = new TimerTask() {  // TimerTask는 안드로이드 클래스
//            @Override
//            public void run() {
//                Log.d("jay", "Timer start: " + timerSec); // 로그 찍기
//                timerSec += 1000;
//
//                int one_day = 1000 * 60 * 60 * 24;
//                int one_hour = 1000 * 60 * 60;
//                int one_min = 1000 * 60;
//                int one_sec = 1000;
//
//                int hours = (int) timerSec % one_day / one_hour;
//                int minutes = (int) timerSec % one_hour / one_min;
//                int seconds = (int) timerSec % one_hour % one_min / one_sec;
//
//
//                // 분이 10보다 작으면 0이 붙는다
//                if (hours < 10) timeLeftText += "0";
//                timeLeftText += hours + ":";
//
//                // 분이 10보다 작으면 0이 붙는다
//                if (minutes < 10) timeLeftText += "0";
//                timeLeftText += minutes + ":";
//
//                // 초가 10보다 작으면 0이 붙는다
//                if (seconds < 10) timeLeftText += "0";
//                timeLeftText += seconds;
//
//                getUITime();
//            }
//        };
//        Timer timeup = new Timer();  // Timer는 안드로이드 클래스
//        timeup.schedule(second, 0, 1000);
//
//    }
//
//
//    private void getUITime() {
//        Runnable updater = new Runnable() {
//            public void run() {
//                // 여기서부터는 main(UI) thread를 활용한다.
//                countup_text.setText(timeLeftText);
//            }
//        };
//        handler.post(updater);
//    }
//
//}