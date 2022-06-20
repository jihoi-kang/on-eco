package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GamePF extends AppCompatActivity {

    OnEcoApplication application;

    ImageView Img_sun;
    TextView Txt_point;
    TextView Txt_pf;
    TextView Txt_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_pf);

        application = (OnEcoApplication) getApplication();

        Img_sun = findViewById(R.id.Img_sun);
        Txt_point = findViewById(R.id.Txt_point);
        Txt_pf = findViewById(R.id.Txt_pf);
        Txt_message = findViewById(R.id.Txt_message);


        // 3초 뒤, 통계 화면으로 자동으로 넘어가기
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(getApplicationContext(), WaterAfterStati.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

        // todo: 전체 포인트 프리퍼런스 저장
        // 포인트 계산
        if (application.min1){
            application.setPoint(10);
            Txt_point.setText("+10");
        } else if (application.min3){
//            application.gamePoint = 2000;
//            application.point += application.gamePoint;
            Txt_point.setText("+2000");
        } else if (application.min5){
//            application.gamePoint = 1500;
//            application.point += application.gamePoint;
            Txt_point.setText("+1500");
        } else if (application.min7){
//            application.gamePoint = 1000;
//            application.point += application.gamePoint;
            Txt_point.setText("+1000");
        } else if (application.min10){
//            application.gamePoint = 700;
//            application.point += application.gamePoint;
            Txt_point.setText("+700");
        } else if (application.min15){
//            application.gamePoint = 500;
//            application.point += application.gamePoint;
            Txt_point.setText("+500");
        } else {
            Img_sun.setVisibility(View.INVISIBLE);
//            application.gamePoint = 10;
//            application.point += application.gamePoint;
            Txt_pf.setText("게임 실패...");
            Txt_message.setText("아쉬워요~\n좀만 더 분발해봅시다!");
        }


        application.min1 = false;
        application.min3 = false;
        application.min5 = false;
        application.min7 = false;
        application.min10 = false;
        application.min15 = false;

    }
}