package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class WriteTrash extends AppCompatActivity {
    // todo: 쓰레기 종류
    private String trashType;

    // todo: 데이터 정의(오늘 배출한 쓰레기, 전일 대비 절약한 쓰레기)
    private String todayTrash;
    private String removedTrash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_trash);

<<<<<<< Updated upstream
        // todo: 쓰레기 버리기 기능에 대해서는 잘 들은게 없어서,,,
        // todo: 쓰레기 유형을 고르고 몇 g 버렸는지 입력하고 추가버튼을 누르면 계속 오늘 배출한 쓰레기에 쌓이는 것이 맞나요? 맞으면 추가 버튼 누를 때마다 쓰레기양이 증가하게 구현해주세요.
=======
        // todo: Button 9개 bind
        Button Btn_tissue = findViewById(R.id.Btn_tissue);
        Button Btn_disposable_cup = findViewById(R.id.Btn_disposable_cup);
        Button Btn_disposable_spoon = findViewById(R.id.Btn_disposable_spoon);
        Button Btn_paper = findViewById(R.id.Btn_paper);
        Button Btn_plastic = findViewById(R.id.Btn_plastic);
        Button Btn_plastic_bag = findViewById(R.id.Btn_plastic_bag);
        Button Btn_can = findViewById(R.id.Btn_can);
        Button Btn_empty_bottle = findViewById(R.id.Btn_empty_bottle);
        Button Btn_etc = findViewById(R.id.Btn_etc);


        // todo: Button을 눌렀을 때 trashType에 저장.
        Btn_tissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        // todo: RecyclerView


>>>>>>> Stashed changes

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