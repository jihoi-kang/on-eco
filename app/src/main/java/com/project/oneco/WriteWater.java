package com.project.oneco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class WriteWater extends AppCompatActivity {

    // todo: 손씻기, 샤워 등등을 누를 때마다 저장할 변수 생성(Data type: String)
    // todo: 측정 화면으로 넘어갈 때 type을 넣어줘야됨(OnEcoApplication의 변수로 사용)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_water);

        // 이전 버튼
        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageButton Wstatistic = findViewById(R.id.Wstatistic);
        Wstatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Statistic.class);
                startActivity(intent);
            }
        });

        Button Btn_bef_WTimer = findViewById(R.id.Btn_bef_WTimer);
        Btn_bef_WTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterStopWatch.class);
                startActivity(intent);
            }
        });

        Button Btn_bef_WTimer_Game = findViewById(R.id.Btn_bef_WTimer_Game);
        Btn_bef_WTimer_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterStopGame.class);
                startActivity(intent);
            }
        });

    }
}