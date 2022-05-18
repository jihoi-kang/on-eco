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

    String[] items = {"Apple", "Orange", "Grape", "item #1", "item #2", "item #3", "item #4", "item #5"};
    String[] tissueItems = { "물티슈(...)", "각티슈(...)", "휴지(10g)" };
    String[] disposableCupItems = { "소주컵", "일반컵", "대용량컵" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // view bind
        listView = findViewById(R.id.lv_fruit);

        // adapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        // 아이템들을 눌렀을 때에 발생하는 이벤트 달기
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long index) {
        String item = items[position];
        Log.d("jay", "item: " + item);
    }
}