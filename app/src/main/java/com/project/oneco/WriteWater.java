package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class WriteWater extends AppCompatActivity {

    // todo: 손씻기, 샤워 등등을 누를 때마 값을 변경해줘됨
    // todo: 측정 화면으로 넘어갈 때 type을 넣어줘야됨
    private String type = "hand";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_water);


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
                Intent intent = new Intent(getApplicationContext(), WaterTimer.class);
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