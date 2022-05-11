package com.project.oneco.test;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.project.oneco.R;
import com.project.oneco.SoundMeter;
import com.project.oneco.data.PreferenceManager;
import com.project.oneco.data.WaterUsage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity {
    private TextView tvSec;
    private TimerTask second;
    private int timerSec = 0;
    private final Handler handler = new Handler();
    private final ArrayList<Float> dbList = new ArrayList<>();
    private final ArrayList<Float> dbUsedList = new ArrayList<>();
    private final ArrayList<Float> dbSavingList = new ArrayList<>();
    private SoundMeter soundMeter = null;
    private float Wpower = 80f;
    private float usedW = 0f;      // 물 사용량 = Wpower * usedWT
    private float usedWT = 0f;      // 물 사용 시간
    private float savingWT = 0f;    // 물 절약 시간

    // 사용자가 누르는 버튼에 따른 수압 및 물 사용량 처리
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.Wstrenth:
                    Wpower = 140;
                    break;
                case R.id.Wmiddle:
                    Wpower = 80;
                    break;
                case R.id.Wweakness:
                    Wpower = 35;
                    break;
            }

            Log.d("jay", "Wpower: " + Wpower);
        }
    };

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_test);

            // todo: OnEcoApplication에 저장된 값을 가져옴(물 사용량의 유형)

            checkPermission();

            soundMeter = new SoundMeter();

            tvSec = findViewById(R.id.tv_sec);
            Button btnStop = findViewById(R.id.btn_stop);
            Button btnStart = findViewById(R.id.btn_start);
            Button Wstrenth = findViewById(R.id.Wstrenth);
            Button Wmiddle = findViewById(R.id.Wmiddle);
            Button Wweakness = findViewById(R.id.Wweakness);

            // todo: 나중에 일시정지 및 계속 기능 구현
            Wstrenth.setOnClickListener(listener);
            Wmiddle.setOnClickListener(listener);
            Wweakness.setOnClickListener(listener);

            // todo: need to remove(SharedPreference 실습을 위한 코드)
            tvSec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PreferenceManager manager = PreferenceManager.getInstance(TestActivity.this);
                    Gson gson = new Gson();

                    // data 만들기
                    WaterUsage usage = new WaterUsage();
                    usage.setDish(3f);
                    usage.setHand(7f);

                    // data를 String화 시키기
                    String json = gson.toJson(usage);

                    // 변환한 String 값을 SharedPreference에 저장
                    manager.putString("0508", json);

                    // 데이터 꺼내오기
                    String data = manager.getString("0508", "");

                    // String을 데이터 모델로 변경
                    WaterUsage waterUsage = gson.fromJson(data, WaterUsage.class);
                    Log.d("jay", "dish: " + waterUsage.getDish());
                    Log.d("jay", "hand: " + waterUsage.getHand());
                }
            });

            // 정지 버튼을 누르면
            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Timer, SoundMeter를 중지시킨다.
                    second.cancel();
                    soundMeter.stop();

                    // todo: 지금까지 모아둔 데시벨 값을 파싱한다.
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

                    usedWT = dbUsedList.size();
                    savingWT = dbSavingList.size();

                    usedW = usedWT * Wpower;

                    setResult();

                    // todo: 이전에 저장해둔 데이터이력이 있는지 확인
                    // todo: 주석되어 있는 코드는 오늘 날짜를 String화 시킨 작업입니다.(ex. 20220511, 20220512) SharedPreference Key값으로 사용합니다.
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//                    Date today = Calendar.getInstance().getTime();
//                    String today = dateFormat.format(today);
                    PreferenceManager manager = PreferenceManager.getInstance(TestActivity.this);
                    String data = manager.getString("0508", "");
                    WaterUsage waterUsage;
                    Gson gson = new Gson();
                    if (data.equals("")) {
                        waterUsage = new WaterUsage();
                    } else {
                        waterUsage = gson.fromJson(data, WaterUsage.class);
                    }

                    // todo: type에 따른 데이터 만들어주기
//                    if (type.equals("hand")) {
//                        // todo: 기존에 값이 있으면 어떡해??
//                        float hand = waterUsage.getHand();
//                        waterUsage.setHand(hand + average);
//                    } else if (type.equals("dish")) {
//                        waterUsage.setDish(average);
//                    } else if (type.equals("etc")) {
//                        waterUsage.setEtc(average);
//                    }
                    // todo: 데이터 저장
                    manager.putString("0508", gson.toJson(waterUsage));
                }
            });


            // 측정 시작 버튼을 누르면
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 데시벨을 얻어오기 위해 sound meter 시작
                    if (soundMeter == null) {
                        return;
                    }
                    soundMeter.start();

                    // timer 시작
                    startTimer();
                }
            });
        }

        // 타이머 시작 메서드
        public void startTimer() {
            // 초기화 작업
            timerSec = 0;
            second = new TimerTask() {  // TimerTask는 안드로이드 클래스
                @Override
                public void run() {
                    Log.d("jay", "Timer start: " + timerSec); // 로그 찍기

                    // soundMeter getAmplitude() 작업 수행
                    if (soundMeter == null) {
                        return;
                    }
                    double amplitude = soundMeter.getAmplitude();
                    float db = 20 * (float) (Math.log10(amplitude));
                    Log.d("jay", "db: " + db);

                    // 데이터를 전역변수의 dbList로 저장한다.
                    dbList.add(db);
                    timerSec++;
                    setSec();
                }
            };
            Timer timer = new Timer();  // Timer는 안드로이드 클래스
            timer.schedule(second, 0, 1000);
        }


        // UI 변경 메서드
        private void setSec() {
            // todo: 1초, 2초, 3초, ... , 61초, 62초, 63초... 를 00:00:00 모양으로 변환
            Runnable updater = new Runnable() {
                public void run() {
                    // 여기서부터는 main(UI) thread를 활용한다.
                    tvSec.setText(timerSec + "초");
                }
            };
            handler.post(updater);
        }

        private void setResult() {
            Runnable updater = new Runnable() {
                public void run() {
                    TextView usedWater = findViewById(R.id.usedWater);
                    usedWater.setText("사용한 물의 양 : " + usedW + "ml");

                    TextView spended_AllT = findViewById(R.id.spended_AllT);
                    spended_AllT.setText("총 소요 시간 : " + timerSec + "초");

                    TextView spended_RealT = findViewById(R.id.spended_RealT);
                    spended_RealT.setText("물 사용 시간 : " + usedWT + "초");

                    TextView savingT = findViewById(R.id.savingT);
                    savingT.setText("물 절약 시간 : " + savingWT + "초");
                }
            };
            handler.post(updater);
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
                            Toast.makeText(TestActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionDenied(List<String> deniedPermissions) {
                            Toast.makeText(TestActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }).setDeniedMessage("권한을 허용하지 않을 경우 서비스를 제대로 이용할 수 없습니다. [Setting] > [Permission]에서 권한을 확인해주세요.")
                    .setPermissions(Manifest.permission.RECORD_AUDIO)
                    .check();
        }


        private void test() {
            // 배열 추가할 때
            dbList.add(1.0f);
            dbList.add(2.5f);
            dbList.add(3.5f);
            dbList.add(4.5f);
            // 배열값 가져 올 때
//                dbList.get(0); // 1.3f
//                dbList.get(1); // 2.7f
            // 배열 초기화
//                dbList.clear();
            // 반복문
            float total = 0f;
            for (int i = 0; i < dbList.size(); i++) {
                total = total + dbList.get(i);
                // 0 + 1.0f = 1.0f
                // 1.0f + 2.5f = 3.5f
                // 3.5f + 3.5f = 7f
                // 7f + 4.5f = 11.5f
            }
            float average = total / dbList.size();
            for (int i = 0; i < dbList.size(); i++) {
                if (dbList.get(i) > average) {
                    // 물을 사용하는 중이라는 뜻
                } else {
                    // 물을 사용하지 않고 있다는 뜻
                }
            }
        }


}