package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.project.oneco.data.PreferenceManager;
import com.project.oneco.data.WaterUsage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WaterAfterStati extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private Gson gson;

    public final Handler handler = new Handler();

    OnEcoApplication application;

    TextView spended_AllT;
    TextView spended_RealT;
    TextView no_SpendedT;
    TextView usedWater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_after_stati);


        spended_AllT = findViewById(R.id.spended_AllT);
        spended_RealT = findViewById(R.id.spended_RealT);
        no_SpendedT = findViewById(R.id.no_SpendedT);
        usedWater = findViewById(R.id.usedWater);

        preferenceManager = PreferenceManager.getInstance(this);
        gson = new Gson();

        // Activity간의 데이터 공유를 위한 application 가져오기
        application = (OnEcoApplication) getApplication();

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

        // SharedPreference 저장 과정
        // 오늘의 날짜를 구한 후 key값으로 등록 - 220608
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
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

        Log.d("jay", "waterUsageStr: " + waterUsageStr);

        // 초기화
        setInit();


    } // end of onCreate


    // 00분 00초로 바꾸기
    public void setResult() {
        Runnable updater = new Runnable() {
            public void run() {
                usedWater.setText("사용한 물의 양  : " + application.usedW + " ml");
            }
        };
        handler.post(updater);



        Log.d("jay", "application.RtimerSec: " + application.RtimerSec);
        Log.d("jay", "application.usedWT: " + application.usedWT);
        Log.d("jay", "application.noUsedWT: " + application.noUsedWT);

        String displayTotalTime = updateTimer(application.RtimerSec);
        String displayUsedTime = updateTimer(application.usedWT);
        String displayNoUsedTime = updateTimer(application.noUsedWT);

        spended_AllT.setText("총 소요 시간      : " + displayTotalTime);
        spended_RealT.setText("물 사용 시간      : " + displayUsedTime);
        no_SpendedT.setText("물 미사용 시간  : " + displayNoUsedTime);
    }

    // 시간 업데이트
    private String updateTimer(float water) {
        int one_hour = 1 * 60 * 60;
        int one_min = 1 * 60;
        int one_sec = 1;

        int minutes = (int) water % one_hour / one_min;
        int seconds = (int) water % one_hour % one_min / one_sec;

        String timeLeftText = "";

        // 분이 10보다 작으면 0이 붙는다
        if (minutes < 10) timeLeftText += "0";
        timeLeftText += minutes + "분 ";

        // 초가 10보다 작으면 0이 붙는다
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds + "초";

        return timeLeftText;
    }

    // 뒤로 가기 버튼 막기
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    // 설정 초기화
    private void setInit(){
        application.waterType = "";
        application.Wtap = "";
        application.Wpower = 0f;
    }

}   // end of class