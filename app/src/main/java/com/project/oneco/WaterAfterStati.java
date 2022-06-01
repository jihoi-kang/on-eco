package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.project.oneco.data.PreferenceManager;
import com.project.oneco.data.WaterUsage;
import com.project.oneco.test.DialogTest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WaterAfterStati extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private Gson gson;

    public final Handler handler = new Handler();

    OnEcoApplication application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_after_stati);


        preferenceManager = PreferenceManager.getInstance(this);
        gson = new Gson();

        // Activity간의 데이터 공유를 위한 application 가져오기
        application = (OnEcoApplication) getApplication();

        // todo: 통계화면 넘어갔을 때 물 vs 쓰레기 중 먼저 보여주는 그래프가 다르도록 구현
        // 통계화면으로 넘어가기
        Button goto_statistic = findViewById(R.id.goto_statistic);
        goto_statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Statistic.class);
                startActivity(intent);
                application.active_activity = "waterAfterStati";
                setInit();  // 설정 초기화
            }
        });

        setResult();

        // todo: 잘 되긴하는데.. 새로 만들었는데도 같은 곳에 저장이 되는 이유?
        // SharedPreference 저장 과정
        // 오늘의 날짜를 구한 후 key값으로 등록 - 0531 todo:년도도 표시하도록 220531, 소문자dd면 0507이 아니라 057으로 나오는게 아닌지?
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMdd", Locale.getDefault());
        String key = simpledateformat.format(calendar.getTime());
        String waterUsageStr = preferenceManager.getString(key + "-water-usage", "");

        // data 만들기 - WaterUseage 클래스 객체 생성
        WaterUsage waterUsage;

        if (waterUsageStr.equals("")) {
            waterUsage = new WaterUsage();
        } else {
            waterUsage = gson.fromJson(waterUsageStr, WaterUsage.class);
        }

        // 물 타입에 따라 측정 값이 들어감
        if (application.waterType.equals("tooth")) {
            float tooth = waterUsage.getTooth();
            waterUsage.setTooth(tooth + application.usedW);
        } else if (application.waterType.equals("hand")) {
            float hand = waterUsage.getHand();
            waterUsage.setHand(hand + application.usedW);
        } else if (application.waterType.equals("face")) {
            float face = waterUsage.getFace();
            waterUsage.setFace(face + application.usedW);
        } else if (application.waterType.equals("shower")) {
            float shower = waterUsage.getShower();
            waterUsage.setShower(shower + application.usedW);
        } else if (application.waterType.equals("dish")) {
            float dish = waterUsage.getDish();
            waterUsage.setDish(dish + application.usedW);
        } else if (application.waterType.equals("etc_water")) {
            float etc_water = waterUsage.getEtcWater();
            waterUsage.setEtcWater(etc_water + application.usedW);
        }

        // localStorage에 저장
        String updatedWaterUsage = gson.toJson(waterUsage);
        preferenceManager.putString(key + "-water-usage", updatedWaterUsage);

        // todo: 그 전의 값이 뜨는 이유?
        Log.d("jay", "waterUsageStr: " + waterUsageStr);

        // 초기화
        setInit();


    } // end of onCreate


    // todo:00분 00초로 바꾸기
    public void setResult() {
        Runnable updater = new Runnable() {
            public void run() {
                TextView usedWater = findViewById(R.id.usedWater);
                usedWater.setText("사용한 물의 양 : " + application.usedW + "ml");

                TextView spended_AllT = findViewById(R.id.countup_text);
                spended_AllT.setText("총 소요 시간 : " + application.RtimerSec + "초");

                TextView spended_RealT = findViewById(R.id.spended_RealT);
                spended_RealT.setText("물 사용 시간 : " + application.usedWT + "초");

                TextView savingT = findViewById(R.id.no_SpendedT);
                savingT.setText("물 절약 시간 : " + application.noUsedWT + "초");
            }
        };
        handler.post(updater);
    }

    // 뒤로 가기 버튼 막기
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    // 설정 초기화
    private void setInit(){
        application.waterType = null;
        application.Wtap = "null";
        application.Wpower = 0f;
    }

}   // end of class