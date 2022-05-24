package com.project.oneco;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class WaterStopGame extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Activity간의 데이터 공유를 위한 application 가져오기1-1
    private OnEcoApplication application;

    Spinner spinner;
    String[] item;

    private final ArrayList<Float> dbList = new ArrayList<>();
    private final ArrayList<Float> dbUsedList = new ArrayList<>();
    private final ArrayList<Float> dbSavingList = new ArrayList<>();
    private SoundMeter soundMeter = null;

    private TextView selected_minText; // 타이머 현황

    private Button startButton;
    private Button stopButton;
    private Button Btn_finish_game;

    private CountDownTimer countDownTimer;

    private boolean timerRunning;   // 타이머 상태
    private boolean firstState;     // 처음인지 아닌지
    private boolean start_already;

    private long timeSec = 0;          // 선택한 시간을 초단위로 변경
    private long tempTime = 0;      // 남은 시간
    private int secondText = 0;

    LinearLayout setting;    // 셋팅 화면
    LinearLayout timer;      // 타이머 화면


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_stop_game);


        // Activity간의 데이터 공유를 위한 application 가져오기1-2
        application = (OnEcoApplication) getApplication();

        spinner = (Spinner) findViewById(R.id.spinner);

        selected_minText = findViewById(R.id.selected_minText);
        startButton = findViewById(R.id.countdown_button);
        stopButton = findViewById(R.id.stopButton);
        Btn_finish_game = findViewById(R.id.Btn_finish_game);

        setting = findViewById(R.id.setting);
        timer = findViewById(R.id.timeup);


        checkPermission();

        soundMeter = new SoundMeter();

        timer.setVisibility(View.GONE);

        // 이전버튼을 누르면
        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 상황에 따른 이전 버튼 동작 선택
                if (start_already) {
                    stopTimer();

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


        // 완료버튼 누르면
        // todo: 통계화면 넘어갈 때 물 vs 쓰레기 중 먼저 보여주는 그래프가 다르도록 구현
        Btn_finish_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
                soundMeter.stop();

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
                        dbSavingList.add(dbList.get(i));
                    }
                }

                application.usedWT = dbUsedList.size();
                application.noUsedWT = dbSavingList.size();

                application.usedW = application.usedWT * application.Wpower;

                Intent intent = new Intent(getApplicationContext(), WaterAfterStati.class);
                startActivity(intent);
            }
        });


        // 스피너 구현
        spinner.setOnItemSelectedListener(this);

//        String m3 = "03:00";
//        String m5 = "05:00";
//        String m7 = "07:00";
//        String m10 = "10:00";
//        String m15 = "15:00";

        item = new String[]{"선택하세요", "03:00", "05:00", "07:00", "10:00", "15:00"};

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

    }   // end of onCreate

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
            timeSec = ((Long.parseLong(sMin) * 60*1000) + (Long.parseLong(sSecond) * 1000) + 1000);

            application.timerSec = 0;
            application.RtimerSec = 0;
        } else {
            timeSec = tempTime;
        }

        // Log.d("tag", "time" + time);

        // 데시벨을 얻어오기 위해 sound meter 시작
        if (soundMeter == null) {
            return;
        }
        soundMeter.start();

        countDownTimer = new CountDownTimer(timeSec, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Log.d("tag", "tempTime: " + tempTime);
                tempTime = millisUntilFinished;
                updateTimer();

                application.timerSec += 1000;
                application.RtimerSec ++;

                // soundMeter getAmplitude() 작업 수행
                if (soundMeter == null) {
                    return;
                }
                double amplitude = soundMeter.getAmplitude();
                float db = 20 * (float) (Math.log10(amplitude));
                Log.d("jay", "db: " + db);

                // 데이터를 전역변수의 dbList로 저장한다.
                dbList.add(db);
            }

            @Override
            public void onFinish() {
            }
        }.start();

        stopButton.setText("일시정지");
        timerRunning = true;
        firstState = false;

//        // todo: Time Up되었을 때 기록 중지, 통계화면으로 넘어가기
//        if(tempTime == 0){
//            soundMeter.stop();
//            countDownTimer.cancel();
//            timerRunning = false;
//
//            // 통계화면으로 넘어가기
//            Intent intent = new Intent(getApplicationContext(), WaterAfterStati.class);
//            startActivity(intent);
//        }
    }

    // 타이머 정지
    private void stopTimer() {
        soundMeter.stop();
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
        if(i != 0){
            Toast.makeText(getApplicationContext(), "제한 시간 " +item[i]+ "을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        selected_minText.setText("");
    }

    /**
     * 데시벨을 측정하기 위해선 RECORD_AUDIO라는 권한을 사용자로부터 받아야 합니다.
     * RECORD_AUDIO 권한이 있는지 확인합니다.
     */
    private void checkPermission() {
        TedPermission.create()
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Toast.makeText(WaterStopGame.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(WaterStopGame.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).setDeniedMessage("권한을 허용하지 않을 경우 서비스를 제대로 이용할 수 없습니다. [Setting] > [Permission]에서 권한을 확인해주세요.")
                .setPermissions(Manifest.permission.RECORD_AUDIO)
                .check();
    }

}   // end of class
