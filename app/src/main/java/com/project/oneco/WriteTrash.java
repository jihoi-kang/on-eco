package com.project.oneco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class WriteTrash extends AppCompatActivity implements AdapterView.OnItemClickListener {
    // todo: 쓰레기 종류
    private String trashType;

    // todo: 데이터 정의(오늘 배출한 쓰레기, 전일 대비 절약한 쓰레기)
    private String todayTrash;
    private String removedTrash;
    private int currentItemWeight = 0;

    EditText etItemWeight;

    ListView lvList;
    ArrayAdapter<String> adapter;

    // todo: listView에 들어갈 item들 정의(9개)
    String[] tissueItems = { "물티슈", "각티슈", "손 닦는 휴지", "두루말이 휴지" };
    String[] disposableCupItems = { "종이 정수기컵", "종이 자판기컵", "종이 Tall 사이즈(355ml)", "종이 Grande 사이즈(473ml)", "종이 Venti 사이즈(591ml)",
            "플라스틱 Tall 사이즈(355ml)", "플라스틱 Grande 사이즈(473ml)", "플라스틱 Venti 사이즈(591ml)"};
    String[] disposableSpoonItems = {"일회용 수저", "일회용 그릇"};
    String[] paperItems = {"A4", "B4", "택배박스 1호(50cm)", "택배박스 2호(60cm)", "택배박스 3호(80cm)", "택배박스 4호(100cm)", "택배박스 5호(120cm)"};
    String[] plasticItems = {"250ml", "500ml", "1L", "2L"};
    String[] plasticBagItems = {"3L", "5L", "10L", "20L"};
    String[] canItems = {"250ml", "355ml", "500ml", "750ml", "참치캔"};
    String[] emptyBottleItems = {"100ml", "180ml"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_trash);

        // todo: listview bind & item click listener 달기(setOnItemClickListener)
        lvList = findViewById(R.id.lv_list);
        lvList.setOnItemClickListener(this);

        etItemWeight = findViewById(R.id.UserInput_TodayT);
        TextView tvTodayTrash = findViewById(R.id.textView10);

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
        Button Btn_add = findViewById(R.id.button);

        // todo: Button을 눌렀을 때 trashType에 저장.(9개 모두 구현)
        Btn_tissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: trashType에 쓰레기 유형 저장
                trashType = "tissue";
                // todo: item들을 riteTrash 셋해준다.
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, tissueItems);
                lvList.setAdapter(adapter);
            }
        });

        Btn_disposable_cup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "disposable_cup";
                // todo: item들을 riteTrash 셋해준다.
                adapter = new ArrayAdapter<String>(WriteTrash.this, android.R.layout.simple_list_item_1, disposableCupItems);
                lvList.setAdapter(adapter);
            }
        });

        Btn_disposable_spoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "disposable_spoon";
            }
        });

        Btn_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "paper";
            }
        });

        Btn_plastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "plastic";
            }
        });

        Btn_plastic_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "plastic_bag";
            }
        });

        Btn_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "can";
            }
        });

        Btn_empty_bottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "empty_bottle";
            }
        });

        Btn_etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trashType = "etc";
            }
        });

        Btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvTodayTrash.setText(currentItemWeight + "g");
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
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long index) {
        // trashType이 뭔지 알아야되
        String itemName = null;
        if (trashType.equals("tissue")) {
            itemName = tissueItems[position];
            // 물티슈, 각티슈 등등 에 따라 값을 넣어주는거에요(editTextView에)
            if (itemName.equals("물티슈")) {
                currentItemWeight = 180;
            } else if (itemName.equals("각티슈")) {
                currentItemWeight = 140;
            }
            etItemWeight.setText("" + currentItemWeight);
        } else if (trashType.equals("disposable_cup")) {
            itemName = disposableCupItems[position];
        }
        // todo: 나머지 item들도 구현 해야함.
        Log.d("jay", "itemName: " + itemName);
    }

}