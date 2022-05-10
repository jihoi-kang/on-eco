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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity {
    private TextView tvSec;
    private TimerTask second;
    private int timerSec = 0;
    private final Handler handler = new Handler();
    private final ArrayList<Float> dbList = new ArrayList<>();
    private SoundMeter soundMeter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //
        String type = getIntent().getStringExtra("type");

        checkPermission();

        soundMeter = new SoundMeter();

        tvSec = findViewById(R.id.tv_sec);
        Button btnStop = findViewById(R.id.btn_stop);
        Button btnStart = findViewById(R.id.btn_start);

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

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: Timer, SoundMeter를 중지시킨다.
                second.cancel();
                soundMeter.stop();
                // todo: 지금까지 모아둔 데시벨 값을 파싱한다.

                // todo: 이전에 저장해둔 데이터이력이 있는지 확인
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
                if (type.equals("hand")) {
                    // todo: 기존에 값이 있으면 어떡해??
                    float hand = waterUsage.getHand();
                    waterUsage.setHand(hand + average);
                } else if (type.equals("dish")) {
                    waterUsage.setDish(average);
                } else if (type.equals("etc")) {
                    waterUsage.setEtc(average);
                }
                // todo: 데이터 저장
                manager.putString("0508", gson.toJson(waterUsage));
            }
        });
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

    public void startTimer() {
        // 초기화 작업
        timerSec = 0;
        second = new TimerTask() {
            @Override
            public void run() {
                Log.d("jay", "Timer start: " + timerSec); // 로그 찍기
                updateUi();
                // todo: sound meter getAmplitude() 작업 수행
                if (soundMeter == null) {
                    return;
                }
                double amplitude = soundMeter.getAmplitude();
                float db = 20 * (float) (Math.log10(amplitude));
                Log.d("jay", "db: " + db);
                // todo: 데이터를 전역변수의 ㅇㅇㅇ으로 저장한다.
                dbList.add(db);
                timerSec++;
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 1000);
    }

    private void updateUi() {
        // todo: 00:00:00 구현
        Runnable updater = new Runnable() {
            public void run() {
                // 여기서부터는 main(UI) thread를 활용한다.
                tvSec.setText(timerSec + "초");
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
