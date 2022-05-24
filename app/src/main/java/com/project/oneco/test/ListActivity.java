package com.project.oneco.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.oneco.R;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    ArrayAdapter<String> adapter;

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
        setContentView(R.layout.activity_list);

        // view bind
        listView = findViewById(R.id.Btn_tissue);
        listView = findViewById(R.id.Btn_disposable_cup);
        listView = findViewById(R.id.Btn_disposable_spoon);
        listView = findViewById(R.id.Btn_paper);
        listView = findViewById(R.id.Btn_plastic);
        listView = findViewById(R.id.Btn_plastic_bag);
        listView = findViewById(R.id.Btn_can);
        listView = findViewById(R.id.Btn_empty_bottle);

        // adapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tissueItems);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, disposableCupItems);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, disposableSpoonItems);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, paperItems);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, plasticItems);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, plasticBagItems);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, canItems);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emptyBottleItems);
        listView.setAdapter(adapter);

        // 아이템들을 눌렀을 때에 발생하는 이벤트 달기
        listView.setOnItemClickListener(this);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long index) {
        String tissue = tissueItems[position];
        Log.d("jay", "tissue: " + tissue);
    }


}