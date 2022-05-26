package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.project.oneco.test.DialogTest;

public class WaterAfterStati extends AppCompatActivity {

    public final Handler handler = new Handler();

    OnEcoApplication application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_after_stati);

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
            }
        });

        setResult();

        // 초기화
        application.Wtap = "null";
        application.Wpower = 0f;


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

}   // end of class