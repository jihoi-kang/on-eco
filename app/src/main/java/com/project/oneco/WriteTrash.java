package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class WriteTrash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_trash);

        // todo: 쓰레기 버리기 기능에 대해서는 잘 들은게 없어서,,,
        // todo: 쓰레기 유형을 고르고 몇 g 버렸는지 입력하고 추가버튼을 누르면 계속 오늘 배출한 쓰레기에 쌓이는 것이 맞나요? 맞으면 추가 버튼 누를 때마다 쓰레기양이 증가하게 구현해주세요.

        ImageButton Btn_back = findViewById(R.id.Btn_back);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageButton Tstatistic = findViewById(R.id.Tstatistic);
        Tstatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Statistic.class);
                startActivity(intent);
            }
        });

    }
}