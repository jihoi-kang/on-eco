package com.project.oneco;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
//import com.project.oneco.test.WaterAfterStati;

// todo: 뒤로가기 버튼 누를 때 상황에 따라 다르게 구현

public class WaterStopWatch extends AppCompatActivity {

    // Activity간의 데이터 공유를 위한 application 가져오기1-1
    private OnEcoApplication application;

    private SoundMeter soundMeter = null;
    // todo:checkPermission class를 참조하여 객체를 만든 후 사용하고 싶은데 잘 안됨..
    //private CheckPermission checkPermission = null;

    // todo:왜 final인지?
    private ArrayList<Float> dbList = new ArrayList<>();
    private ArrayList<Float> dbUsedList = new ArrayList<>();
    private ArrayList<Float> dbNoUsedList = new ArrayList<>();

    private TextView countup_text; // 타이머 현황
    private final Handler handler = new Handler();

    private Button Btn_start_ST;
    private Button Btn_stop_ST;
    private Button Btn_finish_ST;

    private Timer countUpTimer;
    private TimerTask second;

    private boolean timerRunning;   // 타이머 상태
    private boolean firstState;     // 처음인지 아닌지
    private boolean start_already;  // 일시정지하면 처음은 아니지만 이미 시작한 상태

    LinearLayout setting;    // 셋팅 화면
    LinearLayout timeup;      // 타이머 화면


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_stop_watch);


        // Activity간의 데이터 공유를 위한 application 가져오기1-2
        application = (OnEcoApplication) getApplication();

        // WaterPower 팝업 띄우기
        startActivity(new Intent(this, WaterPower.class));

        countup_text = findViewById(R.id.countup_text);
        Btn_start_ST = findViewById(R.id.Btn_start_ST);
        Btn_stop_ST = findViewById(R.id.Btn_stop_ST);
        Btn_finish_ST = findViewById(R.id.Btn_finish_ST);

        setting = findViewById(R.id.setting);
        timeup = findViewById(R.id.timeup);


        timeup.setVisibility(View.GONE);

        // todo:checkPermission class를 참조하여 객체를 만든 후 사용하고 싶은데 잘 안됨..
//        checkPermission = new checkPermission();
//        checkPermission.checkPermission();

        soundMeter = new SoundMeter();

        // 상황에 따른 이전 버튼 동작 선택
        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start_already) {
                    stopTimer();

                    // 취소하시겠습니까? 팝업창을 띄움
                    AlertDialog.Builder dlg_sure_out = new AlertDialog.Builder(WaterStopWatch.this);
                    dlg_sure_out.setMessage("취소하시겠습니까?");
                    dlg_sure_out.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            //firstState = false;
                            //1초 텀 없이 바로 올라가는 문제 해결 필요
                            //startStop();  바로 시작하는 게 아니라 사용자가 시작하기 위해서 없앰
                            Toast.makeText(WaterStopWatch.this, "계속합니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dlg_sure_out.setNegativeButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setting.setVisibility(View.VISIBLE); // 설정 생김
                            timeup.setVisibility(View.GONE);        // 타이머 사라짐

                            firstState = true;
                            start_already = false;
                            // updateTimer();

                            // 초기화
                            countup_text.setText("00:00");
                            application.timerSec = 0;
                            application.RtimerSec = 0;
                            soundMeter = null;
                            dbList = new ArrayList<>();
                            dbUsedList = new ArrayList<>();
                            dbNoUsedList = new ArrayList<>();
                            // 수도꼭지, 수압 초기화
                            application.Wtap = "null";
                            application.Wpower = 0f;

                            setSec();
                        }
                    });
                    dlg_sure_out.show();
                } else {
                    onBackPressed();

                    // 수도꼭지, 수압 초기화
                    application.Wtap = "null";
                    application.Wpower = 0f;
                }
            }
        });


        // 종료버튼을 누르면
        Btn_finish_ST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // second와 soundMeter를 중지시킨다.
                second.cancel();
                soundMeter.stop();
                timerRunning = false;

                // 지금까지 모아둔 데시벨 값을 파싱한다.
                float total = 0f;
                for (int i = 0; i < dbList.size(); i++) {
                    total = total + dbList.get(i);
                }

                // 평균 데시벨
                float average = total / dbList.size();

                for (int i = 0; i < dbList.size(); i++) {
                    if (dbList.get(i) > average) {
                        // 물을 사용하는 중이라는 뜻
                        dbUsedList.add(dbList.get(i));
                    } else {
                        // 물을 사용하지 않고 있다는 뜻
                        dbNoUsedList.add(dbList.get(i));
                    }
                }

                application.usedWT = dbUsedList.size();
                application.noUsedWT = dbNoUsedList.size();

                application.usedW = application.usedWT * application.Wpower;


                // 물 사용 통계 화면 넘어가기
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
                firstState = false;
            }
        });
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

            // 초기화 작업
            application.timerSec = 0;
            application.RtimerSec = 0;
        }

        // 데시벨을 얻어오기 위해 sound meter 시작
        if (soundMeter == null) {
            soundMeter = new SoundMeter();
        }
        soundMeter.start();

        // 초 세면서 데시벨 값 저장하기
        second = new TimerTask() {  // TimerTask는 안드로이드 클래스
            @Override
            public void run() {

                // soundMeter getAmplitude() 작업 수행
                if (soundMeter == null) {
                    return;
                }
                double amplitude = soundMeter.getAmplitude();
                float db = 20 * (float) (Math.log10(amplitude));
                Log.d("jay", "db: " + db);

                // 데이터를 전역변수의 dbList로 저장한다.
                dbList.add(db);

                application.timerSec += 1000;
                application.RtimerSec++;
                setSec();
            }
        };

        Timer CountUpTimer = new Timer();  // Timer는 안드로이드 클래스
        CountUpTimer.schedule(second, 0, 1000);

        Btn_stop_ST.setText("일시정지");
        timerRunning = true;
        firstState = false;
    }

    // Time up UI 변경 메서드
    private void setSec() {
        Runnable updater = new Runnable() {
            public void run() {
                // 여기서부터는 main(UI) thread를 활용한다.
                countup_text.setText(getUITime(application.timerSec));
            }
        };
        handler.post(updater);
    }

    // 타이머 정지
    private void stopTimer() {
        second.cancel();
        timerRunning = false;
        soundMeter.stop();
        Btn_stop_ST.setText("계속");
    }

    // 시간 업데이트
    private String getUITime(long time) {
        //int one_day = 1000 * 60 * 60 * 24;
        int one_hour = 1000 * 60 * 60;
        int one_min = 1000 * 60;
        int one_sec = 1000;

        //int hours = (int) time % one_day / one_hour;
        int minutes = (int) time % one_hour / one_min;
        int seconds = (int) time % one_hour % one_min / one_sec;

        String timeLeftText = "";

        // 분이 10보다 작으면 0이 붙는다
        //if (hours < 10) timeLeftText += "0";
        //timeLeftText += hours + ":";

        // 분이 10보다 작으면 0이 붙는다
        if (minutes < 10) timeLeftText += "0";
        timeLeftText += minutes + ":";

        // 초가 10보다 작으면 0이 붙는다
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        return timeLeftText;

    }   // end of Oncreate

    /**
     * 데시벨을 측정하기 위해선 RECORD_AUDIO라는 권한을 사용자로부터 받아야 합니다.
     * RECORD_AUDIO 권한이 있는지 확인합니다.
     */
    private void checkPermission() {
        TedPermission.create()
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Toast.makeText(WaterStopWatch.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(WaterStopWatch.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).setDeniedMessage("권한을 허용하지 않을 경우 서비스를 제대로 이용할 수 없습니다. [Setting] > [Permission]에서 권한을 확인해주세요.")
                .setPermissions(Manifest.permission.RECORD_AUDIO)
                .check();
    }

}   // end of class