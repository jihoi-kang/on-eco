package com.project.oneco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WriteWater extends AppCompatActivity{
    // todo: 측정 화면으로 넘어갈 때 type을 넣어줘야됨(OnEcoApplication의 변수로 사용)

    // 데이터 정의(오늘 사용한 물 사용량, 전일 대비 절약한 물 사용량)
    private String todayWater;
    private String removedWater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_water);

        // Activity간의 데이터 공유를 위한 application 가져오기
        OnEcoApplication application = (OnEcoApplication) getApplication();


        // 이전 버튼
        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // 홈 화면으로 넘어가기
        TextView title_ONECO = findViewById(R.id.title_ONECO);
        title_ONECO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainHome.class);
                startActivity(intent);
            }
        });

        // 통계 화면으로 넘어가기
        ImageButton Wstatistic = findViewById(R.id.Wstatistic);
        Wstatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Statistic.class);
                startActivity(intent);
            }
        });

        // 물 사용량 측정 버튼 눌렀을 때
        Button Btn_bef_WTimer = findViewById(R.id.Btn_bef_WTimer);
        Btn_bef_WTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    // Toast message : 물 사용 유형 먼저 선택하세요
                if (application.waterType == null){
                    Toast.makeText(getApplicationContext(), "사용할 물의 유형을 먼저 선택해주세요", Toast.LENGTH_SHORT).show();
                } else{ // 물 사용량 측정 화면으로 넘어가기
                    Intent intent = new Intent(getApplicationContext(), WaterStopWatch.class);
                    startActivity(intent);

                    application.waterType = null;
                }
            }
        });

        // 샤워 타이머 게임 화면으로 넘어가기
        Button Btn_bef_WTimer_Game = findViewById(R.id.Btn_bef_WTimer_Game);
        Btn_bef_WTimer_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterStopGame.class);
                startActivity(intent);
            }
        });

        Button Btn_toothBrush = findViewById(R.id.Btn_toothBrush);
        Button Btn_handWash = findViewById(R.id.Btn_handWash);
        Button Btn_faceWash = findViewById(R.id.Btn_faceWash);
        Button Btn_shower = findViewById(R.id.Btn_shower);
        Button Btn_dishWash = findViewById(R.id.Btn_dishWash);
        Button Btn_etc_water = findViewById(R.id.Btn_etc_water);


        // Button을 눌렀을 때 waterType에 물 사용 유형 저장
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){

                    case R.id.Btn_toothBrush:
                        application.waterType = "toothBrush";
                        break;

                    case R.id.Btn_handWash:
                        application.waterType = "handWash";
                        break;

                    case R.id.Btn_faceWash:
                        application.waterType = "faceWash";
                        break;

                    case R.id.Btn_shower:
                        application.waterType = "shower";
                        break;

                    case R.id.Btn_dishWash:
                        application.waterType = "dishWash";
                        break;

                    case R.id.Btn_etc_water:
                        application.waterType = "etc_water";
                        break;
                }
            }
        };

        Btn_toothBrush.setOnClickListener(onClickListener);
        Btn_handWash.setOnClickListener(onClickListener);
        Btn_faceWash.setOnClickListener(onClickListener);
        Btn_shower.setOnClickListener(onClickListener);
        Btn_dishWash.setOnClickListener(onClickListener);
        Btn_etc_water.setOnClickListener(onClickListener);


    } // end of onClick
} // end of class