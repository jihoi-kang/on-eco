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

    // todo: listView에 들어갈 item들 정의(9개)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_trash);

        // todo: listview bind & item click listener 달기(setOnItemClickListener)

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

        // todo: Button을 눌렀을 때 trashType에 저장.(9개 모두 구현)
        Btn_tissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: trashType에 쓰레기 유형 저장
                // todo: item들을 listview에 셋해준다.
            }
        });

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

    // todo: onItemClick 리스너 구현
    // todo: Item을 눌렀을 때에 해당하는 아이템을 setText해주기!

}